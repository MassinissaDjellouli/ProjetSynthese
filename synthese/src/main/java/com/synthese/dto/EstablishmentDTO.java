package com.synthese.dto;

import com.synthese.model.Establishment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstablishmentDTO {

    private String id;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @NotNull
    private List<Long> programs;
    @NotNull
    private List<Long> teachers;
    @NotNull
    private List<Long> students;
    @Min(1)
    private long periodLength;
    @NotBlank
    private String name;
    @Min(1)
    private long periodsPerDay;
    @NotEmpty
    private List<String> daysPerWeek;
    @NotBlank
    private String openTime;
    @NotBlank
    private String closeTime;
    @NotBlank
    private String classesStartTime;
    @NotBlank
    private String periodsBeforeDinner;
    @NotBlank
    private String dinnerLength;
    @NotBlank
    private String betweenPeriodsLength;

    public Establishment toModel() {
        return Establishment.builder()
                .id(new ObjectId(id))
                .address(address)
                .phone(phone)
                .programs(programs)
                .teachers(teachers)
                .students(students)
                .periodLength(periodLength)
                .name(name)
                .periodsPerDay(periodsPerDay)
                .daysPerWeek(daysPerWeek)
                .openTime(openTime)
                .closeTime(closeTime)
                .classesStartTime(classesStartTime)
                .periodsBeforeDinner(periodsBeforeDinner)
                .dinnerLength(dinnerLength)
                .betweenPeriodsLength(betweenPeriodsLength)
                .build();
    }

}