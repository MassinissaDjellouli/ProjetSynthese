package com.synthese.model;

import com.synthese.dto.EstablishmentDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "establishments")
public class Establishment {
    @Id
    private ObjectId id;
    @NotEmpty
    private List<ObjectId> administrators;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
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
    @Min(1)
    private long periodsBeforeDinner;
    @Min(1)
    private long dinnerLength;
    @Min(1)
    private long betweenPeriodsLength;

    public EstablishmentDTO toDTO() {
        return EstablishmentDTO.builder()
                .id(id.toString())
                .address(address)
                .phone(phone)
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