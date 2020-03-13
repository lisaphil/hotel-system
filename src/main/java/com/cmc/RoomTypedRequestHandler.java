package com.cmc;

import java.time.LocalDate;
import java.util.*;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import lombok.Getter;
import lombok.Setter;

import static java.util.Collections.emptyList;

public class RoomTypedRequestHandler {
    public final static String fullBookingMessage = "all rooms are booked for this period of time";
    public final static String fullHotelMessage = "Sorry! Currently hotel is busy! All rooms are booked for this period of time";
    public final static String noBookingMessage = "there is no booking";

    public final static double discount = 0.7;

    @Getter
    private RoomType type;
    @Getter
    private int roomsNumber;
    @Setter
    private Map<Integer, List<BookingInfo>> bookingInformation = new HashMap<>();
    @Getter
    private ArrayList<BookingInfo> bookInfoList;

    private ArrayList<BookingInfo> guestInformation;

    private boolean isFree;
    private boolean isBooked;

    public RoomTypedRequestHandler(RoomType type) {
        this.type = type;
        this.roomsNumber = getLastRoomOfThisType() - getFirstRoomOfThisType() + 1; // TODO
        for (int i = getFirstRoomOfThisType(); i <= getLastRoomOfThisType(); i++) { // TODO remove
            this.bookingInformation.put(i, emptyList());
        }
        this.bookInfoList = new ArrayList<>();
    }

    public DayHotelInfo checkByDate(LocalDate today ) {
        DayHotelInfo dayHotelInfo = new DayHotelInfo();
        long bookedRooms = bookInfoList.stream().filter(x -> x.checkToday(today)).count();
        dayHotelInfo.setBookedRooms(Math.toIntExact(bookedRooms));
        dayHotelInfo.setBusyRooms(emptyList());//TODO
        dayHotelInfo.setFreeRooms(0);
        return dayHotelInfo;
    }
    public void addToBookInfoList(BookingInfo info) {
        bookInfoList.add(info);
    }

    public void addToGuestInformation(BookingInfo info) {
        guestInformation.add(info);
    }

    public void removeFromGuestInformation(BookingInfo info) {
        guestInformation.removeIf(x -> x.equals(info));//TODO
    }

    int getPrice() {
        return type.getPrice();
    }

    int getFirstRoomOfThisType() {
        return type.getFrom();
    }

    int getLastRoomOfThisType() {
        return type.getTo();
    }

    boolean book(BookingInfo info) {
        LocalDate from = info.getFrom();
        LocalDate to = info.getTo();
        boolean result = bookInfoList.stream().filter(x -> x.checkIntersection(from, to)).count() < roomsNumber;
        if (result) {
            addToBookInfoList(info);
        }
        return result;
    }

    double checkIn(BookingInfo info, boolean pay) throws CheckInException {
        Optional<BookingInfo> anyMatchInfo = bookInfoList.stream()
                .filter(x ->
                        x.getName().equalsIgnoreCase(info.getName())
                                && x.getTo().equals(info.getTo())
                                && x.getFrom().equals(info.getFrom()))
                .findFirst();
        if (anyMatchInfo.isPresent()) {
            BookingInfo newInfo = anyMatchInfo.get();
            if (!newInfo.isPayed()) {
                newInfo.setPayed(pay);
            }
            addToGuestInformation(newInfo);
            return newInfo.isDiscount() ? getPrice() * discount : getPrice();
        }
        throw new CheckInException(noBookingMessage, type);
    }

}
