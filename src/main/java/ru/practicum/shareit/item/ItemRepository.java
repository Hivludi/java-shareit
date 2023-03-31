package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(ItemDto itemDto, int userId);

    Item editItem(int itemId, ItemDto itemDto, int userId);

    Item getItemById(int itemId, int userId);

    List<Item> getUserItems(int userId);

    List<Item> searchItemByTextOrDescription(String text, int userId);
}
