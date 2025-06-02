package ru.practicum.explorewithme.main.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRespDto {

    private Long id;

    @Size(min = 2, message = "Минимальная длина поля 3 символов")
    @Size(max = 250, message = "Максимальная длина поля 254 символов")
    private String name;

    @Size(min = 6, message = "Минимальная длина поля 3 символов")
    @Size(max = 254, message = "Максимальная длина поля 254 символов")
    private String email;

}
