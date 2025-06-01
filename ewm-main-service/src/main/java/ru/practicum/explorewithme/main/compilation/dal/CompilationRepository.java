package ru.practicum.explorewithme.main.compilation.dal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.compilation.model.Compilation;

import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Optional<Compilation> findByTitle(String title);

    Page<Compilation> findByPinned(boolean pinned, Pageable pageable);

}
