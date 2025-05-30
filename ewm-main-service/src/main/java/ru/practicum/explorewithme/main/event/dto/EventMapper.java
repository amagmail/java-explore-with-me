package ru.practicum.explorewithme.main.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                //.state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event fromNewEventDto(NewEventDto dto) {
        Event event = new Event();
        event.setCategory(dto.getCategory());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setAnnotation(dto.getAnnotation());
        event.setPaid(dto.getPaid());
        event.setRequestModeration(dto.getRequestModeration());
        event.setEventDate(LocalDateTime.parse(dto.getEventDate(), formatter));
        if (dto.getPaid() == null) {
            event.setPaid(false);
        }
        if (dto.getRequestModeration() == null) {
            event.setRequestModeration(false);
        }
        if (dto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        return event;
    }

    public static Event fromUpdateEventUserRequest(UpdateEventDto dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(dto.getCategory());
        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDateTime.parse(dto.getEventDate(), formatter));
        // TODO: location
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        // TODO: stateAction
        event.setTitle(dto.getTitle());
        return event;
    }

}
