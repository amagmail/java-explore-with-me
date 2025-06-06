package ru.practicum.explorewithme.main.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.event.model.Location;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFullDto toEventFullDto(Event event, CategoryDto categoryDto, UserShortDto initiatorDto) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .initiator(initiatorDto)
                .category(categoryDto)
                .location(new Location(event.getLocationLat(), event.getLocationLon()))
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

        Event event = Event.builder()
                .category(dto.getCategory())
                .participantLimit(dto.getParticipantLimit())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .annotation(dto.getAnnotation())
                .paid(dto.getPaid())
                .requestModeration(dto.getRequestModeration())
                .eventDate(LocalDateTime.parse(dto.getEventDate(), formatter))
                .build();

        if (dto.getLocation() != null) {
            event.setLocationLat(dto.getLocation().lat);
            event.setLocationLon(dto.getLocation().lon);
        }
        return event;
    }

}
