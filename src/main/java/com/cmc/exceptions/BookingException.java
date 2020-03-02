package com.cmc.exceptions;


import com.cmc.RoomType;
import lombok.Getter;

public class BookingException extends Exception{

    @Getter
    private RoomType roomType;
    public BookingException(String message, RoomType roomType){
        super(message);
        this.roomType = roomType;
    }
}
