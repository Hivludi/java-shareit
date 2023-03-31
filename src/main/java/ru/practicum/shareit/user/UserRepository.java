package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User createUser(UserDto userDto);

    List<User> listUsers();

    User editUser(int userId, UserDto userDto);

    void deleteUser(int id);

    void userExistenceCheck(int id);

    User getUserById(int id);
}
