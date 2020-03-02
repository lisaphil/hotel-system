package com.cmc;

import lombok.Data;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Data
public class BookingInfo {
    //private int roomNumber;
    private LocalDate from;
    private LocalDate to;
    private String name;

    public BookingInfo(LocalDate from, LocalDate to, String name) {
        //this.roomNumber = roomNumber;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public boolean checkDate(LocalDate from, LocalDate to) {
        return this.to.isBefore(from) || to.isBefore(this.from);
    }
}
