package com.synthese.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Document(collection = "teachers_schedule")
public class TeachersSchedule {
    private String id;
    private String teacherId;
    private String establishmentId;
    List<CourseScheduleBlock> scheduleBlocks;

    public static List<TeachersSchedule> generateScheduleFormat() {
        return List.of(
                TeachersSchedule.builder()
                        .teacherId("teacherId")
                        .establishmentId("establishmentId")
                        .scheduleBlocks(CourseScheduleBlock.generateScheduleFormat())
                        .build(),
                TeachersSchedule.builder()
                        .teacherId("teacherId")
                        .establishmentId("establishmentId")
                        .scheduleBlocks(CourseScheduleBlock.generateScheduleFormat())
                        .build(),
                TeachersSchedule.builder()
                        .teacherId("teacherId")
                        .establishmentId("establishmentId")
                        .scheduleBlocks(CourseScheduleBlock.generateScheduleFormat())
                        .build(),
                TeachersSchedule.builder()
                        .teacherId("teacherId")
                        .establishmentId("establishmentId")
                        .scheduleBlocks(CourseScheduleBlock.generateScheduleFormat())
                        .build()
        );
    }
}