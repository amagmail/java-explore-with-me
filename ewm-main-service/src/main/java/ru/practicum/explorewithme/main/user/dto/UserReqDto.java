package ru.practicum.explorewithme.main.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReqDto {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String name;

    @NotEmpty(message = "Email пользователя не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;

}
