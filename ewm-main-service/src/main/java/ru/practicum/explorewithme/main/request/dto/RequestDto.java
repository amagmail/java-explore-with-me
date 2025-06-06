package ru.practicum.explorewithme.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.main.request.enums.RequestState;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestDto {

    private Long id;
    private Long requester;
    private Long event;
    private RequestState status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
}
