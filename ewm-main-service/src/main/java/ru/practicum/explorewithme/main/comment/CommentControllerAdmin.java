package ru.practicum.explorewithme.main.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.comment.dto.ResponseComment;
import ru.practicum.explorewithme.main.comment.dto.ResponseCommentFull;
import ru.practicum.explorewithme.main.comment.dto.ResponseEventCommentsFull;
import ru.practicum.explorewithme.main.comment.dto.ResponseEventCommentsShort;
import ru.practicum.explorewithme.main.comment.service.CommentService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/comments")
public class CommentControllerAdmin {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseComment updateCommentAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("ADMIN: Получен запрос на редактирование пользовательского комментария с идентификатором {}", commentId);
        return commentService.updateCommentAdmin(commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("ADMIN: Получен запрос на удаление пользовательского комментария с идентификатором {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

    @GetMapping("/{commentId}")
    public ResponseComment getCommentAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("ADMIN: Получен запрос на извлечение пользовательского комментария с идентификатором {}", commentId);
        return commentService.getCommentAdmin(commentId);
    }

    @GetMapping()
    public ResponseEventCommentsFull getCommentsAdmin(@RequestParam(required = false) List<Long> users,
                                                      @RequestParam(required = false) List<Long> events,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("ADMIN: Получен запрос на поиск комментариев: users={}, events={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, events, categories, rangeStart, rangeEnd, from, size);
        if (users != null && users.size() == 1 && users.getFirst() == 0) {
            users = null;
        }
        if (events != null && events.size() == 1 && events.getFirst() == 0) {
            events = null;
        }
        if (categories != null && categories.size() == 1 && categories.getFirst() == 0) {
            categories = null;
        }
        return commentService.getCommentsAdmin(users, categories, events, rangeStart, rangeEnd, from, size);
    }

}
