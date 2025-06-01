package ru.practicum.explorewithme.main.compilation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompilationReqDto {

    @NotEmpty(message = "Название подборки не может быть пустым")
    private String title;

    private Boolean pinned = false;
}
