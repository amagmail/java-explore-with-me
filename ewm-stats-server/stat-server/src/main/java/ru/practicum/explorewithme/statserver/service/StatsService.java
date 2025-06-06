package ru.practicum.explorewithme.statserver.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.statdto.HitDto;
import ru.practicum.explorewithme.statdto.StatDto;
import ru.practicum.explorewithme.statserver.dal.StatsRepository;
import ru.practicum.explorewithme.statserver.mapper.StatsMapper;
import ru.practicum.explorewithme.statserver.model.StatWithHits;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public void saveHit(HitDto hitDto) {
        statsRepository.save(StatsMapper.fromHitDto(hitDto));
    }

    public Collection<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws BadRequestException {
        if (start == null) {
            throw new BadRequestException("Дата начала в фильтрах не может быть пустым");
        }
        if (end == null) {
            throw new BadRequestException("Дата конца в фильтрах не может быть пустым");
        }
        if (end.isBefore(start)) {
            throw new BadRequestException("В фильтрах используется неверный интервал дат");
        }
        Collection<StatWithHits> stats = unique ? statsRepository.findByParamsAndUniqueByIp(start, end, uris) : statsRepository.findByParams(start, end, uris);
        return stats.stream().map(StatsMapper::statDtoFromStatWithHits).toList();
    }
}
