package ru.practicum.explorewithme.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatDto {
    private String app;
    private String uri;
    private Long hits;
}
