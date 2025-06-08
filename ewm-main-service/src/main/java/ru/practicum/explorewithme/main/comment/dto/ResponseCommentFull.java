package ru.practicum.explorewithme.main.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseCommentFull {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifyDate;
    private UserShortDto user;
    private String userMessage;
    private String adminMessage;
    private Boolean accepted;

}
