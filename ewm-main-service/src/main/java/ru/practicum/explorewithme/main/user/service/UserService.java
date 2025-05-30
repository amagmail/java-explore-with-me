package ru.practicum.explorewithme.main.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.user.dal.UserRepository;
import ru.practicum.explorewithme.main.user.dto.UserReqDto;
import ru.practicum.explorewithme.main.user.dto.UserRespDto;
import ru.practicum.explorewithme.main.user.dto.UserMapper;
import ru.practicum.explorewithme.main.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserRespDto createUser(UserReqDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        return UserMapper.fromUser(userRepository.save(UserMapper.toUser(userDto)));
    }

    public List<UserRespDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null || ids.isEmpty()) {
            Pageable pageable = PageRequest.of(from/size, size, Sort.by("id"));
            Page<User> pagedResult = userRepository.findAll(pageable);
            return pagedResult.stream().map(UserMapper::fromUser).toList();
        } else {
            return userRepository.findByIdIn(ids).stream().map(UserMapper::fromUser).toList();
        }
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким id не существует"));
        userRepository.deleteById(userId);
    }

}
