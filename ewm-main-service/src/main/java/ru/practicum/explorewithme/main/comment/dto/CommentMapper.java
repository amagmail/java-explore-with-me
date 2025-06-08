package ru.practicum.explorewithme.main.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.comment.model.Comment;
import ru.practicum.explorewithme.main.event.dto.EventNodeDto;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

@UtilityClass
public class CommentMapper {

    public static ResponseComment toResponseComment(Comment comment, UserShortDto user, EventNodeDto event) {
        return ResponseComment.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .modifyDate(comment.getModifyDate())
                .userMessage(comment.getUserMessage())
                .adminMessage(comment.getAdminMessage())
                .accepted(comment.getAccepted())
                .user(user)
                .event(event)
                .build();
    }

    public static ResponseCommentShort toResponseCommentShort(Comment comment, UserShortDto user) {
        return ResponseCommentShort.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .message(comment.getUserMessage())
                .user(user)
                .build();
    }

    public static ResponseCommentFull toResponseCommentFull(Comment comment, UserShortDto user) {
        return ResponseCommentFull.builder()
                .id(comment.getId())
                .createdDate(comment.getCreatedDate())
                .modifyDate(comment.getModifyDate())
                .userMessage(comment.getUserMessage())
                .adminMessage(comment.getAdminMessage())
                .accepted(comment.getAccepted())
                .user(user)
                .build();
    }

    public static Comment fromRequestCommentUser(RequestCommentUser entity) {
        return Comment.builder()
                .userMessage(entity.getMessage())
                .build();
    }

    public static Comment fromRequestCommentAdmin(RequestCommentAdmin entity) {
        return Comment.builder()
                .adminMessage(entity.getMessage())
                .accepted(entity.getAccepted())
                .build();
    }

}
