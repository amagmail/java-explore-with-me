package ru.practicum.explorewithme.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.annotation.MinEventDate2h;
import ru.practicum.explorewithme.main.event.enums.StateAction;
import ru.practicum.explorewithme.main.event.model.Location;

@Data
@Builder
public class UpdateEventUserRequest {

    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    private String annotation;

    @Size(max = 7000, message = "Максимальная длина поля 7000 символов")
    private String description;

    @Size(max = 120, message = "Максимальная длина поля 120 символов")
    private String title;

    //@MinEventDate2h
    private String eventDate;

    private Long category;
    private Boolean paid;
    private Boolean requestModeration;
    private StateAction stateAction;

    @PositiveOrZero
    private Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Location location;

}
