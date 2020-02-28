package com.cmc;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static com.cmc.HotelSystem.k;
import static com.cmc.HotelSystem.maxRoomNumber;
import static java.util.Collections.emptyList;

public class Room {
    public enum RoomType {
        Suite(400, 1, k),
        JuniorSuite(200, k+1, 2*k),
        Single(70, 2*k+1, 3*k),
        Double(100, 3*k+1, 4*k ),
        DoubleExtraBed (120, 4*k+1, maxRoomNumber);
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
    @Getter
    private RoomType type;
    @Setter
    private List<BookingInfo> bookingInformation;
    private List<BookingInfo> availabilityInformation;

    private boolean isFree;
    private boolean isBooked;
    public Room(RoomType type) {
        this.type = type;
        this.bookingInformation = emptyList();
    }
    int getPrice() {
        return type.getPrice();
    }
    void book(BookingInfo info) throws Exception {
        if (isBooked) {
            throw new Exception("already booked");
        } else if (isFree) {
            throw new Exception("not free");
        } else {
            this.isBooked = true;
        }
    }
    void checkIn(BookingInfo info) throws Exception {
        if (isBooked) {
            throw new Exception("already booked");
        } else if (!isFree) {
            throw new Exception("not free");
        } else {
            this.isFree = false;
        }
    }
}
