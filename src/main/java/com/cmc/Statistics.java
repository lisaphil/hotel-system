package com.cmc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;

public class Statistics {
    @Getter
    private RoomType room;
    private double busynessSum;
    private double maxBusyness;
    private LocalDate whenMaxBusyness;
    private double minBusyness;
    private LocalDate whenMinBusyness;
    private LocalDate lastUpdate;
    private int roomsNumber;
    private int numberOfDays;
    @Getter
    private double avBusyness;

    Statistics(RoomType room) {
        this.room = room;
        this.lastUpdate = LocalDate.now();
        this.roomsNumber = room.getTo() - room.getFrom() + 1;
        this.numberOfDays = 0;

    }
    public HotelInfo getAll() {
        return null;
    }

    public Object log(LocalDate currentTime, ArrayList<CheckInInfo> checkInInfoList) {
        if (lastUpdate.isBefore(currentTime)) {
            double busyness = checkInInfoList.size() / roomsNumber;
            busynessSum += busyness;
            numberOfDays ++;
        }
        return null;
    }

    public void countFinal(ArrayList<CheckInInfo> checkInInfoList) {
        double busyness = (double)checkInInfoList.size() / roomsNumber;
        busynessSum += busyness;
        numberOfDays ++;
        avBusyness = busyness/numberOfDays;
    }



}
