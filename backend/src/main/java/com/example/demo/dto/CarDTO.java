package com.example.demo.dto;

import com.example.demo.dto.validators.ValidYear;
import com.example.demo.model.TransmissionType;
import lombok.*;

import jakarta.validation.constraints.*;


@Data
@AllArgsConstructor
public class CarDTO {

    private Long id;

    @NotEmpty(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Transmission type is mandatory")
    private TransmissionType transmission;

    @NotNull(message = "Year of manufacture is mandatory")
    @ValidYear(years = 20, message = "Year of manufacture must be a within last 20 years")
    private Integer yearOfManufacture;

    @Min(value = 0, message = "Price per day must be greater than or equal to 0")
    @NotNull(message = "Price per day is mandatory")
    private Integer pricePerDay;

}
