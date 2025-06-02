package ru.practicum.explorewithme.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.main.event.dto.EventFullDto;
import ru.practicum.explorewithme.main.event.enums.Sorting;
import ru.practicum.explorewithme.main.event.service.EventService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class EventControllerPublic {

    private final EventService eventService;
    private RestTemplate restTemplate;

    @GetMapping
    public Collection<EventFullDto> getEventsPublic(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Long> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(defaultValue = "EVENT_DATE") Sorting sort,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        log.info("PUBLIC: Получен запрос на поиск событий с возможностью фильтрации");
        if (text != null) {
            text = text.equals("0") ? null : text.toLowerCase();
        }
        if (categories != null && categories.size() == 1 && categories.getFirst() == 0) {
            categories = null;
        }
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventPublic(@PathVariable("eventId") Long eventId) {

        /*
        String url = "https://external-api/users";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        StatisticRequest request = new StatisticRequest("ewm-main-service", "/events/4", "192.163.0.1");
        HttpEntity<StatisticRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<StatisticRequest> response = restTemplate.postForEntity(url, entity, StatisticRequest.class); */

        log.info("Получен запрос на поиск подробной информации об опубликованном событии: eventId = {}", eventId);
        return eventService.getEventPublic(eventId);
    }

}
