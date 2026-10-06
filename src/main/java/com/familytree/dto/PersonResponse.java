package com.familytree.dto;

import com.familytree.model.Person;

import java.time.LocalDate;

public record PersonResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        LocalDate deathDate,
        Long fatherId,
        Long motherId) {

    public static PersonResponse from(Person p) {
        return new PersonResponse(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getBirthDate(),
                p.getDeathDate(),
                p.getFather() == null ? null : p.getFather().getId(),
                p.getMother() == null ? null : p.getMother().getId());
    }
}
