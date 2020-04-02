package com.cmc.info;
import lombok.Data;

import java.util.List;


@Data
public class HotelInfo {
    private List<Integer> busyRooms;
    private int bookedRooms;
    private int freeRooms;
}
