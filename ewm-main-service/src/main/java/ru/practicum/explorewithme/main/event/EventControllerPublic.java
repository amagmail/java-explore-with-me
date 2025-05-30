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
@RequestMapping("/events")
public class EventControllerPublic {

    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getEvents() {
        log.info("Получен запрос на извлечение событий с возможностью фильтрации");
        return eventService.getEvents();
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") Long eventId) {
        log.info("Получен запрос на извлечение подробной информации об опубликованном событии: eventId = {}", eventId);
        return eventService.getEvent(eventId);
    }

}
