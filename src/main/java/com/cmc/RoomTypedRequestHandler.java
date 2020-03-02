package com.cmc;

import java.util.*;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

import static java.util.Collections.emptyList;
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
            this.bookingInformation.put(i, emptyList());
        }
    }

    public void addToBookingInformation(BookingInfo info, int room) {
        List<BookingInfo> bookingInfo = bookingInformation.get(room);
        bookingInfo.add(info);
        bookingInformation.replace(room, bookingInfo);
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
        Calendar from = info.getFrom();
        Calendar to = info.getTo();
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
                    addToBookingInformation(info, entry.getKey());
                    return entry.getKey();
                }
            }
        } else {
            int roomNumber = diffInDaysPerRooms.entrySet()
                    .stream()
                    .reduce((x, y) -> x.getValue() < y.getValue() ? x : y)
                    .map(Map.Entry::getKey)
                    .get();
            addToBookingInformation(info, roomNumber);
            return roomNumber; // TODO CHECK
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
