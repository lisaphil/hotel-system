package com.cmc;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Getter
    private ArrayList<CheckInInfo> guestInformation = new ArrayList<>();

    private boolean isFree;
    private boolean isBooked;

    public RoomTypedRequestHandler(RoomType type) {
        this.type = type;
        this.roomsNumber = getLastRoomOfThisType() - getFirstRoomOfThisType() + 1; // TODO
        this.bookInfoList = new ArrayList<>();
    }

    public DayHotelInfo checkByDate(LocalDate today) {
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

    public void addToGuestInformation(BookingInfo info, int roomNumber, double price) {
        CheckInInfo checkInInfo = new CheckInInfo(price, roomNumber, info);
        guestInformation.add(checkInInfo);
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
        long bookedRooms = bookInfoList.stream().filter(x -> x.checkIntersection(from, to)).count();
        long checkedInRooms = guestInformation.stream().filter(x -> x.checkIntersection(from, to)).count();
        boolean result = (bookedRooms + checkedInRooms) < roomsNumber;
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
            LocalDate from = newInfo.getFrom();
            LocalDate to = newInfo.getTo();
            if (!newInfo.isPayed()) {
                newInfo.setPayed(pay);
            }
            int firstRoomOfThisType = getFirstRoomOfThisType();
            int lastRoomOfThisType = getLastRoomOfThisType();
            Integer roomNumber = guestInformation.stream()
                    .filter(x -> x.checkIntersection(from, to))
                    .map(CheckInInfo::getRoomNumber)
                    .reduce(firstRoomOfThisType, (x, y) -> x==y ? ++x : x);
            if (roomNumber > lastRoomOfThisType) {
                throw new CheckInException("system mistake???", type);
            }
            double price = newInfo.isDiscount() ? getPrice() * discount : getPrice();
            bookInfoList.removeIf(x -> x.uniqueId == info.uniqueId);
            addToGuestInformation(newInfo, roomNumber, price);
            return price;
        }
        throw new CheckInException(noBookingMessage, type);
    }

    public List<Double> checkInToday(LocalDate currentTime) {
        boolean pay = true;
        List<BookingInfo> bookingInfoList = bookInfoList.stream()
                .filter(x -> x.isBookingToday(currentTime))
                .collect(Collectors.toList());
        return bookingInfoList.size() > 0 ? bookingInfoList.stream()
                .map(x -> {
                    try {
                        return this.checkIn(x, pay);
                    } catch (CheckInException e) {
                        System.err.println(e.getMessage());
                    }
                    return 0.0;
                })
                .filter(x -> x != 0)
                .collect(Collectors.toList())
                : emptyList();
    }
}
