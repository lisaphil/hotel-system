package com.cmc;

import lombok.Getter;

import static com.cmc.Hotel.k;
import static com.cmc.Hotel.maxRoomNumber;

public enum RoomType {
    Suite(400, 1, k),
    JuniorSuite(200, k + 1, 2 * k),
    Single(70, 2 * k + 1, 3 * k),
    Double(100, 3 * k + 1, 4 * k),
    DoubleExtraBed(120, 4 * k + 1, maxRoomNumber);
    @Getter
    private final int price;
    @Getter
    private final int from;
    @Getter
    private final int to;

    private final int testK = 5;

    RoomType(int price, int from, int to) {
        this.price = price;
        this.from = from == 0 ? 1 : from;
        this.to = to == 0 ? testK : to;
    }

    boolean check(int number) {
        return number > from && number < to;
    }
}
