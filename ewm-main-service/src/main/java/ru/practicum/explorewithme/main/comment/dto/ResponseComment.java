package ru.practicum.explorewithme.main.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.event.dto.EventNodeDto;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseComment {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifyDate;
    private EventNodeDto event;
    private UserShortDto user;
    private String userMessage;
    private String adminMessage;
    private Boolean accepted;

}
