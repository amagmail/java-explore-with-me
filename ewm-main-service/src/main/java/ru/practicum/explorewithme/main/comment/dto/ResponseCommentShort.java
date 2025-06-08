package ru.practicum.explorewithme.main.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCommentShort {

    private Long id;
    private LocalDateTime createdDate;
    private UserShortDto user;
    private String message;

}
