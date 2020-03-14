package com.cmc.exceptions;

import lombok.Getter;

public enum ArgumentType {

    RoomNumber(20, 30, "number of rooms"),
    DaysNumber(12, 30, "number of days");

    private final int min;
    private final int max;
    @Getter
    private String messageToRender;

    ArgumentType(int min, int max, String messageToRender) {
        this.min = min;
        this.max = max;
        this.messageToRender = messageToRender;
    }
    public void check(int argValue) throws WrongInputException {
        if (argValue > max || argValue< min) {
            throw new WrongInputException("", this, argValue);
        }
    }
}
