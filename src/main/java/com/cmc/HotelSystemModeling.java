package com.cmc;

import com.cmc.exceptions.ArgumentType;
import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.WrongInputException;
import com.google.common.collect.ImmutableList;

import java.time.LocalDate;
import java.time.Month;

import static com.cmc.RoomType.Suite;

public class HotelSystemModeling {
    private int lengthInDays;
    public static final int defaultK = 5; // each rooms Number
    public static final int defaultM = 25; // number of days


    private int numberOfRooms;
    private Hotel hotelSystem;

    private HotelSystemModeling(int numberOfRooms, int lengthInDays) {
        this.numberOfRooms = numberOfRooms;
        this.lengthInDays = lengthInDays;
        this.hotelSystem = new Hotel(numberOfRooms, lengthInDays);
    }

    public static HotelSystemModeling createHotelSystemModeling(int numberOfRooms, int lengthInDays) {
        return new HotelSystemModeling(numberOfRooms, lengthInDays);
    }
    public static HotelSystemModeling createHotelSystemModelingWithDefaultArgs() {
        return new HotelSystemModeling(defaultK, defaultM);
    }

    public ImmutableList<RoomTypedRequestHandler> getRoomActionHadlers() {
        return hotelSystem.getHandlers();
    }

    public void start() {
        LocalDate from = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 17);
        try {
            hotelSystem.book(Suite,
                    new BookingInfo(from,
                            to,
                            "lisa"));
        } catch (BookingException e) {
            e.printStackTrace();
        }
    }
}
