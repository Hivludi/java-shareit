package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemoryItemRepository implements ItemRepository {

    private final UserRepository userRepository;
    private final Map<Integer, Item> items = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Item createItem(ItemDto itemDto, int userId) {
        userRepository.userExistenceCheck(userId);
        Item item = ItemMapper.toItem(idGenerator++, itemDto, userRepository.getUserById(userId));
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item editItem(int itemId, ItemDto itemDto, int userId) {
        Item itemToUpdate = getItemById(itemId, userId);
        if (itemToUpdate.getOwner().getId() != userId)
            throw new WrongUserException("У пользователя нет прав на редактирование данной вещи");
        if (itemDto.getName() != null)
            itemToUpdate = itemToUpdate.toBuilder().name(itemDto.getName()).build();
        if (itemDto.getDescription() != null)
            itemToUpdate = itemToUpdate.toBuilder().description(itemDto.getDescription()).build();
        if (itemDto.getAvailable() != null)
            itemToUpdate = itemToUpdate.toBuilder().available(itemDto.getAvailable()).build();
        items.put(itemId, itemToUpdate);
        return items.get(itemId);
    }

    @Override
    public Item getItemById(int itemId, int userId) {
        itemExistenceCheck(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getUserItems(int userId) {
        userRepository.userExistenceCheck(userId);
        return items.values().stream()
                .filter(o -> o.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemByTextOrDescription(String text, int userId) {
        return items.values().stream()
                .filter(o -> o.getAvailable().equals(true)
                        && !text.isBlank()
                        && o.getDescription().concat(o.getName()).toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void itemExistenceCheck(int id) {
        if (items.get(id) == null)
            throw new ObjectNotFoundException(String.format("Вещь с идентификатором %s не найдена", id));
    }
}
