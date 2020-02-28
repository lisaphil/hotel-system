package com.cmc.rooms;

import lombok.Getter;

import static com.cmc.HotelSystem.k;
import static com.cmc.HotelSystem.maxRoomNumber;

public enum RoomType {
    Suite(400, 1, k),
    JuniorSuite(200, k+1, 2*k),
    Single(70, 2*k+1, 3*k),
    Double(100, 3*k+1, 4*k ),
    DoubleWithExtraSpace(120, 4*k+1, maxRoomNumber);
    @Getter
    private final int price;
    private final int from;
    private final int to;
    RoomType(int price, int from, int to) {
        this.price = price;
        this.from = from;
        this.to = to;
    }
    boolean check(int number) {
        return number > from && number < to;
    }
}