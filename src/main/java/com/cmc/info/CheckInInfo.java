package com.cmc.info;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CheckInInfo {
    @Getter
    protected BookingInfo bookInfo;
    @Getter @Setter
    protected int roomNumber;
    @Getter @Setter
    protected double pricePerNight;
    public CheckInInfo(double pricePerNight, int roomNumber, BookingInfo info) {
        this.bookInfo = info;
        this.pricePerNight = pricePerNight;
        this.roomNumber = roomNumber;
    }

    public List<String> getCheckInInfo() {
        List<String> info = bookInfo.getBookInfo();
        info.add(String.valueOf(roomNumber));
        info.add(String.valueOf(pricePerNight));
        return info;
    }
}
