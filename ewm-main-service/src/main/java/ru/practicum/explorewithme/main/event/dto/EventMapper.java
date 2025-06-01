package ru.practicum.explorewithme.main.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.event.enums.State;
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
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        if (dto.getLocation() != null) {
            Location location = dto.getLocation();
            event.setLocationLat(Float.parseFloat("0.001"));
            event.setLocationLon(Float.parseFloat("0.002"));
        }
        return event;
    }

    public static Event fromUpdateEventUserRequest(UpdateEventUserRequest dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(dto.getCategory());
        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDateTime.parse(dto.getEventDate(), formatter));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        if (dto.getLocation() != null) {
            Location location = dto.getLocation();
            event.setLocationLat(Float.parseFloat("0.001"));
            event.setLocationLon(Float.parseFloat("0.002"));
        }
        return event;
    }

    public static Event fromUpdateEventAdminRequest(UpdateEventAdminRequest dto) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(dto.getCategory());
        event.setDescription(dto.getDescription());
        event.setEventDate(LocalDateTime.parse(dto.getEventDate(), formatter));
        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setTitle(dto.getTitle());
        if (dto.getLocation() != null) {
            Location location = dto.getLocation();
            event.setLocationLat(Float.parseFloat("0.001"));
            event.setLocationLon(Float.parseFloat("0.002"));
        }
        return event;
    }

}
