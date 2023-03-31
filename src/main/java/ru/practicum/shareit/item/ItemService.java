package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Item createItem(ItemDto itemDto, int userId) {
        return itemRepository.createItem(itemDto, userId);
    }


    public Item editItem(int itemId, ItemDto itemDto, int userId) {
        return itemRepository.editItem(itemId, itemDto, userId);
    }


    public Item getItemById(int itemId, int userId) {
        return itemRepository.getItemById(itemId, userId);
    }


    public List<Item> getUserItems(int userId) {
        return itemRepository.getUserItems(userId);
    }


    public List<Item> searchItemByTextOrDescription(String text, int userId) {
        return itemRepository.searchItemByTextOrDescription(text, userId);
    }
}
