package ru.practicum.explorewithme.main.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventNodeDto {

    private Long id;
    private String title;

}
