package ru.practicum.explorewithme.main.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.comment.dto.*;
import ru.practicum.explorewithme.main.comment.service.CommentService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/comments")
public class CommentControllerAdmin {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseEventComment updateCommentAdmin(@PathVariable(value = "commentId") Long commentId,
                                                   @Valid @RequestBody RequestCommentAdmin dto) {
        log.info("ADMIN: Получен запрос на редактирование пользовательского комментария: commentId={}", commentId);
        return commentService.updateCommentAdmin(commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("ADMIN: Получен запрос на удаление пользовательского комментария: commentId={}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

    @GetMapping("/{commentId}")
    public ResponseEventComment getCommentAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("ADMIN: Получен запрос на выборку пользовательского комментария: commentId={}", commentId);
        return commentService.getCommentAdmin(commentId);
    }

    @GetMapping()
    public Collection<ResponseEventComment> getCommentAdmin(@RequestParam(defaultValue = "0") Integer from,
                                                            @RequestParam(defaultValue = "10") Integer size) {
        log.info("ADMIN: Получен запрос на выборку всех неопубликованных пользовательских комментариев");
        return commentService.getCommentsAdmin(from, size);
    }

}
