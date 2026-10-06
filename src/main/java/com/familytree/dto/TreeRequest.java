package com.familytree.dto;

import jakarta.validation.constraints.NotBlank;

public record TreeRequest(@NotBlank String name) {
}
