package com.cmc.hotel;

import com.cmc.exceptions.BookingException;
import com.cmc.exceptions.CheckInException;
import com.cmc.info.BookingInfo;
import com.cmc.typed.RoomType;
import com.cmc.typed.RoomTypedRequestHandler;
import com.cmc.typed.TypedNumberOfRooms;
import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.cmc.typed.RoomType.*;
import static com.cmc.typed.RoomTypedRequestHandler.*;
import static java.util.stream.Collectors.toList;

public class Hotel {
    public static int suiteInt;
    public static int juniourInt;
    public static int singleInt;
    public static int doubleInt;
    public static int doubleWithExtra;
    public static int maxRoomNumber;

    private RoomTypedRequestHandler suiteRoom;
    private RoomTypedRequestHandler juniorSuiteRoom;
    private RoomTypedRequestHandler singleRoom;
    private RoomTypedRequestHandler doubleRoom;
    private RoomTypedRequestHandler doubleExtraBedRoom;

    @Getter
    private ImmutableList<RoomTypedRequestHandler> handlers;
    public final static String goodByeMessage = "Thank You for staying with us!";
    public final static String priceMessage = "Thank You for staying with us! Your invoice is : ";


    Hotel(TypedNumberOfRooms typedNumberOfRooms) {
        Hotel.suiteInt = typedNumberOfRooms.getSuiteInt();
        Hotel.juniourInt = typedNumberOfRooms.getJuniourInt();
        Hotel.singleInt = typedNumberOfRooms.getSingleInt();
        Hotel.doubleInt = typedNumberOfRooms.getDoubleInt();
        Hotel.doubleWithExtra = typedNumberOfRooms.getDoubleWithExtra();

        suiteRoom = new RoomTypedRequestHandler(Suite);
        juniorSuiteRoom = new RoomTypedRequestHandler(JuniorSuite);
        singleRoom = new RoomTypedRequestHandler(Single);
        doubleRoom = new RoomTypedRequestHandler(RoomType.Double);
        doubleExtraBedRoom = new RoomTypedRequestHandler(DoubleExtraBed);
        List<RoomTypedRequestHandler> rooms = Arrays.asList(suiteRoom, juniorSuiteRoom, singleRoom, doubleRoom, doubleExtraBedRoom);
        rooms.sort(Comparator.comparingInt(RoomTypedRequestHandler::getPrice));
        handlers = ImmutableList.copyOf(rooms);
    }

    public RoomType book(RoomType roomType, BookingInfo info) throws BookingException {
        info.setUniqueId(generateUniqueId());
        boolean bookingResult = handlers.stream()
                .filter(x -> x.getType().equals(roomType))
                .findFirst()
                .get()
                .book(info);
        if (!bookingResult) {
            Optional<RoomTypedRequestHandler> nextAvailableRoomType = handlers.stream().filter(x -> x.getPrice() > roomType.getPricePerNight()).findFirst();
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

    public RoomType checkIn(RoomType roomType, BookingInfo info, boolean pay) throws CheckInException {
        RoomTypedRequestHandler roomTypedRequestHandler =
                handlers.stream()
                        .filter(x -> x.getType().equals(roomType))
                        .findFirst()
                        .get();
        try {
            roomTypedRequestHandler.checkIn(info, pay);
            return roomType;
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
            return newRoomType;
        } catch (BookingException e) {
            throw new CheckInException(fullHotelMessage, roomType);
        } catch (CheckInException e) {
            throw new CheckInException("something got wrong! sorry!", roomType);
        }
    }

    public List<java.lang.Double> checkInAllNow(LocalDate currentTime) {
        log(currentTime);
        return handlers.stream()
                .map(x -> x.checkInToday(currentTime))
                .flatMap(Collection::stream)
                .collect(toList());
    }

    public void log(LocalDate currentTime) {
        handlers.stream().map(x-> x.log(currentTime)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<java.lang.String> checkOutAllNow(LocalDate currentTime) {
        return handlers.stream()
               .map(x-> x.checkOutAllNow(currentTime))
                .flatMap(Collection::parallelStream)
                .collect(toList());
    }
}
