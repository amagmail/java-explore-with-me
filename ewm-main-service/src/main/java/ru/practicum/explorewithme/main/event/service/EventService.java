package ru.practicum.explorewithme.main.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.main.category.dal.CategoryRepository;
import ru.practicum.explorewithme.main.category.dto.CategoryDto;
import ru.practicum.explorewithme.main.category.dto.CategoryMapper;
import ru.practicum.explorewithme.main.event.dal.EventRepository;
import ru.practicum.explorewithme.main.event.dto.*;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.exception.NotFoundException;
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

    public Collection<EventFullDto> getEvents() {
        return eventRepository.findAll().stream()
                .map(EventMapper::toEventFullDto)
                .toList();
    }

    public EventFullDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        return EventMapper.toEventFullDto(event);
    }

    //---------------------------------------------
    // Admin: События
    //---------------------------------------------

    public Collection<EventFullDto> getEventsAdmin(List<Long> users, List<Long> categories, List<String> states, String rangeStart, String rangeEnd, Integer from, Integer size) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateFrom = null;
        if (rangeStart != null) {
            dateFrom = LocalDateTime.parse(rangeStart, formatter);
        }
        LocalDateTime dateTo = null;
        if (rangeEnd != null) {
            dateTo = LocalDateTime.parse(rangeEnd, formatter);
        }
        Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
        return eventRepository.findByAdminFilters(users, categories, states, pageable).stream()
                .map(event -> {
                    EventFullDto eventDto = EventMapper.toEventFullDto(event);
                    Long catId = event.getCategory();
                    Long userId = event.getInitiator();
                    CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                    UserShortDto userDto = UserMapper.toUserShortDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId)));
                    eventDto.setInitiator(userDto);
                    eventDto.setCategory(categoryDto);
                    return eventDto;
                })
                .toList();
    }

    public EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminDto reqDto) {
        Event event = EventMapper.fromUpdateEventAdminRequest(reqDto);
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        //UserShortDto userDto = UserMapper.toUserShortDto(user);

        EventFullDto respDto = EventMapper.toEventFullDto(event);
        //respDto.setInitiator(userDto);
        respDto.setCategory(categoryDto);
        return respDto;
    }

    //---------------------------------------------
    // Public: События
    //---------------------------------------------

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

        EventFullDto respDto = EventMapper.toEventFullDto(event);
        respDto.setInitiator(userDto);
        respDto.setCategory(categoryDto);
        return respDto;
    }

    public Collection<EventShortDto> getEventsPrivate(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        return eventRepository.getUserEvents(userId).stream()
                .map(event -> {
                    Long catId = event.getCategory();
                    CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
                    UserShortDto userDto = UserMapper.toUserShortDto(user);
                    EventShortDto eventDto = EventMapper.toEventShortDto(event);
                    eventDto.setInitiator(userDto);
                    eventDto.setCategory(categoryDto);
                    return eventDto;
                })
                .toList();
    }

    public EventFullDto getEventPrivate(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.getUserEvent(userId, eventId);
        if (event == null) {
            throw new NotFoundException("Не найдено событие с идентификатором " + eventId);
        }
        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором " + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);
        EventFullDto eventDto = EventMapper.toEventFullDto(event);
        eventDto.setInitiator(userDto);
        eventDto.setCategory(categoryDto);
        return eventDto;
    }

    public EventFullDto updateEventPrivate(Long userId, Long eventId, UpdateEventDto reqDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = EventMapper.fromUpdateEventUserRequest(reqDto);
        event.setInitiator(userId);
        event = eventRepository.save(event);

        Long catId = event.getCategory();
        CategoryDto categoryDto = CategoryMapper.fromCategory(categoryRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдена категория с идентификатором" + catId)));
        UserShortDto userDto = UserMapper.toUserShortDto(user);

        EventFullDto respDto = EventMapper.toEventFullDto(event);
        respDto.setInitiator(userDto);
        respDto.setCategory(categoryDto);
        return respDto;
    }

}
