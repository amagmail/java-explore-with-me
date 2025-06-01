package ru.practicum.explorewithme.main.event.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator = ?1 order by e.eventDate DESC")
    List<Event> getEventsPrivate(Long userId);

    @Query("select e from Event e where e.initiator = ?1 and e.id = ?2")
    Event getEventPrivate(Long userId, Long eventId);

    @Query("select e from Event e " +
            "where (?1 is null or e.initiator in ?1) " +
            "and (?2 is null or e.category in ?2) " +
            "and (?3 is null or e.state in ?3) " +
            "and (cast(?4 as date) is null or e.eventDate >= ?4) " +
            "and (cast(?5 as date) is null or e.eventDate <= ?5)")
    List<Event> getEventsAdmin(List<Long> users, List<Long> categories, List<String> states, LocalDateTime dateFrom, LocalDateTime dateTo, Pageable pageable);

    //List<Event> findByInitiatorIn(List<Long> users);
    //Page<Event> findAll(Pageable pageable);

    @Query("select e from Event e where e.id = ?1 and e.publishedOn is not null")
    Event getEventPublic(Long eventId);

}
