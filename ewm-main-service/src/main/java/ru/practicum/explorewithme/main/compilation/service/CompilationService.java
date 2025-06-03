package ru.practicum.explorewithme.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.compilation.dal.CompilationRepository;
import ru.practicum.explorewithme.main.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.compilation.dto.CompilationMapper;
import ru.practicum.explorewithme.main.compilation.dto.CompilationReqDto;
import ru.practicum.explorewithme.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.main.compilation.model.Compilation;
import ru.practicum.explorewithme.main.event.dal.EventRepository;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto createCompilation(CompilationReqDto compilationDto) {
        if (!compilationRepository.findByTitleIgnoreCase(compilationDto.getTitle()).isEmpty()) {
            throw new ConflictException("Подборка с названием " + compilationDto.getTitle() + " уже существует");
        }
        Set<Event> events = new HashSet<>();
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            events = new HashSet<>(eventRepository.findAllByIdIn(compilationDto.getEvents().stream().toList()));
        }

        Compilation compilation = CompilationMapper.toCompilation(compilationDto, events);
        return CompilationMapper.fromCompilation(compilationRepository.save(compilation));
    }

    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest dto) {

        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка с идентификатором " + compId + " не найдена"));

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null) {
            if (!compilationRepository.findByTitleIgnoreCase(dto.getTitle()).isEmpty()) {
                throw new ConflictException("Подборка с названием " + dto.getTitle() + " уже существует");
            }
            if (!compilation.getTitle().equalsIgnoreCase(dto.getTitle())) {
                throw new ConflictException("Подборка с названием " + dto.getTitle() + " уже существует");
            }
            compilation.setTitle(dto.getTitle());
        }

        if (dto.getEvents() != null) {
            Set<Event> events = new HashSet<>();
            if (!dto.getEvents().isEmpty()) {
                events = new HashSet<>(eventRepository.findAllByIdIn(dto.getEvents().stream().toList()));
            }
            compilation.setEvents(events);
        }

        return CompilationMapper.fromCompilation(compilationRepository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка с идентификатором " + compId + "не найдена"));
        compilationRepository.deleteById(compId);
    }

    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        Page<Compilation> compilations = pinned == null ? compilationRepository.findAll(pageable) : compilationRepository.findByPinned(pinned, pageable);
        return compilations.stream().map(CompilationMapper::fromCompilation).toList();
    }

    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.fromCompilation(compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Подборка не найдена")));
    }
}
