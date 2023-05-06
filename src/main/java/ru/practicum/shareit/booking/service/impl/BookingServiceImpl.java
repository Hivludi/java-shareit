package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.exeption.BadRequestException;
import ru.practicum.shareit.util.exeption.NotFoundException;
import ru.practicum.shareit.util.exeption.UserVerificationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    @Override
    public BookingDto createNewBooking(BookingDto bookingDto, Long bookerId) {
        Long itemId = bookingDto.getItemId();
        Item item = itemService.checkIfItemExist(itemId);
        User booker = userService.checkIfUserExist(bookerId);

        if (Objects.equals(item.getOwner().getId(), booker.getId())) {
            throw new UserVerificationException("Booking could not be created by item owner");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = checkIfBookingExist(bookingId);
        User owner = userService.checkIfUserExist(ownerId);
        Item item = booking.getItem();
        itemService.checkIfUserIsOwner(item, owner.getId());
        if (approved && !booking.getStatus().equals(Status.APPROVED)) {
            booking.setStatus(Status.APPROVED);
        } else if (!approved && !booking.getStatus().equals(Status.REJECTED)) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new BadRequestException("Booking status was already changed");
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        User user = userService.checkIfUserExist(userId);
        Booking booking = checkIfBookingExist(bookingId);
        checkAccess(user, booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public List<BookingDto> getUserBookings(Long userId, String state, Long from, Integer size) {
        User user = userService.checkIfUserExist(userId);
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        State bookingState = getBookingState(state);
        int startPage = Math.toIntExact(from / size);
        PageRequest pageRequest = PageRequest.of(startPage, size);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByIdDesc(user.getId(), pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findAllPastBookings(user.getId(), now);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllFutureBookings(user.getId(), now);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllCurrentBookings(user.getId(), now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllWaiting(user.getId());
                break;
            case REJECTED:
                bookings = bookingRepository.findAllRejected(user.getId());
                break;
        }
        if (bookingState.equals(State.CURRENT)) return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getId))
                .collect(Collectors.toList());
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String state, Long from, Integer size) {
        User owner = userService.checkIfUserExist(ownerId);
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        State bookingState = getBookingState(state);
        int startPage = Math.toIntExact(from / size);
        PageRequest pageRequest = PageRequest.of(startPage, size);
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllOwnersBookings(owner.getId(), pageRequest);
                break;
            case PAST:
                bookings = bookingRepository.findAllOwnersPastBookings(owner.getId(), now);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllOwnersFutureBookings(owner.getId(), now);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllOwnersCurrentBookings(owner.getId(), now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllOwnersWaiting(owner.getId());
                break;
            case REJECTED:
                bookings = bookingRepository.findAllOwnersRejected(owner.getId());
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Booking checkIfBookingExist(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new NotFoundException(
                    String.format("%s with id= %s not found", Booking.class.getSimpleName(), id));
        }
        return booking.get();
    }

    private void checkAccess(User user, Booking booking) {
        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();
        if (!Objects.equals(ownerId, user.getId()) && !Objects.equals(bookerId, user.getId())) {
            throw new UserVerificationException("only booker or item owner could get booking info");
        }
    }

    private State getBookingState(String state) {
        try {
            return State.valueOf(state);
        } catch (Throwable e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }


}
