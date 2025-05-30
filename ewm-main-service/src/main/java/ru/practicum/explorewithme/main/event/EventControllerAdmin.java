package ru.practicum.explorewithme.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.event.service.EventService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping("/events")
    public Collection<EventFullDto> getEvents() {
        log.info("Получен запрос на поиск событий");
        return eventService.getEvents();
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос на редактирование данных события и его статуса: eventId = {}", eventId);
        return null;
    }

}
