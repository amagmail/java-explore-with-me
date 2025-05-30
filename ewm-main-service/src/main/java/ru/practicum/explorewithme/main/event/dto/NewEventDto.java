package ru.practicum.explorewithme.main.event.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.annotation.MinEventDate;

@Data
@Builder
public class NewEventDto {

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    @NotNull(message = "Поле не может быть NULL")
    private String annotation;

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 7000, message = "Максимальная длина поля 7000 символов")
    @NotNull(message = "Поле не может быть пустым")
    private String description;

    @Size(min = 3, message = "Минимальная длина поля 3 символов")
    @Size(max = 1200, message = "Максимальная длина поля 120 символов")
    @NotNull(message = "Поле не может быть NULL")
    private String title;

    @MinEventDate
    @NotNull(message = "Поле не может быть пустым")
    private String eventDate;

    @NotNull(message = "Поле не может быть пустым")
    private Long category;

    private Boolean paid; // = false

    private Integer participantLimit; // = 0

    private Boolean requestModeration; // = false

    //private Long location;

}
