package ru.practicum.explorewithme.main.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByEventAndRequester(Long eventId, Long userId);

    //TODO: добавить фильтр на не свои события
    List<Request> findAllByRequester(Long id);
}
