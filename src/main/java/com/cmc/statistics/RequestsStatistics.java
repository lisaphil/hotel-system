package com.cmc.statistics;

import com.cmc.random.RandomGenerator;
import com.cmc.typed.RoomType;

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

    public void log(RandomGenerator.ActionType actionType, RoomType returnedRoomType) {
        if (returnedRoomType != null) {
            if (actionType.equals(RandomGenerator.ActionType.Book)) {
                allBookRequest++;
                handledBookRequest ++;
            }
            allCheckInRequest++;
            handledCheckInRequest ++;
        } else {
            if (actionType.equals(RandomGenerator.ActionType.Book)) {
                allBookRequest++;
            } else {
                allCheckInRequest++;
            }
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
}
