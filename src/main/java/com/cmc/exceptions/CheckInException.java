package com.cmc.exceptions;

import com.cmc.typed.RoomType;
import lombok.Getter;

public class CheckInException extends Exception{

    @Getter
    private RoomType roomType;
    public CheckInException(String message, RoomType roomType){
        super(message);
        this.roomType = roomType;
    }
}
