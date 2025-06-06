package ru.practicum.explorewithme.main.user.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqDto {

    @Size(min = 2, message = "Минимальная длина поля 3 символов")
    @Size(max = 250, message = "Максимальная длина поля 254 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    private String name;

    @Size(min = 6, message = "Минимальная длина поля 3 символов")
    @Size(max = 254, message = "Максимальная длина поля 254 символов")
    @NotNull(message = "Поле не может быть неопределенным")
    @NotEmpty(message = "Поле не может быть пустым")
    @NotBlank(message = "Поле не может состоять из пробелов")
    @Email(message = "Некорректный email")
    private String email;

}
