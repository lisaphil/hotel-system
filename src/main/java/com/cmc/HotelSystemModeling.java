package com.cmc;

import com.cmc.exceptions.BookingException;
import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.time.Month;

import static com.cmc.RoomType.Suite;

public class HotelSystemModeling {
    private int lengthInDays;
    private int numberOfRooms;
    private Hotel hotelSystem;
    private HotelSystemModeling(int numberOfRooms, int lengthInDays) {
        this.numberOfRooms = numberOfRooms;
        this.lengthInDays = lengthInDays;
        this.hotelSystem = new Hotel(numberOfRooms,lengthInDays);
    }

    public static HotelSystemModeling createHotelSystemModeling(int numberOfRooms, int lengthInDays){
        return new HotelSystemModeling(numberOfRooms, lengthInDays);
    }

    public ImmutableList<RoomTypedRequestHandler> getRoomActionHadlers() {
        return hotelSystem.getHandlers();
    }
    public void start() {
        LocalDate from = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 17);
        try {
            hotelSystem.book(Suite,
                    new BookingInfo( from,
                            to,
                            "lisa"));
        } catch (BookingException e) {
            e.printStackTrace();
        }
    }
}
