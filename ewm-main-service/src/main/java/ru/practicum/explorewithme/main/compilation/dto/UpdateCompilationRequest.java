package ru.practicum.explorewithme.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
public class UpdateCompilationRequest {

    @Size(min = 1, message = "Минимальная длина поля 1 символов")
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    private String title;

    private Boolean pinned;
    private Set<Long> events;
}
