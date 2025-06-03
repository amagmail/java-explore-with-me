package ru.practicum.explorewithme.main.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.compilation.model.Compilation;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public CompilationDto fromCompilation(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        dto.setEvents(compilation.getEvents().stream().toList());
        return dto;
    }

    public Compilation toCompilation(CompilationReqDto dto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
        compilation.setEvents(events);
        return compilation;
    }

    public Compilation toCompilation(UpdateCompilationRequest dto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
        compilation.setEvents(events);
        return compilation;
    }
}
