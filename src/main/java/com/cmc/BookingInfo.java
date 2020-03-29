package com.cmc;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Data
public class BookingInfo {
    protected int uniqueId;
    protected boolean discount;
    protected boolean isPayed;
    protected LocalDate from;
    protected LocalDate to;
    protected String name;

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

    protected BookingInfo(BookingInfo info) {
        this.uniqueId = info.uniqueId;
        this.isPayed = info.isPayed;
        this.discount = info.discount;
        this.from = info.from;
        this.to = info.to;
        this.name = info.name;
    }

    public List<String> getBookInfo() {
        return Lists.newArrayList( String.valueOf(uniqueId), String.valueOf(discount), String.valueOf(isPayed), from.toString(), to.toString(), name);
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

    public boolean isBookingToday(LocalDate currentTime) {
        return from.isEqual(currentTime);
    }
}
