package ru.practicum.explorewithme.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.event.enums.State;
import ru.practicum.explorewithme.main.event.model.Location;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {

    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;
    private Integer views;
    private Integer confirmedRequests;
    private String description;
    private Integer participantLimit;
    private State state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Location location;

    private Boolean requestModeration;
}
