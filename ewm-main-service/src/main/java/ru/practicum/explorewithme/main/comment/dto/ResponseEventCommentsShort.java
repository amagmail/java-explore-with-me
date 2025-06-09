package ru.practicum.explorewithme.main.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.event.dto.EventNodeDto;

import java.util.List;

@Data
@Builder
public class ResponseEventCommentsShort {

    private EventNodeDto event;
    private List<ResponseCommentShort> comments;

}
