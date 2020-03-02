package com.cmc;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class BookingInfo {
    private int roomNumber;
    private Calendar from;
    private Calendar to;
    private String name;

    public BookingInfo(int roomNumber, Calendar from, Calendar to, String name) {
        this.roomNumber = roomNumber;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public boolean checkDate(Calendar from, Calendar to) {
        return this.to.before(from) || to.before(this.from);
    }
}
