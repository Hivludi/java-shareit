package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    //Обработка множественных ошибок валидации
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MultipleErrorsResponse collectValidationErrorsAndLog(final MethodArgumentNotValidException e) {

        List list = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        for (Object error : list) {
            log.warn("Ошибка валидации: {}", error);
        }

        return new MultipleErrorsResponse(list);
    }

    //Обработка единичных ошибок валидации
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final IncorrectParameterException e) {
        log.warn("Ошибка с полем \"{}\": {}", e.getParameter(), e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\": %s", e.getParameter(), e.getMessage()));
    }

    //Обработка ошибок поиска объектов
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.warn("Ошибка: {}", e.getMessage());
        return new ErrorResponse(String.format("%s", e.getMessage()));
    }

    //Обработка ошибок создания объектов
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleObjectAlreadyExistsException(final ObjectAlreadyExistsException e) {
        log.warn("Ошибка: {}", e.getMessage());
        return new ErrorResponse(String.format("%s", e.getMessage()));
    }

    //Обработка ошибок прав доступа
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleWrongUserException(final WrongUserException e) {
        log.warn("Ошибка: {}", e.getMessage());
        return new ErrorResponse(String.format("%s", e.getMessage()));
    }

    private static class ErrorResponse {
        String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    private static class MultipleErrorsResponse {
        List<String> errorsList;

        public MultipleErrorsResponse(List<String> errorsList) {
            this.errorsList = errorsList;
        }

        public List<String> getErrorsList() {
            return errorsList;
        }
    }
}