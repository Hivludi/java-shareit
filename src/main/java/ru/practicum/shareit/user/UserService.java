package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(UserDto userDto) {
        return userRepository.createUser(userDto);
    }

    public List<User> listUsers() {
        return userRepository.listUsers();
    }

    public User editUser(int userId, UserDto userDto) {
        return userRepository.editUser(userId, userDto);
    }

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }
}
