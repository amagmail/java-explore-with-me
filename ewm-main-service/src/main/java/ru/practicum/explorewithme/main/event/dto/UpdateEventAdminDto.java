package ru.practicum.explorewithme.main.event.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.annotation.MinEventDate1h;
import ru.practicum.explorewithme.main.event.enums.StateAction;

@Data
@Builder
public class UpdateEventAdminDto {

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    private String annotation;

    @Size(min = 20, message = "Минимальная длина поля 20 символов")
    @Size(max = 7000, message = "Максимальная длина поля 7000 символов")
    private String description;

    @Size(min = 3, message = "Минимальная длина поля 3 символов")
    @Size(max = 1200, message = "Максимальная длина поля 120 символов")
    private String title;

    @MinEventDate1h
    private String eventDate;

    private Long category;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    //private Long location;

}
