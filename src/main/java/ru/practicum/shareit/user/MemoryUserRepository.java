package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectAlreadyExistsException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class MemoryUserRepository implements UserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public User createUser(UserDto userDto) {
        userMailCheck(userDto, idGenerator);
        User user = UserMapper.toUser(idGenerator++, userDto);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> listUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User editUser(int userId, UserDto userDto) {
        userMailCheck(userDto, userId);
        User userToUpdate = getUserById(userId);
        if (userDto.getEmail() != null)
            userToUpdate = userToUpdate.toBuilder().email(userDto.getEmail()).build();
        if (userDto.getName() != null)
            userToUpdate = userToUpdate.toBuilder().name(userDto.getName()).build();
        users.put(userId, userToUpdate);
        return users.get(userId);
    }

    @Override
    public void deleteUser(int id) {
        userExistenceCheck(id);
        users.remove(id);
    }

    @Override
    public User getUserById(int id) {
        userExistenceCheck(id);
        return users.get(id);
    }

    @Override
    public void userExistenceCheck(int id) {
        if (users.get(id) == null)
            throw new ObjectNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
    }

    private void userMailCheck(UserDto userDto, int userId) {
        if (users.values().stream()
                .anyMatch(o -> o.getEmail().equalsIgnoreCase(userDto.getEmail())
                        && o.getId() != userId))
            throw new ObjectAlreadyExistsException(
                    String.format("Пользователь с электронной почтой %s уже существует", userDto.getEmail()));
    }
}
