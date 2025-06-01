package ru.practicum.explorewithme.main.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Optional<Request> existRequest = requestRepository.findByEventAndRequester(eventId, userId);
        if (existRequest.isPresent()) {
            throw new ConflictException("Пользователь уже подавал заявку на участие в данном событии");
        }
        //TODO: добавить проверку события

        Request request = new Request();
        request.setRequester(user.getId());
        request.setEvent(eventId);
        request.setStatus(RequestState.PENDING);
        request.setCreated(LocalDateTime.now());

        return RequestMapper.fromRequest(requestRepository.save(request));
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        if (!request.getRequester().equals(userId)) {
            throw new NotFoundException("Пользователь не является автором заявки");
        }
        requestRepository.deleteById(requestId);
        return RequestMapper.fromRequest(request);
    }

    public List<RequestDto> getRequestsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return requestRepository.findAllByRequester(userId).stream().map(RequestMapper::fromRequest).toList();
    }

}
