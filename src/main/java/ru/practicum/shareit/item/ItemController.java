package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final String SHARER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public Item createItem(@RequestBody @Validated ItemDto itemDto,
                           @RequestHeader(value = SHARER_ID) int userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item editItem(@PathVariable int itemId,
                         @RequestBody ItemDto itemDto,
                         @RequestHeader(value = SHARER_ID) int userId) {
        return itemService.editItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId,
                            @RequestHeader(value = SHARER_ID) int userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<Item> getUserItems(@RequestHeader(value = SHARER_ID) int userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItemByTextOrDescription(@RequestParam String text,
                                                    @RequestHeader(value = SHARER_ID) int userId) {
        return itemService.searchItemByTextOrDescription(text, userId);
    }
}
