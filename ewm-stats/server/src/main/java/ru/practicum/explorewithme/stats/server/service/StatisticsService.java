package ru.practicum.explorewithme.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.stats.dto.HitDto;
import ru.practicum.explorewithme.stats.dto.StatDto;
import ru.practicum.explorewithme.stats.server.dal.StatisticsRepository;
import ru.practicum.explorewithme.stats.server.mapper.StatisticsMapper;
import ru.practicum.explorewithme.stats.server.model.StatisticWithHits;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statsRepository;

    @Transactional
    public void saveHit(HitDto hitDto) {
        statsRepository.save(StatisticsMapper.fromHitDto(hitDto));
    }

    public Collection<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Collection<StatisticWithHits> stats = unique ? statsRepository.findByParamsAndUniqueByIp(start, end, uris) : statsRepository.findByParams(start, end, uris);
        return stats.stream()
                .map(StatisticsMapper::statDtoFromStatWithHits)
                .toList();
    }
}
