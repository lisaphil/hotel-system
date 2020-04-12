package com.cmc.statistics;

import com.cmc.info.CheckInInfo;
import com.cmc.typed.RoomType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;

public class AverageStatistics {
    @Getter
    private RoomType room;
    private double busynessSum;
    @Getter
    private double maxBusyness;
    @Getter
    private LocalDate whenMaxBusyness;
    @Getter
    private double minBusyness;
    @Getter
    private LocalDate whenMinBusyness;
    private LocalDate lastUpdate;
    private int roomsNumber;
    private int numberOfDays;
    @Getter
    private double avBusyness;

    public AverageStatistics(RoomType room) {
        this.room = room;
        this.lastUpdate = LocalDate.now();
        this.roomsNumber = room.getTo() - room.getFrom() + 1;
        this.numberOfDays = 0;
        this.maxBusyness = 0;
        this.minBusyness = 1;
    }

    public Object log(LocalDate currentTime, ArrayList<CheckInInfo> checkInInfoList) {
        if (lastUpdate.isBefore(currentTime)) {
            double busyness = (double)checkInInfoList.size() / roomsNumber;
            if (maxBusyness < busyness) {
                maxBusyness = busyness;
                whenMaxBusyness = currentTime.minusDays(1);
            }
            if (minBusyness > busyness) {
                minBusyness = busyness;
                whenMinBusyness = currentTime.minusDays(1);
            }
            busynessSum += busyness;
            numberOfDays ++;
            lastUpdate = currentTime;
        }
        return null;
    }

    public void countFinal(ArrayList<CheckInInfo> checkInInfoList) {
        double busyness = (double)checkInInfoList.size() / roomsNumber;
        busynessSum += busyness;
        numberOfDays ++;
        avBusyness = busynessSum/numberOfDays;
    }
}
