package ru.practicum.explorewithme.main.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.dto.*;
import ru.practicum.explorewithme.main.event.service.EventService;
import ru.practicum.explorewithme.main.request.dto.RequestDto;

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
        log.info("PRIVATE: Получен запрос на поиск событий: userId={}", userId);
        return eventService.getEventsPrivate(userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventPrivate(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId) {
        log.info("PRIVATE: Получен запрос на поиск события: userId={}, userId={}", userId, eventId);
        return eventService.getEventPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventPrivate(@PathVariable("userId") Long userId,
                                           @PathVariable("eventId") Long eventId,
                                           @Valid @RequestBody UpdateEventUserRequest dto) {
        log.info("PRIVATE: Получен запрос на изменение события: userId={}, eventId={}, dto={}", userId, eventId, dto);
        return eventService.updateEventPrivate(userId, eventId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getEventRequestsPrivate(@PathVariable("userId") Long userId,
                                              @PathVariable("eventId") Long eventId) {
        log.info("PRIVATE: Получен запрос на поиск информации о запросах на участие в событии: userId={}, userId={}", userId, eventId);
        return eventService.getEventRequestsPrivate(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestsPrivate(@PathVariable("userId") Long userId,
                                                                     @PathVariable("eventId") Long eventId,
                                                                     @Valid @RequestBody EventRequestStatusUpdateResult dto) {
        log.info("PRIVATE: Получен запрос на изменение заявок на участие в событии: userId={}, eventId={}, dto={}", userId, eventId, dto);
        return eventService.updateEventRequestsPrivate(userId, eventId, dto);
    }

}
