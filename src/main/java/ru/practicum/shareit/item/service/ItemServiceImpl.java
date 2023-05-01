package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentCreateDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.WrongUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingsAndComments;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item createItem(int userId, ItemDto itemDto) {
        userService.getUserById(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userId);

        itemRepository.save(item);

        return item;
    }

    @Override
    public List<ItemDtoWithBookingsAndComments> findAllItems(int userId) {
        userService.getUserById(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        return addBookingsAndCommentsForItems(items);
    }

    @Override
    public Item updateItem(int itemId, ItemDto itemDto, int userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Вещь с идентификатором %s не найдена", itemId)));

        if (userId != item.getOwner()) {
            throw new WrongUserException("Вещь не принадлежит пользователю");
        }
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return item;
    }

    @Override
    public ItemDtoWithBookingsAndComments getItemById(int itemId, int userId) {
        userService.getUserById(userId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Вещь с идентификатором %s не найдена", itemId)));


        List<Item> list = List.of(item);
        ItemDtoWithBookingsAndComments itemDtoWithBookingsAndComments = addBookingsAndCommentsForItems(list).get(0);

        if (userId != item.getOwner()) {
            itemDtoWithBookingsAndComments.setLastBooking(null);
            itemDtoWithBookingsAndComments.setNextBooking(null);
        }

        return itemDtoWithBookingsAndComments;
    }

    @Override
    public List<Item> getItemsByNameOrDescription(String text, int userId) {
        userService.getUserById(userId);

        if (text.isBlank()) return Collections.emptyList();
        return itemRepository.findAvailableItemsByNameOrDescription(text.toLowerCase());
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentCreateDto commentCreateDto) {
        User user = userService.getUserById(userId);
        List<Booking> pastBookings = bookingRepository.findAllByBookerIdAndPastState(userId,
                Sort.by(Sort.Direction.DESC, "end"));
        if (pastBookings.isEmpty()) {
            throw new BookingException("Вещь не была забронирована");
        }
        if (pastBookings.stream()
                .map(b -> b.getBooker().getId())
                .noneMatch(id -> id.equals(userId))) {
            throw new BookingException(
                    String.format("Вещь не была забронирована пользователем с идентификатором %s", userId));
        }
        Item item = pastBookings.stream()
                .map(Booking::getItem)
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow();

        Comment comment = CommentMapper.toComment(commentCreateDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(Instant.now());

        commentRepository.save(comment);

        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        commentDto.setAuthorName(user.getName());
        return commentDto;
    }

    private List<ItemDtoWithBookingsAndComments> addBookingsAndCommentsForItems(List<Item> items) {
        List<Integer> itemsId = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<BookingForItemDto> bookings = bookingRepository.findAllByItemIdIn(itemsId).stream()
                .filter(b -> !b.getStatus().equals(Status.REJECTED))
                .map(BookingMapper::toBookingDtoForItem)
                .collect(Collectors.toList());

        List<Comment> comments = commentRepository.findAllByItemIdIn(itemsId);

        Set<ItemDtoWithBookingsAndComments> itemsWithBookings =
                new TreeSet<>(Comparator.comparing(ItemDtoWithBookingsAndComments::getId));
        for (Item item : items) {
            ItemDtoWithBookingsAndComments itemDtoWithBookings = ItemMapper.toItemDtoWithBooking(item);
            Set<BookingForItemDto> nextBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getStart));
            Set<BookingForItemDto> lastBookings = new TreeSet<>(Comparator.comparing(BookingForItemDto::getEnd)
                    .reversed());
            for (BookingForItemDto booking : bookings) {
                LocalDateTime now = LocalDateTime.now();
                if (item.getId() == booking.getItem().getId()) {
                    if (booking.getStart().isAfter(now)) {
                        nextBookings.add(booking);
                    } else {
                        lastBookings.add(booking);
                    }
                }
            }
            itemDtoWithBookings.setNextBooking(nextBookings.stream().findFirst().orElse(null));
            itemDtoWithBookings.setLastBooking(lastBookings.stream().findFirst().orElse(null));
            itemsWithBookings.add(itemDtoWithBookings);

            if (comments.isEmpty()) {
                itemDtoWithBookings.setComments(Collections.emptyList());
                continue;
            }

            itemDtoWithBookings.setComments(new ArrayList<>());
            for (Comment comment : comments) {
                if (comment.getItem().getId() == item.getId()) {
                    CommentDto commentDto = CommentMapper.toCommentDto(comment);
                    commentDto.setAuthorName(comment.getAuthor().getName());
                    itemDtoWithBookings.getComments().add(commentDto);
                }
            }
        }

        return new ArrayList<>(itemsWithBookings);
    }
}
