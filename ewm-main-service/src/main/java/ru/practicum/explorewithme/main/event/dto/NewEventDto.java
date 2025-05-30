package ru.practicum.explorewithme.main.event.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.annotation.MinEventDate;

@Data
@Builder
public class NewEventDto {

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    private String annotation;

    @NotEmpty(message = "Поле не может быть пустым")
    private Long category;

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 7000, message = "Максимальная длина поля 7000 символов")
    private String description;

    @MinEventDate
    private String eventDate;

    //private Long location;

    private Boolean paid; // = false

    private Integer participantLimit; // = 0

    private Boolean requestModeration; // = false

    @Size(min = 3, message = "Минимальная длина поля 3 символов")
    @Size(max = 1200, message = "Максимальная длина поля 120 символов")
    private String title;
}
