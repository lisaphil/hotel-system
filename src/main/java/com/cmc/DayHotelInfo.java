package com.cmc;
import lombok.Data;

import java.util.List;


@Data
public class DayHotelInfo {
    private List<Integer> busyRooms;
    private int bookedRooms;
    private int freeRooms;
}
