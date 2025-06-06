package ru.practicum.explorewithme.main.category.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.category.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c order by c.id desc")
    List<Category> getCategories(Pageable pageable);

    Optional<Object> findByName(String name);

    Boolean existsByName(String name);
}
