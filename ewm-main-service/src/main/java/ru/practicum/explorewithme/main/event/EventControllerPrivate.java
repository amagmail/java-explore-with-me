package ru.practicum.explorewithme.main.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.dto.EventShortDto;
import ru.practicum.explorewithme.main.event.dto.NewEventDto;
import ru.practicum.explorewithme.main.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.main.event.service.EventService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventPrivate(@PathVariable("userId") Long userId,
                                           @Valid @RequestBody NewEventDto dto) {
        log.info("PRIVATE: Получен запрос на создание события: userId={}, dto={}", userId, dto);
        return eventService.createEventPrivate(userId, dto);
    }

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> getEventsPrivate(@PathVariable("userId") Long userId) {
        log.info("PRIVATE: Получен запрос на выборку событий: userId={}", userId);
        return eventService.getEventsPrivate(userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventPrivate(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId) {
        log.info("PRIVATE: Получен запрос на выборку события: userId={}, userId={}", userId, eventId);
        return eventService.getEventPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventPrivate(@PathVariable("userId") Long userId,
                                           @PathVariable("eventId") Long eventId,
                                           @Valid @RequestBody UpdateEventDto dto) {
        log.info("PRIVATE: Получен запрос на изменение события: userId={}, dto={}", userId, dto);
        return eventService.updateEventPrivate(userId, eventId, dto);
    }

}
