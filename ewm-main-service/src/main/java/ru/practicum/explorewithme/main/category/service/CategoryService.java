package ru.practicum.explorewithme.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.category.dal.CategoryRepository;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.category.dto.CategoryMapper;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("Категория с таким названием уже существует");
        }
        return CategoryMapper.fromCategory(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        if (!category.getName().equals(categoryDto.getName()) && categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с таким названием уже существует");
        }

        category.setName(categoryDto.getName());

        return CategoryMapper.fromCategory(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long catId) {
        Category oldCategory = categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена"));

        //TODO: добавить проверку на событие

        categoryRepository.deleteById(oldCategory.getId());
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        Page<Category> pagedResult = categoryRepository.findAll(pageable);
        return pagedResult.stream().map(CategoryMapper::fromCategory).toList();
    }

    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Категория не найдена")));
    }
}
