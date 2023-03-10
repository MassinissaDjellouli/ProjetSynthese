package com.synthese.service;

import com.synthese.dto.*;
import com.synthese.enums.Roles;
import com.synthese.exceptions.AlreadyExistingCourseException;
import com.synthese.exceptions.ManagerNotFoundException;
import com.synthese.exceptions.UserNotFoundException;
import com.synthese.exceptions.WrongPasswordException;
import com.synthese.model.*;
import com.synthese.repository.*;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class ManagerService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final TeacherRepository teacherRepository;
    private final ProgramRepository programRepository;
    private final CourseRepository courseRepository;

    public List<ManagerDTO> login(LoginDTO loginDTO) throws UserNotFoundException, WrongPasswordException, ManagerNotFoundException {
        Optional<User> user = userRepository.findByUsernameAndRole(loginDTO.getUsername(), Roles.MANAGER);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (!user.get().getPassword().equals(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }
        List<Manager> managerList = managerRepository.findByUserId(user.get().getId());
        if (managerList.isEmpty()) {
            throw new ManagerNotFoundException();
        }
        return managerList.stream().map(manager ->
                ManagerDTO.builder()
                        .id(user.get().getId().toString())
                        .firstName(manager.getFirstName())
                        .lastName(manager.getLastName())
                        .username(user.get().getUsername())
                        .establishmentId(manager.getEstablishment().toString())
                        .build()
        ).toList();
    }

    public List<CourseDTO> getCourses(String programId) {
        return courseRepository.findByProgram(new ObjectId(programId))
                .stream().map(Course::toDTO).toList();
    }

    public List<ProgramDTO> getPrograms(String establishmentId) {
        return programRepository.findByEstablishment(new ObjectId(establishmentId))
                .stream().map(Program::toDTO).toList();
    }

    public List<TeacherDTO> getTeachers(String establishmentId) {
        return teacherRepository.findByEstablishment(new ObjectId(establishmentId))
                .stream()
                .map(teacher -> {
                    if (teacher.getCourses().isEmpty()) {
                        return teacher.toDTO(establishmentId);
                    }
                    return teacher.toDTO(establishmentId, courseRepository.findAllById(teacher.getCourses()));
                }).toList();
    }

    public void addCourseList(String programId, List<CourseCreationDTO> courseCreationDTOList) throws AlreadyExistingCourseException {
        try {
            Program program = programRepository.findById(new ObjectId(programId)).orElseThrow();
            courseCreationDTOList.forEach(courseCreationDTO -> {
                Course course = Course.builder()
                        .name(courseCreationDTO.getName())
                        .hoursPerWeek(courseCreationDTO.getHoursPerWeek())
                        .program(program.getId())
                        .build();
                courseRepository.save(course);
            });
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                throw new AlreadyExistingCourseException();
            }
            throw e;
        }
    }

    public void addTeacherToCourse(String id, List<ObjectId> teachers) {
        teachers.forEach(teacherId -> {
            Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
            if (teacherOpt.isEmpty()) {
                return;
            }
            Teacher teacher = teacherOpt.get();
            List<ObjectId> courses = teacher.getCourses();
            courses.add(new ObjectId(id));
            Set<String> uniqueCoursesSet = new HashSet<>(courses.stream().map(ObjectId::toString).toList());
            teacher.setCourses(uniqueCoursesSet.stream().map(ObjectId::new).toList());
            teacherRepository.save(teacher);
        });
    }
}