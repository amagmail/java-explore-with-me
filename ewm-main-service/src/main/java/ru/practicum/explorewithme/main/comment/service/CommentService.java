package ru.practicum.explorewithme.main.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.comment.dal.CommentRepository;
import ru.practicum.explorewithme.main.comment.dto.*;
import ru.practicum.explorewithme.main.comment.model.Comment;
import ru.practicum.explorewithme.main.event.dal.EventRepository;
import ru.practicum.explorewithme.main.event.dto.EventMapper;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.user.dal.UserRepository;
import ru.practicum.explorewithme.main.user.dto.UserMapper;
import ru.practicum.explorewithme.main.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    //---------------------------------------------
    // Private: Комментарии
    //---------------------------------------------

    // Создание комментария пользователя с идентификатором userId по событию eventId
    // * на выходе получаем DTO.ResponseComment
    // * на вход передаем DTO.RequestCommentUser
    @Transactional
    public ResponseComment createCommentPrivate(Long userId, Long eventId, RequestCommentUser dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Comment comment = CommentMapper.fromRequestCommentUser(dto);
        comment.setUserId(userId);
        comment.setEventId(eventId);
        comment.setAccepted(false);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setModifyDate(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toResponseComment(comment, UserMapper.toUserShortDto(user), EventMapper.toEventNodeDto(event));
    }

    // Редактирование комментария пользователя с идентификатором userId по событию eventId
    // * на выходе получаем DTO.ResponseComment
    // * на вход передаем DTO.RequestCommentUser и идентификатор комментария commentId
    @Transactional
    public ResponseComment updateCommentPrivate(Long userId, Long eventId, Long commentId, RequestCommentUser dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Не найден комментарий с идентификатором " + commentId));
        if (!comment.getUserId().equals(userId)) {
            throw new ConflictException("Вы не можете получить приватный доступ к чужим комментариям");
        }
        if (comment.getAccepted().equals(true)) {
            throw new ConflictException("Вы не можете редактировать подтвержденный комментарий");
        }
        comment.setUserMessage(dto.getMessage());
        comment.setModifyDate(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toResponseComment(comment, UserMapper.toUserShortDto(user), EventMapper.toEventNodeDto(event));
    }

    // Получение комментария пользователя с идентификатором userId по событию eventId
    // * на выходе получаем DTO.ResponseComment
    // * на вход передаем идентификатор комментария commentId
    public ResponseComment getCommentPrivate(Long userId, Long eventId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Не найден комментарий с идентификатором " + commentId));
        if (!comment.getUserId().equals(userId)) {
            throw new ConflictException("Вы не можете получить приватный доступ к чужим комментариям");
        }
        return CommentMapper.toResponseComment(comment, UserMapper.toUserShortDto(user), EventMapper.toEventNodeDto(event));
    }

    // Получение всех комментариев пользователя с идентификатором userId по событию eventId
    // * на выходе получаем DTO.ResponseEventCommentsFull
    // * на вход без дополнительных параметров
    public ResponseEventCommentsFull getCommentsPrivate(Long userId, Long eventId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        List<ResponseCommentFull> comments = commentRepository.getEventUserComments(eventId, userId, pageable).stream()
                .map(comment -> CommentMapper.toResponseCommentFull(comment, UserMapper.toUserShortDto(user)))
                .toList();
        return ResponseEventCommentsFull.builder()
                .event(EventMapper.toEventNodeDto(event))
                .comments(comments)
                .build();
    }

    //---------------------------------------------
    // Public: Комментарии
    //---------------------------------------------

    // Получение всех опубликованных администратором (по признаку accepted) пользовательских комментариев по событию eventId
    // * на выходе получаем DTO.ResponseEventCommentsShort
    // * на вход без дополнительных параметров
    public ResponseEventCommentsShort getEventComments(Long eventId, Integer from, Integer size) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с идентификатором " + eventId));
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        List<ResponseCommentShort> comments = commentRepository.getEventComments(eventId, pageable).stream()
                .map(comment -> {
                    Long userId = comment.getUserId();
                    User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с идентификатором " + userId));
                    return CommentMapper.toResponseCommentShort(comment, UserMapper.toUserShortDto(user));
                }).toList();
        return ResponseEventCommentsShort.builder()
                .event(EventMapper.toEventNodeDto(event))
                .comments(comments)
                .build();
    }

    //---------------------------------------------
    // Admin: Комментарии
    //---------------------------------------------

    @Transactional
    public ResponseComment updateCommentAdmin(Long commentId) {
        return null;
    }

    @Transactional
    public void deleteCommentAdmin(Long commentId) {

    }

    public ResponseComment getCommentAdmin(Long commentId) {
        return null;
    }

    public ResponseEventCommentsFull getCommentsAdmin(List<Long> users, List<Long> categories, List<Long> events, String rangeStart, String rangeEnd, Integer from, Integer size) {
        return null;
    }

}
