package com.cmc;

import com.cmc.exceptions.BookingException;
import com.cmc.random.RandomGenerator;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.TimeUnit;

import static com.cmc.RoomType.Suite;

public class HotelSystemModeling implements Runnable{
    private int lengthInDays;
    public static final int defaultK = 5; // each rooms Number
    public static final int defaultM = 25; // number of days


    private int numberOfRooms;
    private Hotel hotelSystem;
    private LocalDate startDate;
    private LocalDate finishDate;
    private LocalDateTime currentTime;
    private long deltaHours = 2; // TODO make random
    private RandomGenerator randomGenerator;
    @Getter
    private boolean finish;


    private HotelSystemModeling(int numberOfRooms, int lengthInDays) {
        this.numberOfRooms = numberOfRooms;
        this.lengthInDays = lengthInDays;
        this.finish = false;
        this.hotelSystem = new Hotel(numberOfRooms, lengthInDays);
    }

    public static HotelSystemModeling createHotelSystemModeling(int numberOfRooms, int lengthInDays) {
        return new HotelSystemModeling(numberOfRooms, lengthInDays);
    }
    public static HotelSystemModeling createHotelSystemModelingWithDefaultArgs() {
        return new HotelSystemModeling(defaultK, defaultM);
    }

    public ImmutableList<RoomTypedRequestHandler> getRoomActionHandlers() {
        return hotelSystem.getHandlers();
    }


    private void next() {
        currentTime = currentTime.plusHours(deltaHours);
    }

    @Override
    public void run() {
        startDate = LocalDate.now();
        finishDate = startDate.plusDays(lengthInDays);
        currentTime = startDate.atStartOfDay();
        randomGenerator = new RandomGenerator(startDate, finishDate);
        while (currentTime.isBefore(finishDate.atStartOfDay())) {
            BookingInfo bookingInfo = randomGenerator.generateBookInfo(currentTime.toLocalDate());
            RoomType roomType = randomGenerator.generateRoomType();
            try {
                Thread.sleep(30);
                hotelSystem.book(roomType, bookingInfo);
            } catch (BookingException e) {
                e.getMessage(); // log this?
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            next();
        }
        finish = true;
    }
}
