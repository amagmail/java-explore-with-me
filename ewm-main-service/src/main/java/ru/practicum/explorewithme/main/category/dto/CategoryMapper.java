package ru.practicum.explorewithme.main.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public CategoryDto fromCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}
