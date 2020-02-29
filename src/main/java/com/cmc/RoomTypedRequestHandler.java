package com.cmc;

import java.util.*;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

import static java.util.stream.Collectors.toList;

public class RoomTypedRequestHandler {
    @Getter
    private RoomType type;
    @Setter
    private Map<Integer, List<BookingInfo>> bookingInformation;
    private List<BookingInfo> availabilityInformation;

    private boolean isFree;
    private boolean isBooked;

    public RoomTypedRequestHandler(RoomType type) {
        this.type = type;
        for (int i = getFirstRoomOfThisType(); i <= getLastRoomOfThisType() ; i++ ) {
            this.bookingInformation.add(createEmptyInfo(i));
        }
    }

    public BookingInfo createEmptyInfo(int roomNumber) {
        return new BookingInfo(roomNumber, new Date(), new Date(), "", true);
    }

    public void addToBookingInformation(BookingInfo info) {
        //check???
        bookingInformation = bookingInformation.stream()
                .filter(x -> (x.getRoomNumber() != info.getRoomNumber()) || x.isFreeForAllTime())
                .collect(toList());
        bookingInformation.add(info);
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

    int book(BookingInfo info) throws Exception {
        Date from = info.getFrom();
        Date to = info.getTo();
        Map<Integer, Integer> diffInDaysPerRooms = new HashMap<>();
        for (Map.Entry<Integer, List<BookingInfo>> entry : bookingInformation.entrySet()) {
            List<BookingInfo> infos = entry.getValue();
            infos.sort(Comparator.comparing(BookingInfo::getFrom));
            if (!infos.isEmpty()) {
                List<BookingInfo> bookingInfosAfter = infos.stream().filter(x -> x.getFrom().after(to)).collect(toList());
                List<BookingInfo> bookingInfosBefore = infos.stream().filter(x -> x.getTo().before(from)).collect(toList());
                if (Stream.concat(bookingInfosBefore.stream(), bookingInfosAfter.stream()).collect(toList()).equals(infos)) {
                    //choose the closest
                    int differenceInDays = bookingInfosBefore.isEmpty() ? 0 : bookingInfosBefore.get(0).getTo().compareTo(from);//TODO
                    diffInDaysPerRooms.put(entry.getKey(), differenceInDays);
                }
            }
        }
        if (diffInDaysPerRooms.isEmpty()) {
            //If nothing was added to diffInDaysPerRooms that means all rooms that have booking are not available for those days,
            // so need to take first empty room at all
            for (Map.Entry<Integer, List<BookingInfo>> entry : bookingInformation.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    return entry.getKey();
                }
            }
        } else {
            return diffInDaysPerRooms.entrySet()
                    .stream()
                    .reduce((x, y) -> x.getValue() < y.getValue() ? x : y)
                    .map(Map.Entry::getKey)
                    .get(); // TODO CHECK
        }
        throw new Exception("all rooms are booked for this period of time");
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
