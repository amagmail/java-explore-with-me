package ru.practicum.explorewithme.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
