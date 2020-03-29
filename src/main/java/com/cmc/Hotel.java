package com.cmc;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.cmc.RoomType.*;
import static com.cmc.RoomTypedRequestHandler.*;
import static java.util.stream.Collectors.toList;

public class Hotel {
    public static int k;
    public static int maxRoomNumber;
    public static int m;

    private RoomTypedRequestHandler suiteRoom;
    private RoomTypedRequestHandler juniorSuiteRoom;
    private RoomTypedRequestHandler singleRoom;
    private RoomTypedRequestHandler doubleRoom;
    private RoomTypedRequestHandler doubleExtraBedRoom;

    //List<Integer> uniqueIds = (new Random()).ints(1, 10000).distinct().boxed().collect(toList());

    @Getter
    private ImmutableList<RoomTypedRequestHandler> handlers;
    public final static String goodByeMessage = "Thank You for staying with us!";
    public final static String priceMessage = "Thank You for staying with us! Your invoice is : ";


    Hotel(int k, int m) {
        suiteRoom = new RoomTypedRequestHandler(Suite);
        juniorSuiteRoom = new RoomTypedRequestHandler(JuniorSuite);
        singleRoom = new RoomTypedRequestHandler(Single);
        doubleRoom = new RoomTypedRequestHandler(RoomType.Double);
        doubleExtraBedRoom = new RoomTypedRequestHandler(DoubleExtraBed);
        List<RoomTypedRequestHandler> rooms = Arrays.asList(suiteRoom, juniorSuiteRoom, singleRoom, doubleRoom, doubleExtraBedRoom);
        rooms.sort(Comparator.comparingInt(RoomTypedRequestHandler::getPrice));
        handlers = ImmutableList.copyOf(rooms);
    }

    //TODO test
    public RoomType book(RoomType roomType, BookingInfo info) throws BookingException {
        info.setUniqueId(generateUniqueId());
        boolean bookingResult = handlers.stream()
                .filter(x -> x.getType().equals(roomType))
                .findFirst()
                .get()
                .book(info);
        if (!bookingResult) {
            Optional<RoomTypedRequestHandler> nextAvailableRoomType = handlers.stream().filter(x -> x.getPrice() > roomType.getPrice()).findFirst();
            if (nextAvailableRoomType.isPresent() && nextAvailableRoomType.get().book(info)) {
                return nextAvailableRoomType.get().getType();
            }
            throw new BookingException(fullBookingMessage, roomType);
        }
        return roomType;
    }

    private int generateUniqueId() {
        LocalDateTime today = LocalDateTime.now();
        return today.getHour() + today.getMinute() + today.getSecond() + today.getDayOfYear() + today.getNano();
    }

    //TODO test
    public RoomType checkIn(RoomType roomType, BookingInfo info, boolean pay) throws CheckInException {
        RoomTypedRequestHandler roomTypedRequestHandler =
                handlers.stream()
                        .filter(x -> x.getType().equals(roomType))
                        .findFirst()
                        .get();
        try {
            roomTypedRequestHandler.checkIn(info, pay);
            return roomType; // TODO
        } catch (CheckInException e) {
        }
        info.setPayed(pay);
        try {
            RoomType newRoomType = book(roomType, info);
            RoomTypedRequestHandler newRoomTypedRequestHandler =
                    handlers.stream()
                            .filter(x -> x.getType().equals(newRoomType))
                            .findFirst()
                            .get();
            newRoomTypedRequestHandler.checkIn(info, pay);
            return newRoomType; // TODO
        } catch (BookingException e) {
            throw new CheckInException(fullHotelMessage, roomType);
        } catch (CheckInException e) {
            throw new CheckInException("something got wrong! sorry!", roomType);
        }
    }

    public String checkOut(String name) {
        Optional<BookingInfo> optionalBookingInfo = handlers.stream()
                .map(x -> x.getBookInfoList())
                .flatMap(Collection::stream)
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst();
        if (optionalBookingInfo.isPresent()) {
            BookingInfo bookingInfo = optionalBookingInfo.get();
            return bookingInfo.isPayed() ? goodByeMessage : createCheque(bookingInfo);
        }
        return null;
    }

    private String createCheque(BookingInfo bookingInfo) {
        Optional<RoomType> roomTypeOptional = handlers.stream()
                .filter(x -> x.getBookInfoList()
                        .stream()
                        .anyMatch(y -> y.equals(bookingInfo)))
                .map(RoomTypedRequestHandler::getType)
                .findFirst();
        if (roomTypeOptional.isPresent()) {
            int price = roomTypeOptional.get().getPrice();
            double sum = bookingInfo.isDiscount() ? price * discount : price;
            return priceMessage + sum;
        }
        return "";
    }

    public List<java.lang.Double> checkInAllNow(LocalDate currentTime) {
        return handlers.stream()
                .map(x -> x.checkInToday(currentTime))
                .flatMap(Collection::stream)
                .collect(toList());
    }
}
