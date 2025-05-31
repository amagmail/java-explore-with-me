package ru.practicum.explorewithme.main.event.dal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator = ?1 order by e.eventDate DESC")
    List<Event> getUserEvents(Long userId);

    @Query("select e from Event e where e.initiator = ?1 and e.id = ?2")
    Event getUserEvent(Long userId, Long eventId);

    @Query("select e from Event e " +
            "where (?1 is null or e.initiator in ?1) " +
            "and (?2 is null or e.category in ?2) " +
            "and (?3 is null or e.state in ?3) ")
    List<Event> findByAdminFilters(List<Long> users, List<Long> categories, List<String> states, Pageable pageable);

    //List<Event> findByInitiatorIn(List<Long> users);
    //Page<Event> findAll(Pageable pageable);

}
