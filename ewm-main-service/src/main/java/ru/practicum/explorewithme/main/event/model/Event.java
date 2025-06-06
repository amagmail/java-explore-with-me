package ru.practicum.explorewithme.main.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.main.event.enums.State;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "category")
    private Long category;

    @Column(name = "location_lat")
    private Float locationLat;

    @Column(name = "location_lon")
    private Float locationLon;

    @Column(name = "initiator")
    private Long initiator;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "views")
    private Integer views;

    @Column(name = "annotation")
    private String annotation;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private State state;
}
