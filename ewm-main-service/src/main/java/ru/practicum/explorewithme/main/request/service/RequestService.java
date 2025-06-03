package ru.practicum.explorewithme.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.event.dal.EventRepository;
import ru.practicum.explorewithme.main.event.enums.State;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.request.dal.RequestRepository;
import ru.practicum.explorewithme.main.request.dto.RequestDto;
import ru.practicum.explorewithme.main.request.dto.RequestMapper;
import ru.practicum.explorewithme.main.request.model.Request;
import ru.practicum.explorewithme.main.request.enums.RequestState;
import ru.practicum.explorewithme.main.user.dal.UserRepository;
import ru.practicum.explorewithme.main.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getInitiator().equals(userId)) {
            throw new ConflictException("Пользователь не может создать запрос на собственное мероприятие");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Мероприятие неопубликовано");
        }
        if (requestRepository.existsByEventAndRequester(eventId, userId)) {
            throw new ConflictException("Такой запрос уже существует");
        }
        int confirmedCount = requestRepository.countByEventAndStatus(eventId, RequestState.CONFIRMED);
        if (event.getParticipantLimit() != 0 && confirmedCount >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит по участникам");
        }

        RequestState status = (event.getParticipantLimit() == 0 || !event.getRequestModeration()) ? RequestState.CONFIRMED : RequestState.PENDING;
        Request request = Request.builder()
                .event(eventId)
                .requester(userId)
                .created(LocalDateTime.now())
                .status(status)
                .build();
        return RequestMapper.fromRequest(requestRepository.save(request));
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        if (!request.getRequester().equals(userId)) {
            throw new NotFoundException("Пользователь не является автором заявки");
        }
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.fromRequest(request);
    }

    public List<RequestDto> getRequestsByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return requestRepository.findAllByRequester(userId).stream()
                .map(RequestMapper::fromRequest)
                .toList();
    }

}
