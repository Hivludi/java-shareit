package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toItem(int itemId, ItemDto itemDto, User user) {
        return new Item(itemId, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), user);
    }
}