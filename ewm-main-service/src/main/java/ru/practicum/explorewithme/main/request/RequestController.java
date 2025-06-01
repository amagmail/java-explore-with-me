package ru.practicum.explorewithme.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.main.request.dto.RequestDto;
import ru.practicum.explorewithme.main.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@PathVariable("userId") Long userId, @RequestParam("eventId") Long eventId) {
        log.info("Получен запрос на создание запроса на участие в событии с идентификатором: {}", eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable("userId") Long userId, @PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на отмену запроса на участие в событии с идентификатором: {}", requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<RequestDto> getUserRequests(@PathVariable("userId") Long userId) {
        log.info("Получен запрос на получение списка запросов пользователя с идентификатором: {}", userId);
        return requestService.getRequestsByUser(userId);
    }
}
