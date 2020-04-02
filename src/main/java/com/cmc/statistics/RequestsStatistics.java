package com.cmc.statistics;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.cmc.random.RandomGenerator;

public class RequestsStatistics {
    int handledBookRequest;
    int allBookRequest;
    int handledCheckInRequest;
    int allCheckInRequest;
    public RequestsStatistics() {
        this.allBookRequest = 0;
        this.handledBookRequest = 0;
        this.allBookRequest = 0;
        this.handledCheckInRequest = 0;
    }
    public void log(RandomGenerator.ActionType actionType) {
        switch (actionType) {
            case CheckIn:
                allCheckInRequest++;
                handledCheckInRequest++;
                break;
            case Book:
                allBookRequest++;
                handledBookRequest++;
                allCheckInRequest++;
                handledCheckInRequest++;
        }
    }
    public double get(RandomGenerator.ActionType actionType) {
        switch (actionType) {
            case CheckIn:
                return (double) handledCheckInRequest/allCheckInRequest*100;
            case Book:
                return (double) handledBookRequest/allBookRequest*100;
        }
        return 0.00;
    }
    public void log(CheckInException e) {
        allCheckInRequest++;
    }
    public void log(BookingException e) {
        allBookRequest++;
    }


}
