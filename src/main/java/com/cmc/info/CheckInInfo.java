package com.cmc.info;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CheckInInfo extends BookingInfo {
    @Getter @Setter
    protected int roomNumber;
    @Getter @Setter
    protected double pricePerNight;
    public CheckInInfo(double pricePerNight, int roomNumber, BookingInfo info) {
        super(info);
        this.pricePerNight = pricePerNight;
        this.roomNumber = roomNumber;
    }

    public List<String> getCheckInInfo() {
        List<String> info = this.getBookInfo();
        info.add(String.valueOf(roomNumber));
        info.add(String.valueOf(pricePerNight));
        return info;
    }
}
