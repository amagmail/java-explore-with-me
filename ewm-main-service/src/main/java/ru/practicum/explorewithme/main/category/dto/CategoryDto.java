package ru.practicum.explorewithme.main.category.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {

    private Long id;

    @NotEmpty(message = "Название категории не может быть пустым")
    private String name;
}
