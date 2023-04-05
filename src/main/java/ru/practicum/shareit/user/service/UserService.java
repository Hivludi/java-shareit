package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User createUser(UserDto userDto);

    void deleteUser(int id);

    User getUserById(int id);

    User editUser(int id, UserDto userDto);
}
