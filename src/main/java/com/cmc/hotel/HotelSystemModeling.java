package com.cmc.hotel;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.cmc.info.BookingInfo;
import com.cmc.random.RandomGenerator;
import com.cmc.typed.RoomType;
import com.cmc.typed.RoomTypedRequestHandler;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HotelSystemModeling implements Runnable {
    private int lengthInDays;
    public static final int defaultNumberRooms = 5; // each rooms Number
    public static final int defaultNumberDays = 25; // number of days


    private int numberOfRooms;
    private Hotel hotelSystem;
    private LocalDate startDate;
    private LocalDate finishDate;
    private LocalDateTime currentTime;
    private long deltaHours = 2; // TODO make random
    private RandomGenerator randomGenerator;
    @Getter
    private boolean finish;


    private HotelSystemModeling(int suiteInt, int juniourInt, int singleInt, int doubleInt, int doubleWithExtra, int lengthInDays) {
        this.lengthInDays = lengthInDays;
        this.finish = false;
        this.hotelSystem = new Hotel(suiteInt, juniourInt, singleInt, doubleInt, doubleWithExtra);
    }

    public static HotelSystemModeling createHotelSystemModeling(int suiteInt, int juniourInt, int singleInt, int doubleInt, int doubleWithExtra, int lengthInDays) {
        return new HotelSystemModeling(suiteInt, juniourInt, singleInt, doubleInt, doubleWithExtra, lengthInDays);
    }

    public static HotelSystemModeling createHotelSystemModelingWithDefaultArgs() {
        return new HotelSystemModeling(defaultNumberRooms, defaultNumberRooms, defaultNumberRooms, defaultNumberRooms, defaultNumberRooms, defaultNumberDays);
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
        hotelSystem.checkOutAllNow(currentTime);
        hotelSystem.checkInAllNow(currentTime);
        RandomGenerator.ActionType actionType = randomGenerator.generateEvent();
        boolean pay = true;
        switch (actionType) {
            case CheckIn:
                bookingInfo.setFrom(currentTime);
                hotelSystem.checkIn(roomType, bookingInfo, pay);
                break;
            case Book:
                hotelSystem.book(roomType, bookingInfo);
        }
    }
}
