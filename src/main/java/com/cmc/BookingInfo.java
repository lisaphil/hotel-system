package com.cmc;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Data
public class BookingInfo {
    //private int roomNumber;
    private boolean discount;
    private boolean isPayed;
    private LocalDate from;
    private LocalDate to;
    private String name;

    public BookingInfo(LocalDate from, LocalDate to, String name, boolean isPayed) {
        this.isPayed = isPayed;
        this.discount = false;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public BookingInfo(LocalDate from, LocalDate to, String name) {
        this.discount = false;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public List<String> getBookInfo() {
        return Lists.newArrayList( String.valueOf(discount), String.valueOf(isPayed), from.toString(), to.toString(), name);
    }
    public boolean checkDate(LocalDate from, LocalDate to) {
        return this.to.isBefore(from) || to.isBefore(this.from);
    }

    public boolean checkIntersection(LocalDate from, LocalDate to) {
        return !this.to.isBefore(from) && !to.isBefore(this.from);
    }

    public boolean checkToday(LocalDate today) {
        return from.isBefore(today) && to.isAfter(today);
    }
}
