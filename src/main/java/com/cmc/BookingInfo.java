package com.cmc;

import lombok.Data;

import java.util.Date;

@Data
public class BookingInfo {
    private int roomNumber;
    private Date from;
    private Date to;
    private String name;

}
