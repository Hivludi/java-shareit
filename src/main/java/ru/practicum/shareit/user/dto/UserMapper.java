package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }

    public static User toUser(int userId, UserDto userDto) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }
}
