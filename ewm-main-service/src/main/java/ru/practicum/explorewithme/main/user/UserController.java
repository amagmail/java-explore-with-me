package ru.practicum.explorewithme.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.main.user.dto.UserReqDto;
import ru.practicum.explorewithme.main.user.dto.UserRespDto;
import ru.practicum.explorewithme.main.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDto createUser(@Valid @RequestBody UserReqDto userReqDto) {
        log.info("Получен запрос на создание пользователя с данными: {}", userReqDto);
        return userService.createUser(userReqDto);
    }

    @GetMapping("/users")
    public List<UserRespDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {

        log.info("Получен запрос на получение пользователей c параметрами: ids={}, from={}, size={}", ids, from, size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(value = "userId") Long userId) {
        log.info("Получен запрос на удаление пользователя с id={}", userId);
        userService.deleteUserById(userId);
    }

}
