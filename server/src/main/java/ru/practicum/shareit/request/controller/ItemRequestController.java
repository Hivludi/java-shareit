package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createNewRequest(@RequestBody ItemRequestDto itemRequestDto,
                                           @RequestHeader(X_SHARER_USER_ID) Long requestorId) {
        log.info("create new item request = {} by user with id = {}", itemRequestDto, requestorId);
        return itemRequestService.createNewRequest(itemRequestDto, requestorId);
    }

    @GetMapping
    public List<ItemRequestDto> getUsersRequests(@RequestHeader(X_SHARER_USER_ID) Long requestorId) {
        log.info("get all requests of user with id = {}", requestorId);
        return itemRequestService.getUsersRequests(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsPageable(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(name = "from") Long from,
            @RequestParam(name = "size") Integer size) {
        log.info("get all requests from id = {} page size = {}", from, size);
        return itemRequestService.getAllRequestsPageable(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(X_SHARER_USER_ID) Long requestorId,
                                          @PathVariable("requestId") Long requestId) {
        log.info("get request with id = {}", requestId);
        return itemRequestService.getRequestById(requestId, requestorId);
    }
}
