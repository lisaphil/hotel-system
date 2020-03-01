package com.cmc;

import lombok.Data;

import java.util.Date;

@Data
public class BookingInfo {
    private int roomNumber;
    private Date from;
    private Date to;
    private String name;

    public BookingInfo(int roomNumber, Date from, Date to, String name) {
        this.roomNumber = roomNumber;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public boolean checkDate(Date from, Date to) {
        return this.to.before(from) || to.before(this.from);
    }
}
