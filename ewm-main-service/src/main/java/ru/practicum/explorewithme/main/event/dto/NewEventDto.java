package ru.practicum.explorewithme.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.annotation.MinEventDate2h;
import ru.practicum.explorewithme.main.event.model.Location;

@Data
@Builder
public class NewEventDto {

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    private String annotation;

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 7000, message = "Максимальная длина поля 7000 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    private String description;

    @Size(min = 3, message = "Минимальная длина поля 3 символов")
    @Size(max = 120, message = "Максимальная длина поля 120 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    private String title;

    //@MinEventDate2h
    @NotNull(message = "Поле не может быть неопределенным")
    private String eventDate;

    @NotNull(message = "Поле не может быть неопределенным")
    private Long category;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Location location;

}
