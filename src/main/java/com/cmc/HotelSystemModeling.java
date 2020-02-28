package com.cmc;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.cmc.Room.RoomType.Suite;

public class HotelSystemModeling {
    private int lengthInDays;
    private int numberOfRooms;
    private HotelSystem hotelSystem;
    private HotelSystemModeling(int numberOfRooms, int lengthInDays) {
        this.numberOfRooms = numberOfRooms;
        this.lengthInDays = lengthInDays;
    }

    public static HotelSystemModeling createHotelSystemModeling(int numberOfRooms, int lengthInDays){
        return new HotelSystemModeling(numberOfRooms, lengthInDays);
    }

    public void start() {
        hotelSystem.book(Suite);

    }
}
