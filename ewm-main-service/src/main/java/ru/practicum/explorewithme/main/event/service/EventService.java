package ru.practicum.explorewithme.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.main.category.dal.CategoryRepository;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.category.dto.CategoryMapper;
import ru.practicum.explorewithme.main.event.dal.EventRepository;
import ru.practicum.explorewithme.main.event.dto.*;
import ru.practicum.explorewithme.main.event.enums.Sorting;
import ru.practicum.explorewithme.main.event.enums.State;
import ru.practicum.explorewithme.main.event.enums.StateAction;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.request.dal.RequestRepository;
import ru.practicum.explorewithme.main.request.dto.RequestDto;
import ru.practicum.explorewithme.main.request.dto.RequestMapper;
import ru.practicum.explorewithme.main.request.model.Request;
import ru.practicum.explorewithme.main.user.dal.UserRepository;
import ru.practicum.explorewithme.main.user.dto.UserMapper;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;
import ru.practicum.explorewithme.main.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //---------------------------------------------
    // Admin: События
    //---------------------------------------------

    public Collection<EventFullDto> getEventsAdmin(List<Long> users, List<Long> categories, List<String> states, String rangeStart, String rangeEnd, Integer from, Integer size) {

        LocalDateTime dateFrom = null;
        if (rangeStart != null) {
            dateFrom = LocalDateTime.parse(rangeStart, formatter);
        }
        LocalDateTime dateTo = null;
        if (rangeEnd != null) {
            dateTo = LocalDateTime.parse(rangeEnd, formatter);
        }
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        return eventRepository.getEventsAdmin(users, categories, states, dateFrom, dateTo, pageable).stream()
                .map(event -> {
                    Long catId = event.getCategory();
                    Long userId = event.getInitiator();
                    CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                    UserShortDto initiatorDto = UserMapper.toUserShortDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId)));
                    return EventMapper.toEventFullDto(event, categoryDto, initiatorDto);
                })
                .toList();
    }

    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest entity) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        if (entity.getAnnotation() != null) {
            event.setAnnotation(entity.getAnnotation());
        }
        if (entity.getCategory() != null) {
            event.setCategory(entity.getCategory());
        }
        if (entity.getDescription() != null) {
            event.setDescription(entity.getDescription());
        }
        if (entity.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(entity.getEventDate(), formatter));
        }
        if (entity.getPaid() != null) {
            event.setPaid(entity.getPaid());
        }
        if (entity.getParticipantLimit() != null) {
            event.setParticipantLimit(entity.getParticipantLimit());
        }
        if (entity.getRequestModeration() != null) {
            event.setRequestModeration(entity.getRequestModeration());
        }
        if (entity.getTitle() != null) {
            event.setTitle(entity.getTitle());
        }
        if (entity.getLocation() != null) {
            event.setLocationLat(entity.getLocation().lat);
            event.setLocationLon(entity.getLocation().lon);
        }
        if (entity.getStateAction() != null ) {
            switch (entity.getStateAction()) {
                case StateAction.PUBLISH_EVENT:
                    if (event.getState() != State.PENDING) {
                        throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
                    }
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case StateAction.REJECT_EVENT:
                    if (event.getState() == State.PUBLISHED) {
                        throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
                    }
                    event.setState(State.CANCELED);
                    event.setPublishedOn(null);
                    break;
            }
        }
        event = eventRepository.save(event);
        Long catId = event.getCategory();
        Long userId = event.getInitiator();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto initiatorDto = UserMapper.toUserShortDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId)));
        return EventMapper.toEventFullDto(event, categoryDto, initiatorDto);
    }

    //---------------------------------------------
    // Private: События
    //---------------------------------------------

    public EventFullDto createEventPrivate(Long userId, NewEventDto reqDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = EventMapper.fromNewEventDto(reqDto);
        event.setInitiator(userId);
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

    public Collection<EventShortDto> getEventsPrivate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        return eventRepository.getEventsPrivate(userId).stream()
                .map(event -> {
                    Long catId = event.getCategory();
                    CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                    UserShortDto userDto = UserMapper.toUserShortDto(user);
                    EventShortDto eventDto = EventMapper.toEventShortDto(event);
                    eventDto.setInitiator(userDto);
                    eventDto.setCategory(categoryDto);
                    return eventDto;
                }).toList();
    }

    public EventFullDto getEventPrivate(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.getEventPrivate(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Не найдено событие с идентификатором " + eventId);
        }
        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором " + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

    public EventFullDto updateEventPrivate(Long userId, Long eventId, UpdateEventUserRequest entity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        if (!event.getInitiator().equals(userId)) {
            throw new ConflictException("У пользователя " + userId + " нет доступа к событию " + eventId);
        }
        if (entity.getAnnotation() != null) {
            event.setAnnotation(entity.getAnnotation());
        }
        if (entity.getCategory() != null) {
            event.setCategory(entity.getCategory());
        }
        if (entity.getDescription() != null) {
            event.setDescription(entity.getDescription());
        }
        if (entity.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(entity.getEventDate(), formatter));
        }
        if (entity.getPaid() != null) {
            event.setPaid(entity.getPaid());
        }
        if (entity.getParticipantLimit() != null) {
            event.setParticipantLimit(entity.getParticipantLimit());
        }
        if (entity.getRequestModeration() != null) {
            event.setRequestModeration(entity.getRequestModeration());
        }
        if (entity.getTitle() != null) {
            event.setTitle(entity.getTitle());
        }
        if (entity.getLocation() != null) {
            event.setLocationLat(entity.getLocation().lat);
            event.setLocationLon(entity.getLocation().lon);
        }
        if (entity.getStateAction() != null) {
            //TODO: изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409)
        }
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

    public Collection<RequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        return eventRepository.getEventRequestsPrivate(userId, eventId).stream().map(RequestMapper::fromRequest).toList();
    }

    public EventRequestStatusUpdateResult updateEventRequestsPrivate(Long userId, Long eventId, EventRequestStatusUpdateRequest entity) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        List<Request> eventRequests = eventRepository.getEventRequestsPrivate(userId, eventId);
        //TODO: сформировать объект и сохранить статусы заявок, добавить все необходимые проверки
        return null;
    }

    //---------------------------------------------
    // Public: События
    //---------------------------------------------

    public List<EventFullDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, Sorting sort, Integer from, Integer size) {
        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        LocalDateTime dateFrom = null;
        if (rangeStart != null) {
            dateFrom = LocalDateTime.parse(rangeStart, formatter);
        }
        LocalDateTime dateTo = null;
        if (rangeEnd != null) {
            dateTo = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (rangeStart == null && rangeEnd == null) {
            dateFrom = LocalDateTime.now();
        }
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        List<EventFullDto> resp = null;
        switch (sort) {
            case Sorting.EVENT_DATE:
                resp = eventRepository.getEventsPublicOrderByEventDate(text, categories, paid, onlyAvailable, dateFrom, dateTo, pageable).stream()
                        .map(event -> {
                            Long catId = event.getCategory();
                            CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                            Long userId = event.getInitiator();
                            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
                            UserShortDto userDto = UserMapper.toUserShortDto(user);
                            return EventMapper.toEventFullDto(event, categoryDto, userDto);
                        })
                        .toList();
                break;
            case Sorting.VIEWS:
                resp = eventRepository.getEventsPublicOrderByViews(text, categories, paid, onlyAvailable, dateFrom, dateTo, pageable).stream()
                        .map(event -> {
                            Long catId = event.getCategory();
                            CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                            Long userId = event.getInitiator();
                            User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
                            UserShortDto userDto = UserMapper.toUserShortDto(user);
                            return EventMapper.toEventFullDto(event, categoryDto, userDto);
                        })
                        .toList();
                break;
        }
        return resp;
    }

    public EventFullDto getEventPublic(Long eventId) {
        //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        Event event = eventRepository.getEventPublic(eventId);
        if (event == null) {
            throw new NotFoundException("Не найдено событие с идентификатором " + eventId);
        }
        Long userId = event.getInitiator();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

}
