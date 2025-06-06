package ru.practicum.explorewithme.main.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.main.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.main.compilation.dto.CompilationReqDto;
import ru.practicum.explorewithme.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.main.compilation.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Slf4j
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CompilationReqDto compilationDto) {
        log.info("Получен запрос на создание подборки с данными: {}", compilationDto);
        return compilationService.createCompilation(compilationDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable Long compId, @Valid @RequestBody UpdateCompilationRequest compilationDto) {
        log.info("Получен запрос на обновление подборки с данными: {}", compilationDto);
        return compilationService.updateCompilation(compId, compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Получен запрос на удаление подборки с id={}", compId);
        compilationService.deleteCompilation(compId);
    }
}
