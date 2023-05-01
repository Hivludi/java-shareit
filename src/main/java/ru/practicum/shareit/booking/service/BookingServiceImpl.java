package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public @Valid BookingDto create(int userId, BookingCreateDto bookingCreateDto) {
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Вещь с идентификатором: %s не найдена", bookingCreateDto.getItemId())));
        if (!item.getAvailable()) {
            throw new BookingException(
                    String.format("Вещь с идентификатором: %s недоступна для бронирования", item.getId()));
        }
        if (item.getOwner() == userId) {
            throw new ObjectNotFoundException("Невозможно забронировать собственную вещь");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Пользователь с идентификатором: %s не найден", userId)));
        Booking booking = BookingMapper.toBooking(bookingCreateDto, item, user);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public @Valid BookingDto approve(int bookingId, boolean approved, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Бронирование с идентификатором: %s не найдено", bookingId)));

        if (booking.getItem().getOwner() != userId) {
            throw new ObjectNotFoundException("Предмет не принадлежит данному пользователю");
        }
        if (booking.getStatus().equals(Status.APPROVED) && approved) {
            throw new BookingException(String.format("Бронирование с идентификатором: %s уже подтверждено", bookingId));
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public @Valid BookingDto getById(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        String.format("Бронирование с идентификатором: %s не найдено", bookingId)));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner() != userId) {
            throw new ObjectNotFoundException("Бронирование не может быть просмотрено данным пользователем");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(String state, int userId) {

        State resultState = throwIfStateNotValid(state);

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с идентификатором: %s не найден", userId));
        }

        List<BookingDto> result = Collections.emptyList();

        switch (resultState) {
            case ALL:
                result = BookingMapper.toListOfBookingDto(bookingRepository.findAllByBookerId(userId,
                        Sort.by(Sort.Direction.DESC, "end")));
                break;
            case CURRENT:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndCurrentState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case PAST:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndPastState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case FUTURE:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndFutureState(userId, Sort.by(Sort.Direction.DESC, "end")));
                break;
            case WAITING:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndStatus(userId, Status.WAITING,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case REJECTED:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByBookerIdAndStatus(userId, Status.REJECTED,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
        }
        return result;
    }

    @Override
    public List<BookingDto> getAllByOwner(String state, int userId) {

        State resultState = throwIfStateNotValid(state);

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException(String.format("Пользователь с идентификатором: %s не найден", userId));
        }

        List<BookingDto> result = Collections.emptyList();

        switch (resultState) {
            case ALL:
                result = BookingMapper.toListOfBookingDto(bookingRepository.findAllByOwnerId(userId,
                        Sort.by(Sort.Direction.DESC, "end")));
                break;
            case CURRENT:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndCurrentState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case PAST:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndPastState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case FUTURE:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndFutureState(userId,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case WAITING:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.WAITING,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
            case REJECTED:
                result = BookingMapper.toListOfBookingDto(bookingRepository
                        .findAllByOwnerIdAndWaitingOrRejectedState(userId, Status.REJECTED,
                                Sort.by(Sort.Direction.DESC, "end")));
                break;
        }
        return result;
    }

    private State throwIfStateNotValid(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BookingException(String.format("Unknown state: %s", state));
        }
    }
}
