package com.cmc;

import java.time.LocalDate;
import java.time.Month;
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
        LocalDate from = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 17);
        hotelSystem.book(Suite,
                new BookingInfo( from,
                        to,
                        "lisa"));
    }
}
