package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(UserDto userDto) {
        return userRepository.save(UserMapper.toUser(userDto));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User editUser(int userId, UserDto userDto) {
        User userToUpdate = getUserById(userId);
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank())
            userToUpdate.setEmail(userDto.getEmail());
        if (userDto.getName() != null && !userDto.getName().isBlank())
            userToUpdate.setName(userDto.getName());
        userRepository.save(userToUpdate);
        return userToUpdate;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.delete(getUserById(id));
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                        new ObjectNotFoundException(String.format("Пользователь с идентификатором %s не найден", id)));
    }
}
