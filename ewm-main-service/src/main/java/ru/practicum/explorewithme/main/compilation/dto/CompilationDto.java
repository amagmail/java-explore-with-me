package ru.practicum.explorewithme.main.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompilationDto {

    private Long id;
    private Boolean pinned;

    @Size(min = 1, message = "Минимальная длина поля 1 символов")
    @Size(max = 50, message = "Максимальная длина поля 50 символов")
    private String title;
}
