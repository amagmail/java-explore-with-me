package ru.practicum.explorewithme.stats.server.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.stats.server.model.StatisticItem;
import ru.practicum.explorewithme.stats.server.model.StatisticWithHits;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<StatisticItem, Long> {

    @Query("SELECT s.app as app, s.uri as uri, COUNT(s) AS hits FROM Stat s WHERE s.timestamp BETWEEN :start AND :end AND (:uris IS NULL OR s.uri IN :uris) GROUP BY s.uri, s.app order by hits DESC")
    Collection<StatisticWithHits> findByParams(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT s.app as app, s.uri as uri, COUNT(DISTINCT s.ip) AS hits FROM Stat s WHERE s.timestamp BETWEEN :start AND :end AND (:uris IS NULL OR s.uri IN :uris) GROUP BY s.uri, s.app order by hits DESC")
    Collection<StatisticWithHits> findByParamsAndUniqueByIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
