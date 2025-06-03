package ru.practicum.explorewithme.main.compilation.dto;

import lombok.*;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;
    private Boolean pinned;
    private String title;
    private List<Event> events;
}
