package ru.practicum.explorewithme.stats.server.mapper;

import ru.practicum.explorewithme.stats.dto.HitDto;
import ru.practicum.explorewithme.stats.dto.StatDto;
import ru.practicum.explorewithme.stats.server.model.StatisticItem;
import ru.practicum.explorewithme.stats.server.model.StatisticWithHits;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatisticsMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static StatisticItem fromHitDto(HitDto hitDto) {
        StatisticItem stat = new StatisticItem();
        stat.setApp(hitDto.getApp());
        stat.setUri(hitDto.getUri());
        stat.setIp(hitDto.getIp());
        stat.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return stat;
    }

    public static StatDto statDtoFromStatWithHits(StatisticWithHits stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }
}
