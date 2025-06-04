package ru.practicum.explorewithme.main.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.explorewithme.main.exception.BadRequestException;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.request.dal.RequestRepository;
import ru.practicum.explorewithme.main.request.dto.RequestDto;
import ru.practicum.explorewithme.main.request.dto.RequestMapper;
import ru.practicum.explorewithme.main.request.enums.RequestState;
import ru.practicum.explorewithme.main.request.model.Request;
import ru.practicum.explorewithme.main.user.dal.UserRepository;
import ru.practicum.explorewithme.main.user.dto.UserMapper;
import ru.practicum.explorewithme.main.user.dto.UserShortDto;
import ru.practicum.explorewithme.main.user.model.User;
import ru.practicum.explorewithme.statclient.ClientStat;
import ru.practicum.explorewithme.statdto.StatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final ClientStat statisticsClient;

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
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            throw new BadRequestException("Интервал фильтрации задан некорректно");
        }
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        return eventRepository.getEventsAdmin(users, categories, states, dateFrom, dateTo, pageable).stream()
                .map(event -> {
                    if (event.getConfirmedRequests() == null) {
                        int confirmed = requestRepository.countByEventAndStatus(event.getId(), RequestState.CONFIRMED);
                        event.setConfirmedRequests(confirmed);
                    }
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
            LocalDateTime date = LocalDateTime.parse(entity.getEventDate(), formatter);
            if (date.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Необходимо соблюдать минимальный интервал времени в 2 часа до начала события");
            }
            if (event.getPublishedOn() != null && date.isBefore(event.getPublishedOn().plusHours(1))) {
                throw new ConflictException("Дата события должна быть не менее чем на 1 час позже даты публикации");
            }
            event.setEventDate(date);
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
        if (LocalDateTime.parse(reqDto.getEventDate(), formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Необходимо соблюдать минимальный интервал времени в 2 часа до начала события");
        }
        if (reqDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (reqDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (reqDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        event.setPublishedOn(null);
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

    public Collection<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return eventRepository.getEventsPrivate(userId, pageRequest).stream()
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
        if (event.getState() != State.PENDING && event.getState() != State.CANCELED) {
            throw new ConflictException("Изменять можно только события в статусах: PENDING, CANCELED");
        }
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
            LocalDateTime date = LocalDateTime.parse(entity.getEventDate(), formatter);
            if (date.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Необходимо соблюдать минимальный интервал времени в 2 часа до начала события");
            }
            event.setEventDate(date);
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
        if (entity.getStateAction() != null && entity.getStateAction() == StateAction.SEND_TO_REVIEW) {
            event.setState(State.PENDING);
        }
        if (entity.getStateAction() != null && entity.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(State.CANCELED);
        }
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

    public Collection<RequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        if (!event.getInitiator().equals(userId)) {
            throw new ConflictException("Доступ разрешен только инициатору заявки");
        }
        return requestRepository.findByEvent(eventId).stream()
                .map(RequestMapper::fromRequest)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateEventRequestsPrivate(Long userId, Long eventId, EventRequestStatusUpdateRequest entity) {
        /*
        1. если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        2. нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
        3. статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        4. если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить */

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        if (!event.getInitiator().equals(userId)) {
            throw new ConflictException("Заявку может изменить только инициатор");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ConflictException("Нет необходимости подтверждать эту заявку");
        }
        List<Request> requests = requestRepository.findByIdIn(entity.getRequestIds());
        List<Request> confirmed = new ArrayList<>();
        List<Request> rejected = new ArrayList<>();
        int confirmedCount = requestRepository.countByEventAndStatus(eventId, RequestState.CONFIRMED);

        for (Request req : requests) {
            if (!req.getStatus().equals(RequestState.PENDING)) {
                throw new ConflictException("Обновить можно только заявки в статусе PENDING");
            }
        }
        for (Request req : requests) {
            if (entity.getStatus() == RequestState.CONFIRMED) {
                if (event.getParticipantLimit() != 0 && confirmedCount >= event.getParticipantLimit()) {
                    req.setStatus(RequestState.REJECTED);
                    rejected.add(req);
                } else {
                    req.setStatus(RequestState.CONFIRMED);
                    confirmed.add(req);
                    confirmedCount++;
                }
            } else if (entity.getStatus() == RequestState.REJECTED) {
                req.setStatus(RequestState.REJECTED);
                rejected.add(req);
            }
        }
        requestRepository.saveAll(requests);

        if (entity.getStatus() == RequestState.CONFIRMED && event.getParticipantLimit() != 0 && confirmedCount >= event.getParticipantLimit()) {
            List<Request> pending = requestRepository.findByEventAndStatus(eventId, RequestState.PENDING);
            for (Request req : pending) {
                req.setStatus(RequestState.REJECTED);
            }
            requestRepository.saveAll(pending);
            rejected.addAll(pending);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed.stream().map(RequestMapper::fromRequest).collect(Collectors.toList()))
                .rejectedRequests(rejected.stream().map(RequestMapper::fromRequest).collect(Collectors.toList()))
                .build();
    }

    //---------------------------------------------
    // Public: События
    //---------------------------------------------

    public List<EventFullDto> getEventsPublic(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, Sorting sort, Integer from, Integer size) {
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
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            throw new BadRequestException("Диапазон дат задан неверно");
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

        Event event = eventRepository.findByIdAndState(eventId, State.PUBLISHED).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Long userId = event.getInitiator();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        Long catId = event.getCategory();

        LocalDateTime dateFrom = LocalDateTime.parse("1999-01-01 00:00:00", formatter);
        LocalDateTime dateTo = LocalDateTime.now();
        String[] uris = {"/events/" + eventId.toString()};
        ResponseEntity<Object> statistic = statisticsClient.getStats(dateFrom, dateTo, uris, true);

        if (statistic != null && statistic.hasBody()) {
            ObjectMapper mapper = new ObjectMapper();
            StatDto[] dto = mapper.convertValue(statistic.getBody(), StatDto[].class);
            Integer views = 0;
            if (dto != null && dto.length > 0) {
                views = dto[0].getHits();
            }
            event.setViews(views);
        }

        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        return EventMapper.toEventFullDto(event, categoryDto, userDto);
    }

}
