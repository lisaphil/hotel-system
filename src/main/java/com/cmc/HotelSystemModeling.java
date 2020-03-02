package com.cmc;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.cmc.RoomType.Suite;

public class HotelSystemModeling {
    private int lengthInDays;
    private int numberOfRooms;
    private Hotel hotelSystem;
    private HotelSystemModeling(int numberOfRooms, int lengthInDays) {
        this.numberOfRooms = numberOfRooms;
        this.lengthInDays = lengthInDays;
    }

    public static HotelSystemModeling createHotelSystemModeling(int numberOfRooms, int lengthInDays){
        return new HotelSystemModeling(numberOfRooms, lengthInDays);
    }

    public void start() {
        hotelSystem.book(Suite,
                new BookingInfo(1, new GregorianCalendar(2017, Calendar.JANUARY , 25),
                        new GregorianCalendar(2017, Calendar.JANUARY , 30),
                        "lisa"));
    }
}
