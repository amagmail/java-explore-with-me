package ru.practicum.explorewithme.main.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.event.dto.UpdateEventAdminDto;
import ru.practicum.explorewithme.main.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.main.event.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping("/events")
    public Collection<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("ADMIN: Получен запрос на поиск событий: users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsAdmin(users, categories, states, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventPrivate(@PathVariable("eventId") Long eventId,
                                           @Valid @RequestBody UpdateEventAdminDto dto) {
        log.info("ADMIN: Получен запрос на изменение события: eventId={}, dto={}", eventId, dto);
        return eventService.updateEventAdmin(eventId, dto);
    }

}
