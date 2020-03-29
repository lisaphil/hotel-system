package com.cmc;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.cmc.random.RandomGenerator;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
            LocalDate currentTime = this.currentTime.toLocalDate();
            BookingInfo bookingInfo = randomGenerator.generateBookInfo(currentTime);
            RoomType roomType = randomGenerator.generateRoomType();
            try {
                Thread.sleep(50);
                performEvent(roomType, bookingInfo, currentTime);
            } catch (BookingException e) {
                e.getMessage(); // log this?
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (CheckInException e) {
                e.getMessage(); // log this?
            }
            next();
        }
        finish = true;
    }

    private void performEvent(RoomType roomType, BookingInfo bookingInfo, LocalDate currentTime) throws BookingException, CheckInException {
        hotelSystem.checkInAllNow(currentTime);
        RandomGenerator.ActionType actionType = randomGenerator.generateEvent();
        boolean pay = true; //TODO
        switch (actionType) {
            case CheckIn:
                //hotelSystem.checkIn(roomType, bookingInfo, pay);
                break;
            case Book:
                hotelSystem.book(roomType, bookingInfo);
        }
    }
}
