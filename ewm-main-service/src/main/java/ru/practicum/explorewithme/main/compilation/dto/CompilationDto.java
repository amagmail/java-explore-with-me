package ru.practicum.explorewithme.main.compilation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
}
