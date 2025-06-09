package ru.practicum.explorewithme.main.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCommentAdmin {

    private Long id;
    private Boolean accepted;
    private String message;

}
