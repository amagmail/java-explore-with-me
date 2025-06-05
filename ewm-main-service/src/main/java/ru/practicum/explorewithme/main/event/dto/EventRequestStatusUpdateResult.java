package ru.practicum.explorewithme.main.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.request.dto.RequestDto;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    private List<RequestDto>  confirmedRequests;
    private List<RequestDto> rejectedRequests;
}