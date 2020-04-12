package com.cmc.hotel;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.cmc.typed.TypedNumberOfRooms;
import com.cmc.info.BookingInfo;
import com.cmc.random.RandomGenerator;
import com.cmc.statistics.RequestsStatistics;
import com.cmc.typed.RoomType;
import com.cmc.typed.RoomTypedRequestHandler;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    private LocalDateTime currentTime;
    private RandomGenerator randomGenerator;
    @Getter
    private boolean finish;
    @Setter @Getter
    private boolean pause;

    @Setter
    private int randomParam;
    private RequestsStatistics requestsStatistics;


    private HotelSystemModeling(TypedNumberOfRooms typedNumberOfRooms, int lengthInDays) {
        this.lengthInDays = lengthInDays;
        this.finish = false;
        this.hotelSystem = new Hotel(typedNumberOfRooms);
        this.pause = false;
    }

    public static HotelSystemModeling createHotelSystemModeling(TypedNumberOfRooms typedNumberOfRooms, int lengthInDays) {
        return new HotelSystemModeling(typedNumberOfRooms, lengthInDays);
    }

    public static HotelSystemModeling createHotelSystemModelingWithDefaultArgs() {
        TypedNumberOfRooms typedNumberOfRooms = new TypedNumberOfRooms(defaultNumberRooms, defaultNumberRooms, defaultNumberRooms, defaultNumberRooms, defaultNumberRooms);
        return new HotelSystemModeling(typedNumberOfRooms, defaultNumberDays);
    }

    public ImmutableList<RoomTypedRequestHandler> getRoomActionHandlers() {
        return hotelSystem.getHandlers();
    }


    private void next() {
        long deltaHours = randomGenerator.generateDeltaHours();
        currentTime = currentTime.plusHours(deltaHours);
    }

    @Override
    public void run() {
        requestsStatistics = new RequestsStatistics();
        startDate = LocalDate.now();
        finishDate = startDate.plusDays(lengthInDays);
        currentTime = startDate.atStartOfDay();
        randomGenerator = new RandomGenerator(startDate, finishDate, randomParam);
        while (currentTime.isBefore(finishDate.atStartOfDay())) {
            while (pause) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LocalDate currentTime = this.currentTime.toLocalDate();
            BookingInfo bookingInfo = randomGenerator.generateBookInfo(currentTime);
            RoomType roomType = randomGenerator.generateRoomType();
            try {
                Thread.sleep(100);
                performEvent(roomType, bookingInfo, currentTime);
            } catch (BookingException e) {
                System.out.println(e.getMessage());
                requestsStatistics.log(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (CheckInException e) {
                System.out.println(e.getMessage());
                requestsStatistics.log(e);
            }
            next();
        }
        finish = true;
    }

    private void performEvent(RoomType roomType, BookingInfo bookingInfo, LocalDate currentTime) throws BookingException, CheckInException {
        hotelSystem.checkOutAllNow(currentTime);
        hotelSystem.checkInAllNow(currentTime);
        RandomGenerator.ActionType actionType = randomGenerator.generateEvent();
        boolean pay = randomGenerator.getPayed();
        switch (actionType) {
            case CheckIn:
                bookingInfo.setFrom(currentTime);
                hotelSystem.checkIn(roomType, bookingInfo, pay);
                break;
            case Book:
                bookingInfo.setPayed(pay);
                hotelSystem.book(roomType, bookingInfo);
        }
        requestsStatistics.log(actionType);
    }

    public double getRequestsStatistics(RandomGenerator.ActionType actionType) {
        return requestsStatistics.get(actionType);
    }
}
