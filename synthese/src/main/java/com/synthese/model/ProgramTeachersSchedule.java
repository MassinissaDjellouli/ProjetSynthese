package com.synthese.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramTeachersSchedule {
    private String programID;
    private List<TeachersSchedule> teachersSchedules;


    public static ProgramTeachersSchedule generateScheduleFormat() {
        return ProgramTeachersSchedule.builder()
                .programID("programID")
                .teachersSchedules(TeachersSchedule.generateScheduleFormat())
                .build();
    }
}