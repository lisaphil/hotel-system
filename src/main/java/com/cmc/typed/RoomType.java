package com.cmc.typed;

import lombok.Getter;

import java.util.stream.Stream;

import static com.cmc.hotel.Hotel.*;

public enum RoomType {
    Suite(400, 1, suiteInt - 1, "Suite"),
    JuniorSuite(200, suiteInt + 1, suiteInt + juniourInt, "Junior Suite"),
    Single(70, suiteInt + juniourInt + 1, suiteInt + juniourInt+ singleInt, "Single"),
    Double(100, suiteInt + juniourInt+ singleInt  + 1, suiteInt + juniourInt+ singleInt + doubleInt, "Double"),
    DoubleExtraBed(120, suiteInt + juniourInt+ singleInt + doubleInt + 1, suiteInt + juniourInt+ singleInt + doubleInt + doubleWithExtra, "Double with Extra Bed");
    @Getter
    private final int pricePerNight;
    @Getter
    private final int from;
    @Getter
    private final int to;
    @Getter
    private final String name;

    private final int testK = 5;

    RoomType(int pricePerNight, int from, int to, String name) {
        this.pricePerNight = pricePerNight;
        this.from = from <= 0 ? 1 : from;
        this.to = to <= 0 ? testK : to;
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
