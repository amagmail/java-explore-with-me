package ru.practicum.explorewithme.main.event.dto;

import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.event.enums.StateAction;

import java.time.LocalDateTime;

public class UpdateEventUserRequest {

    private String annotation;
    private CategoryDto category;
    private String description;
    private LocalDateTime eventDate;
    private Long location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
