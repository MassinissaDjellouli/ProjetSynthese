package com.synthese.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synthese.dto.CourseDTO;
import com.synthese.dto.TeacherClassesDTO;
import com.synthese.dto.TeacherScheduleGenerationInfoDTO;
import com.synthese.exceptions.ChatGPTException;
import com.synthese.exceptions.EstablishmentNotFoundException;
import com.synthese.exceptions.ProgramNotFoundException;
import com.synthese.exceptions.ScheduleGenerationException;
import com.synthese.model.ChatGPT.ChatGPTMessage;
import com.synthese.model.ChatGPT.ChatGPTResponse;
import com.synthese.model.*;
import com.synthese.repository.CourseRepository;
import com.synthese.repository.EstablishmentRepository;
import com.synthese.repository.ProgramRepository;
import com.synthese.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class ScheduleGenerationService {
    private static final int MAX_CORRECTION_DEPTH = 10;
    private static final int MAX_PERIODS_IN_BLOCK = 4;
    private final ChatGPTService chatGPTService;
    private final EstablishmentRepository establishmentRepository;
    private final ProgramRepository programRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final String JSON_DELIMITER = "____________________________________________________";
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final ObjectMapper mapper = getMapper();

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return mapper;
    }

    public void generateScheduleForProgram(ObjectId programID) throws EstablishmentNotFoundException, ProgramNotFoundException, ChatGPTException, ScheduleGenerationException {
        TeacherScheduleGenerationInfoDTO teacherScheduleGenerationInfoDTO = createTeachersScheduleGenerationInfo(programID);
        ChatGPTResponse chatGPTResponse = generateTeachersSchedule(teacherScheduleGenerationInfoDTO);
        processResponse(chatGPTResponse, teacherScheduleGenerationInfoDTO);
    }

    private TeacherScheduleGenerationInfoDTO createTeachersScheduleGenerationInfo(ObjectId programID) throws ProgramNotFoundException, EstablishmentNotFoundException {
        Optional<Program> programOpt = programRepository.findById(programID);
        if (programOpt.isEmpty()) {
            throw new ProgramNotFoundException();
        }
        Program program = programOpt.get();
        Optional<Establishment> estOpt = establishmentRepository.findById(program.getEstablishment());
        if (estOpt.isEmpty()) {
            throw new EstablishmentNotFoundException();
        }
        List<Course> courses = courseRepository.findByProgram(programID);
        List<Teacher> teachers = teacherRepository.findAll();
        teachers = teachers.stream().filter(teacher ->
                        new HashSet<>(
                                courses.stream().map(Course::getId).toList())
                                .containsAll(teacher.getCourses()))
                .toList();
        Establishment establishment = estOpt.get();
        List<TeacherClassesDTO> teachersClasses = teachers.stream().map(teacher -> {
            List<Course> teacherCourses = teacher.getCourses().stream().map(courseID -> {
                Optional<Course> courseOpt = courses.stream()
                        .filter(course -> course.getId().equals(courseID)).findFirst();
                return courseOpt.orElseThrow();
            }).toList();
            return TeacherClassesDTO.builder()
                    .teacherID(teacher.getId().toString())
                    .classes(teacherCourses.stream().map(Course::toDTO).toList())
                    .build();
        }).toList();
        return TeacherScheduleGenerationInfoDTO.builder()
                .programID(program.getId().toString())
                .establishment(establishment.toDTO())
                .teachersClasses(teachersClasses)
                .build();
    }


    private List<String> validateSchedule(ProgramTeachersSchedule schedule, TeacherScheduleGenerationInfoDTO scheduleGenerationInfo) throws ScheduleGenerationException {
        List<String> corrections = new ArrayList<>(validateNoNull(schedule, scheduleGenerationInfo.getTeachersClasses()));
        if (!corrections.isEmpty()) {
            return corrections;
        }
        corrections.addAll(validateLengths(schedule, scheduleGenerationInfo.getEstablishment().toModel()));
        corrections.addAll(validateNonConflicting(schedule, scheduleGenerationInfo.getEstablishment().toModel()));
        return corrections.size() > 30 ? corrections.subList(0, 29) : corrections;
    }

    private List<String> validateNoNull(ProgramTeachersSchedule schedule, List<TeacherClassesDTO> teachersClasses) throws ScheduleGenerationException {
        List<String> corrections = new ArrayList<>();
        for (TeachersSchedule teachersSchedule : schedule.getTeachersSchedules()) {
            for (CourseScheduleBlock courseScheduleBlock : teachersSchedule.getScheduleBlocks()) {
                if (courseScheduleBlock.getCourseId() == null) {
                    Optional<TeacherClassesDTO> teacherClassesOpt = teachersClasses.stream().filter(teacherClassesDTO ->
                            teacherClassesDTO.getTeacherID().equals(teachersSchedule.getTeacherId())).findFirst();
                    if (teacherClassesOpt.isEmpty()) {
                        throw new ScheduleGenerationException();
                    }
                    List<CourseDTO> classes = teacherClassesOpt.get().getClasses();
                    if (classes.isEmpty()) {
                        corrections.add("Teacher " + teachersSchedule.getTeacherId()
                                + " has a schedule but he does not have any classes assigned. Make sure he does not have any schedule blocks.");
                        continue;
                    }
                    corrections.add("Teacher " + teachersSchedule.getTeacherId() +
                            " has schedule blocks with null course id. Make sure there is a course id for each schedule block." +
                            "For this teacher, it must be one of the following:" + String.join(",", classes.stream().map(CourseDTO::getId).toList()));
                }
            }
        }
        return corrections;
    }

    private List<String> validateLengths(ProgramTeachersSchedule schedule, Establishment establishment) throws ScheduleGenerationException {
        List<String> corrections = new ArrayList<>();
        List<String> possibleStartTimes = getPossibleStartTimes(establishment);
        for (TeachersSchedule teacherSchedule : schedule.getTeachersSchedules()) {
            Map<String, Long> coursesHours = new HashMap<>();
            for (CourseScheduleBlock courseScheduleBlock : teacherSchedule.getScheduleBlocks()) {
                addToOrIncrement(coursesHours, courseScheduleBlock.getCourseId(), establishment.getPeriodLength() * courseScheduleBlock.getPeriods());
                if (!establishment.getDaysPerWeek().contains(courseScheduleBlock.getDay())) {
                    corrections.add(
                            "Teacher " + teacherSchedule.getTeacherId() + " has a course on day "
                                    + courseScheduleBlock.getDay() +
                                    " that is not in the establishment schedule, make sure the day is in the following days: "
                                    + String.join(",", establishment.getDaysPerWeek()));
                }
                if (courseScheduleBlock.getPeriods() > MAX_PERIODS_IN_BLOCK) {
                    corrections.add(
                            "Teacher " + teacherSchedule.getTeacherId() + " has a course on day " + courseScheduleBlock.getDay()
                                    + "that has more than "
                                    + MAX_PERIODS_IN_BLOCK + " periods in a block, make sure the course has less than "
                                    + MAX_PERIODS_IN_BLOCK + " periods in a block");
                }
                if (!possibleStartTimes.contains(courseScheduleBlock.getStartTime())) {
                    corrections.add(
                            "Teacher " + teacherSchedule.getTeacherId() + " has a course that starts at "
                                    + courseScheduleBlock.getStartTime() +
                                    " that is not in the establishment schedule, make sure the start time is in the following times: "
                                    + String.join(",", possibleStartTimes));
                }
            }
            for (String course : coursesHours.keySet()) {
                Optional<Course> courseOpt = courseRepository.findById(new ObjectId(course));
                if (courseOpt.isEmpty()) {
                    throw new ScheduleGenerationException();
                }
                if (coursesHours.get(course) < courseOpt.get().getHoursPerWeek()) {
                    corrections.add(
                            "Teacher " + teacherSchedule.getTeacherId() + " has a course " + course +
                                    " that has less hours than the course requires, make sure the course has exactly "
                                    + courseOpt.get().getHoursPerWeek() + " hours per week");
                }
                if (coursesHours.get(course) > courseOpt.get().getHoursPerWeek()) {
                    corrections.add(
                            "Teacher " + teacherSchedule.getTeacherId() + " has a course " + course +
                                    " that has " + coursesHours.get(course) + " hours per week. Reduce it to " + courseOpt.get().getHoursPerWeek() + " by reducing the periods per schedule block or by reducing the number of schedule blocks");
                }
            }
        }
        return corrections;
    }

    private List<String> getPossibleStartTimes(Establishment establishment) {
        List<String> possibleStartTimes = new ArrayList<>();
        possibleStartTimes.add(establishment.getClassesStartTime());
        boolean isLastTime = false;
        LocalTime lastTimeAdded = LocalTime.parse(establishment.getClassesStartTime(), TIME_FORMATTER);
        int currentPeriod = 1;
        while (!isLastTime) {
            if (currentPeriod == establishment.getPeriodsBeforeDinner() + 1) {
                lastTimeAdded = lastTimeAdded
                        .plusMinutes(establishment.getDinnerLength())
                        .minusMinutes(establishment.getBetweenPeriodsLength());
            }
            lastTimeAdded = lastTimeAdded
                    .plusMinutes(establishment.getPeriodLength())
                    .plusMinutes(establishment.getBetweenPeriodsLength());
            if (lastTimeAdded
                    .plusMinutes(establishment.getPeriodLength())
                    .plusMinutes(establishment.getBetweenPeriodsLength())
                    .isAfter(LocalTime.parse(establishment.getCloseTime(), TIME_FORMATTER))) {
                isLastTime = true;
                continue;
            }
            possibleStartTimes.add(lastTimeAdded.format(TIME_FORMATTER));
            currentPeriod++;
        }
        return possibleStartTimes;
    }

    private List<String> validateNonConflicting(ProgramTeachersSchedule schedule, Establishment establishment) {
        List<String> corrections = new ArrayList<>();
        for (TeachersSchedule teacherSchedule : schedule.getTeachersSchedules()) {
            Map<String, List<CourseScheduleBlock>> coursesPerDay = new HashMap<>();
            for (CourseScheduleBlock courseScheduleBlock : teacherSchedule.getScheduleBlocks()) {
                addToOrInitList(coursesPerDay, courseScheduleBlock.getDay(), courseScheduleBlock);
            }
            for (String day : coursesPerDay.keySet()) {
                List<CourseScheduleBlock> coursesOnDay = coursesPerDay.get(day);
                for (int i = 0; i < coursesOnDay.size() - 1; i++) {
                    CourseScheduleBlock course1 = coursesOnDay.get(i);
                    CourseScheduleBlock course2 = coursesOnDay.get(i + 1);
                    if (LocalTime.parse(course1.getStartTime(), TIME_FORMATTER)
                            .plusMinutes(establishment.getPeriodLength() * course1.getPeriods())
                            .isAfter(LocalTime.parse(course2.getStartTime(), TIME_FORMATTER))) {
                        corrections.add(
                                "Teacher " + teacherSchedule.getTeacherId() + " has a course " + course1.getCourseId() +
                                        " that starts at " + course1.getStartTime() + " and lasts " +
                                        course1.getPeriods() + " periods of " + establishment.getPeriodLength()
                                        + " minutes while " + course2.getCourseId() +
                                        " is still ongoing on day " + day +
                                        ". Make sure to adjust the periods so that courses do not overlap and if necessary add more or less schedule blocks.");
                    }
                }
            }
        }
        return corrections;
    }

    private <T> void addToOrInitList(Map<String, List<T>> map, String key, T value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            List<T> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        }
    }

    private void addToOrIncrement(Map<String, Long> map, String key, long value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + value);
        } else {
            map.put(key, value);
        }
    }

    private String removeAccents(String stringifiedData) {
        stringifiedData = stringifiedData.replaceAll("é", "e");
        stringifiedData = stringifiedData.replaceAll("è", "e");
        stringifiedData = stringifiedData.replaceAll("ê", "e");
        stringifiedData = stringifiedData.replaceAll("à", "a");
        stringifiedData = stringifiedData.replaceAll("â", "a");
        stringifiedData = stringifiedData.replaceAll("î", "i");
        stringifiedData = stringifiedData.replaceAll("ï", "i");
        stringifiedData = stringifiedData.replaceAll("ô", "o");
        stringifiedData = stringifiedData.replaceAll("ö", "o");
        stringifiedData = stringifiedData.replaceAll("ù", "u");
        stringifiedData = stringifiedData.replaceAll("û", "u");
        stringifiedData = stringifiedData.replaceAll("ü", "u");
        stringifiedData = stringifiedData.replaceAll("ç", "c");
        return stringifiedData;
    }

    private void correctSchedule(ProgramTeachersSchedule schedule,
                                 List<String> corrections,
                                 TeacherScheduleGenerationInfoDTO scheduleGenerationInfo,
                                 int correctionDepth) throws ChatGPTException, ScheduleGenerationException {
        if (correctionDepth > MAX_CORRECTION_DEPTH) {
            throw new ScheduleGenerationException();
        }
        try {
            String prompt = "Modify the following json file:" + mapper.writeValueAsString(schedule) +
                    ", while making sure to keep the same format and correct the following mistakes:" +
                    String.join(",", corrections) + ". Make sure it is delimited by a " + JSON_DELIMITER +
                    "at the extremities.";
            ChatGPTResponse response = chatGPTService.chatGPT(chatGPTService.generateMessages(prompt));
            processResponse(response, scheduleGenerationInfo, correctionDepth);
        } catch (JsonProcessingException e) {
            throw new ChatGPTException();
        }
    }

    private void processResponse(ChatGPTResponse response, TeacherScheduleGenerationInfoDTO scheduleGenerationInfo, int correctionCount) throws ScheduleGenerationException {
        try {
            String responseString = response.getChoices().get(0).getMessage().getContent();
            String[] split = responseString.split(JSON_DELIMITER);
            String json = "";
            if (split.length < 2 && split[0].startsWith("{")) {
                json = split[0].replaceAll("\n", "").replaceAll(
                        " ", "");
            } else if (split.length < 2) {
                throw new ScheduleGenerationException();
            } else {
                json = split[1].replaceAll("\n", "").replaceAll(
                        " ", "");
            }
            try {
                ProgramTeachersSchedule schedule = mapper.readValue(json, ProgramTeachersSchedule.class);
                List<String> corrections = validateSchedule(schedule, scheduleGenerationInfo);
                if (corrections.isEmpty()) {
                    return;
                }
                correctSchedule(schedule, corrections, scheduleGenerationInfo, correctionCount + 1);
            } catch (JsonMappingException e) {
                System.out.println("Invalid json: " + responseString);
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ScheduleGenerationException();
        }
    }

    private void processResponse(ChatGPTResponse response, TeacherScheduleGenerationInfoDTO scheduleGenerationInfo) throws ScheduleGenerationException {
        processResponse(response, scheduleGenerationInfo, 0);
    }

    private ChatGPTResponse generateTeachersSchedule(TeacherScheduleGenerationInfoDTO teacherScheduleGenerationInfoDTO) throws ChatGPTException, ScheduleGenerationException {
        try {
            ProgramTeachersSchedule scheduleFormat = ProgramTeachersSchedule.generateScheduleFormat();
            String scheduleFormatStringified = mapper.writeValueAsString(scheduleFormat);
            String promptHeader = "Generate a JSON, delimited with " + JSON_DELIMITER + " at the start and end of the json of this format :" + scheduleFormatStringified + "with the following informations:";
            String promptFooter = "with periodLength,dinnerLength and betweenPeriodsLength being in minutes, with the first period starting at classesStartTime, with a pause between each periods of betweenPeriodsLength minutes, with a pause of dinnerLength minutes after periodsBeforeDinner periods. There must be at most periodsPerDay periods in a day. the hoursPerWeek of each class must be respected in the schedule. the hoursPerWeek need to be separated in blocks of periodLength length. The schedule must be valid, meaning that no teacher can teach two classes at the same time. You can ignore fields with the null value. if they are too complex, you can ignore a few restrictions so long as you return a json in the right format.";
            String stringifiedData = mapper.writeValueAsString(teacherScheduleGenerationInfoDTO);
            List<ChatGPTMessage> chatGPTMessages = createMessages(promptHeader, stringifiedData, promptFooter);
            return chatGPTService.chatGPT(chatGPTMessages);
        } catch (JsonProcessingException e) {
            throw new ChatGPTException();
        }
    }

    private List<ChatGPTMessage> createMessages(String promptHeader, String stringifiedData, String promptFooter) {
        return chatGPTService.generateMessages(promptHeader + stringifiedData + promptFooter);
    }
}