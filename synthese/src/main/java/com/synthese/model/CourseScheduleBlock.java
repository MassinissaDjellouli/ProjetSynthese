package com.synthese.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseScheduleBlock {
    private String day;
    private String startTime;
    private int periods;
    private String courseId;

    public static List<CourseScheduleBlock> generateScheduleFormat() {
        return List.of(
                CourseScheduleBlock.builder()
                        .day("day")
                        .startTime("startTime")
                        .periods(1)
                        .courseId("courseId")
                        .build(),
                CourseScheduleBlock.builder()
                        .day("day")
                        .startTime("startTime")
                        .periods(1)
                        .courseId("courseId")
                        .build(),
                CourseScheduleBlock.builder()
                        .day("day")
                        .startTime("startTime")
                        .periods(1)
                        .courseId("courseId")
                        .build(),
                CourseScheduleBlock.builder()
                        .day("day")
                        .startTime("startTime")
                        .periods(1)
                        .courseId("courseId")
                        .build()
        );
    }
}