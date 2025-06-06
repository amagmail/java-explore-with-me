package ru.practicum.explorewithme.main.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на создание категории с данными: {}", categoryDto);
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос на обновление категории с данными: {}", categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Получен запрос на удаление категории с id={}", catId);
        categoryService.deleteCategory(catId);
    }
}
