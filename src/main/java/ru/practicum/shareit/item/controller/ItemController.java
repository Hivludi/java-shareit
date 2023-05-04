package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final String xSharerUserId = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addNewItem(@Valid @RequestBody @NotNull ItemDto itemDto,
                              @RequestHeader(xSharerUserId) Long ownerId) {
        itemDto.setOwner(ownerId);
        log.info("save new item = {}", itemDto);
        return itemService.addNewItem(itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable("id") Long id,
                              @RequestBody @NotNull ItemDto itemDto,
                              @RequestHeader(xSharerUserId) Long ownerId) {
        itemDto.setId(id);
        itemDto.setOwner(ownerId);
        log.info("update item = {}", itemDto);
        return itemService.updateItem(itemDto);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable("id") Long id,
                           @RequestHeader(xSharerUserId) Long userId) {
        log.info("get item with id = {} from user with id = {}", id, userId);
        return itemService.getItem(id, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(
            @RequestHeader(xSharerUserId) Long ownerId,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(value = 0) Long from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size
    ) {
        log.info("get items with owner id = {}", ownerId);
        return itemService.getItemsByOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestParam(name = "text") String text,
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(value = 0) Long from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size
    ) {
        log.info("search items name or desc contains = {}", text);
        return itemService.search(text, from, size);
    }

    @PostMapping("/{id}/comment")
    public CommentDto addComment(@PathVariable("id") Long id,
                                 @RequestHeader(xSharerUserId) Long authorId,
                                 @Valid @RequestBody @NotNull CommentDto commentDto) {
        commentDto.setCreated(LocalDateTime.now());
        return itemService.addComment(id, authorId, commentDto);
    }

}