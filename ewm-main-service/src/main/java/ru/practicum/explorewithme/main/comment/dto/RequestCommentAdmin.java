package ru.practicum.explorewithme.main.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCommentAdmin {

    private Long id;
    private Boolean accepted;

    @Size(min = 5, message = "Минимальная длина поля 5 символов")
    @Size(max = 2000, message = "Максимальная длина поля 2000 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    private String message;

}
