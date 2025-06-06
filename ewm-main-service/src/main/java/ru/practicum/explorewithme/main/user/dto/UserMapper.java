package ru.practicum.explorewithme.main.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.main.user.model.User;

@UtilityClass
public class UserMapper {

    public UserRespDto fromUser(User user) {
        return UserRespDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserReqDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
