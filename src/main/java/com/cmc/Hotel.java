package com.cmc;

import com.google.common.collect.ImmutableList;

import static com.cmc.RoomType.Suite;

public class Hotel {
    public static int k;
    public static int maxRoomNumber;
    public static int m;

    private RoomTypedRequestHandler suiteRoom;
    private RoomTypedRequestHandler juniorSuiteRoom;
    private RoomTypedRequestHandler singleRoom;
    private RoomTypedRequestHandler doubleRoom;
    private RoomTypedRequestHandler doubleExtraBedRoom;

    private ImmutableList<RoomTypedRequestHandler> rooms = ImmutableList.of(suiteRoom, juniorSuiteRoom, singleRoom, doubleRoom, doubleExtraBedRoom);

    Hotel(int k, int m) {
        suiteRoom = new RoomTypedRequestHandler(Suite);
    }

    public void book(RoomType roomType, BookingInfo info) {
        try {
            rooms.stream()
                    .filter(x -> x.getType().equals(roomType))
                    .findFirst()
                    .get()
                    .book(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIn(RoomType roomType, BookingInfo info) {
        try {
            rooms.stream().filter(x -> x.getType().equals(roomType)).findFirst().get().checkIn(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
