package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(int id, ItemDto itemDto);
    Item updateItem(int id, ItemDto itemDto, int userId);
    List<ItemDtoWithBookingsAndComments> findAllItems(int id);
    List<Item> getItemsByNameOrDescription(String text, int userId);
    ItemDtoWithBookingsAndComments getItemById(int id, int userID);
    CommentDto addComment(int userId, int itemId, CommentCreateDto commentCreationDto);
}
