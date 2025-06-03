package ru.practicum.explorewithme.main.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.request.enums.RequestState;
import ru.practicum.explorewithme.main.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByEventAndRequester(Long eventId, Long userId);

    List<Request> findAllByRequester(Long id);

    List<Request> findByIdIn(List<Long> ids);

    List<Request> findByEventAndStatus(Long eventId, RequestState status);

    List<Request> findByEvent(Long eventId);

    int countByEventAndStatus(Long eventId, RequestState status);

    boolean existsByEventAndRequester(Long eventId, Long requesterId);
}
