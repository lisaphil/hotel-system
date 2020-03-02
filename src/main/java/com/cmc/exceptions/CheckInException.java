package com.cmc.exceptions;

import com.cmc.RoomType;
import lombok.Getter;

public class CheckInException extends Exception{

    @Getter
    private RoomType roomType;
    public CheckInException(String message, RoomType roomType){
        super(message);
        this.roomType = roomType;
    }
}
