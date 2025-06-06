package ru.practicum.explorewithme.main.event.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.request.enums.RequestState;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestState status;
}
