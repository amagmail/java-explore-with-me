package ru.practicum.explorewithme.main.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.compilation.model.Compilation;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public CompilationDto fromCompilation(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream().toList())
                .build();
    }

    public Compilation toCompilation(CompilationReqDto dto, Set<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .events(events)
                .build();
    }
}
