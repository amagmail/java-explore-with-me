package ru.practicum.explorewithme.main.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.request.model.Request;

@UtilityClass
public class RequestMapper {
    public RequestDto fromRequest(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent())
                .requester(request.getRequester())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }
}
