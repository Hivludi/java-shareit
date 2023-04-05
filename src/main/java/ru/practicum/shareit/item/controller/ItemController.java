package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String SHARER_ID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemServiceImpl;

    @PostMapping
    public Item createItem(@RequestBody @Validated ItemDto itemDto,
                           @RequestHeader(value = SHARER_ID) int userId) {
        return itemServiceImpl.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@PathVariable int itemId,
                         @RequestBody ItemDto itemDto,
                         @RequestHeader(value = SHARER_ID) int userId) {
        return itemServiceImpl.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingsAndComments getItemById(@PathVariable int itemId,
                                                      @RequestHeader(value = SHARER_ID) int userId) {
        return itemServiceImpl.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoWithBookingsAndComments> getUserItems(@RequestHeader(value = SHARER_ID) int userId) {
        return itemServiceImpl.findAllItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItemByTextOrDescription(@RequestParam String text,
                                                    @RequestHeader(value = SHARER_ID) int userId) {
        return itemServiceImpl.getItemsByNameOrDescription(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = SHARER_ID) int userId,
                              @PathVariable @Positive int itemId,
                              @RequestBody @Valid CommentCreateDto commentCreationDto) {
        return itemServiceImpl.addComment(userId, itemId, commentCreationDto);
    }
}
