package ru.practicum.explorewithme.main.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.comment.dto.RequestCommentUser;
import ru.practicum.explorewithme.main.comment.dto.ResponseComment;
import ru.practicum.explorewithme.main.comment.dto.ResponseCommentFull;
import ru.practicum.explorewithme.main.comment.dto.ResponseEventCommentsFull;
import ru.practicum.explorewithme.main.comment.service.CommentService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseComment createCommentPrivate(@PathVariable("userId") Long userId,
                                                @PathVariable("eventId") Long eventId,
                                                @Valid @RequestBody RequestCommentUser dto) {
        log.info("PRIVATE: Получен запрос на создание комментария по событию: userId={}, eventId={}, dto={}", userId, eventId, dto);
        return commentService.createCommentPrivate(userId, eventId, dto);
    }

    @PatchMapping("/{commentId}")
    public ResponseComment updateCommentPrivate(@PathVariable("userId") Long userId,
                                                @PathVariable("eventId") Long eventId,
                                                @PathVariable("commentId") Long commentId,
                                                @Valid @RequestBody RequestCommentUser dto) {
        log.info("PRIVATE: Получен запрос на редактирование комментария по событию: userId={}, eventId={}, commentId={}, dto={}", userId, eventId, commentId, dto);
        return commentService.updateCommentPrivate(userId, eventId, commentId, dto);
    }

    @GetMapping("/{commentId}")
    public ResponseComment getCommentPrivate(@PathVariable("userId") Long userId,
                                             @PathVariable("eventId") Long eventId,
                                             @PathVariable("commentId") Long commentId) {
        log.info("PRIVATE: Получен запрос на извлечение пользовательского комментария с идентификатором: userId={}, eventId={}, commentId={}", userId, eventId, commentId);
        return commentService.getCommentPrivate(userId, eventId, commentId);
    }

    @GetMapping()
    public ResponseEventCommentsFull getCommentsPrivate(@PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("PRIVATE: Получен запрос на извлечение пользовательских комментариев с идентификатором: userId={}, eventId={}", userId, eventId);
        return commentService.getCommentsPrivate(userId, eventId, from, size);
    }

}
