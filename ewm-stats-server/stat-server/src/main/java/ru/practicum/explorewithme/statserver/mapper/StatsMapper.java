package ru.practicum.explorewithme.statserver.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.statdto.HitDto;
import ru.practicum.explorewithme.statdto.StatDto;
import ru.practicum.explorewithme.statserver.model.Stat;
import ru.practicum.explorewithme.statserver.model.StatWithHits;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Stat fromHitDto(HitDto hitDto) {
        Stat stat = new Stat();
        stat.setApp(hitDto.getApp());
        stat.setUri(hitDto.getUri());
        stat.setIp(hitDto.getIp());
        stat.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return stat;
    }

    public StatDto statDtoFromStatWithHits(StatWithHits stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }
}
