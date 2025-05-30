package ru.practicum.explorewithme.main.event.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator = ?1 order by e.eventDate DESC")
    List<Event> getUserEvents(Long userId);

    @Query("select e from Event e where e.initiator = ?1 and e.id = ?2")
    Event getUserEvent(Long userId, Long eventId);

}
