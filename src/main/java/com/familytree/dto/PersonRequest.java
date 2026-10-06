package com.familytree.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PersonRequest(
        @NotBlank String firstName,
        String lastName,
        LocalDate birthDate,
        LocalDate deathDate,
        Long fatherId,
        Long motherId) {
}
