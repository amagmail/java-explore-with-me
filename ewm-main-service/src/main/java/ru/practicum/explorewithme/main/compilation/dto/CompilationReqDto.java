package ru.practicum.explorewithme.main.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@Builder
public class CompilationReqDto {

    @Size(min = 1, message = "Минимальная длина поля 1 символов")
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    @NotBlank
    @NotEmpty
    @NotNull
    private String title;

    private Boolean pinned = false;
    private Set<Long> events;
}
