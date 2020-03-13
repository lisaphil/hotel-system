package com.cmc;

import lombok.Getter;

import java.util.Collection;
import java.util.stream.Stream;

import static com.cmc.Hotel.k;
import static com.cmc.Hotel.maxRoomNumber;

public enum RoomType {
    Suite(400, 1, k, "Suite"),
    JuniorSuite(200, k + 1, 2 * k, "Junior Suite"),
    Single(70, 2 * k + 1, 3 * k, "Single"),
    Double(100, 3 * k + 1, 4 * k, "Double"),
    DoubleExtraBed(120, 4 * k + 1, maxRoomNumber, "Double with Extra Bed");
    @Getter
    private final int price;
    @Getter
    private final int from;
    @Getter
    private final int to;
    @Getter
    private final String name;

    private final int testK = 5;

    RoomType(int price, int from, int to, String name) {
        this.price = price;
        this.from = from == 0 ? 1 : from;
        this.to = to == 0 ? testK : to;
        this.name = name;
    }

    public static String[] names() {
        return Stream.of(RoomType.values()).map(x -> x.name).toArray(String[]::new);
    }

    boolean check(int number) {
        return number > from && number < to;
    }

    @Override
    public String toString() {
        return getName();
    }
}
