package com.cmc;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CheckInInfo extends BookingInfo {
    @Getter @Setter
    protected int roomNumber;
    @Getter @Setter
    protected double price;
    CheckInInfo(double price, int roomNumber, BookingInfo info) {
        super(info);
        this.price = price;
        this.roomNumber = roomNumber;
    }

    public List<String> getCheckInInfo() {
        List<String> info = this.getBookInfo();
        info.add(String.valueOf(roomNumber));
        info.add(String.valueOf(price));
        return info;
    }
}