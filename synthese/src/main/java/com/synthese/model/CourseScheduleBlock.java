package com.synthese.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseScheduleBlock {
    private String day;
    private String startTime;
    private int periods;
    private String courseId;
}