package ru.practicum.explorewithme.main.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.comment.dto.ResponseEventCommentsShort;
import ru.practicum.explorewithme.main.comment.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("events/{eventId}/comments")
public class CommentControllerPublic {

    private final CommentService commentService;

    @GetMapping()
    public ResponseEventCommentsShort getEventComments(@PathVariable("eventId") Long eventId,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("PUBLIC: Получен запрос на выборку всех опубликованных комментариев события: eventId={}", eventId);
        return commentService.getEventComments(eventId, from, size);
    }

}
