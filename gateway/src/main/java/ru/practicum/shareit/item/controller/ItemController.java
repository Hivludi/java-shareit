package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewItem(@Valid @RequestBody @NotNull ItemDto itemDto,
                                             @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("save new item = {}", itemDto);
        return itemClient.addNewItem(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable("id") Long id,
                              @RequestBody @NotNull ItemDto itemDto,
                              @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("update item = {}", itemDto);
        return itemClient.updateItem(id, ownerId, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable("id") Long id,
                           @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("get item with id = {} from user with id = {}", id, userId);
        return itemClient.getItem(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(
            @RequestHeader(X_SHARER_USER_ID) Long ownerId,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(value = 0) Long from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size
    ) {
        log.info("get items with owner id = {}", ownerId);
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(name = "text") String text,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(value = 0) Long from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size
    ) {
        log.info("search items name or desc contains = {}", text);
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable("id") Long id,
                                 @RequestHeader(X_SHARER_USER_ID) Long authorId,
                                 @Valid @RequestBody @NotNull CommentDto commentDto) {
        return itemClient.addComment(id, authorId, commentDto);
    }
}
