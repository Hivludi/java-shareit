package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Item {
    private final int id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final User owner;
    private ItemRequest request;
}