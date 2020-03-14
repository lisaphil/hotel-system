package com.cmc.exceptions;

import com.cmc.RoomType;
import lombok.Getter;

public class WrongInputException extends Exception{

    @Getter
    private ArgumentType type;
    @Getter
    private int arg;
    public WrongInputException(String message, ArgumentType type, int arg){
        super(message);
        this.type = type;
        this.arg = arg;
    }
}