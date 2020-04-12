package com.cmc.info;

import com.google.common.collect.Lists;
import lombok.Data;

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

    public BookingInfo(LocalDate from, LocalDate to, String name) {
        this.discount = false;
        this.from = from;
        this.to = to;
        this.name = name;
    }

    public List<String> getBookInfo() {
        return Lists.newArrayList( String.valueOf(uniqueId), String.valueOf(discount), String.valueOf(isPayed), from.toString(), to.toString(), name);
    }

    public boolean checkIntersection(LocalDate from, LocalDate to) {
        return !this.to.isBefore(from) && !to.isBefore(this.from);
    }

    public boolean isBookingToday(LocalDate currentTime) {
        return from.isEqual(currentTime);
    }
}
