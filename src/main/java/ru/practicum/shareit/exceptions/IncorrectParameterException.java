package ru.practicum.shareit.exceptions;

public class IncorrectParameterException extends RuntimeException {

    private final String parameter;

    public IncorrectParameterException(String message, String parameter) {
        super(message);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
