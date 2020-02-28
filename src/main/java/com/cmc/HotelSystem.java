package com.cmc;

import com.cmc.rooms.Suite;
import com.google.common.collect.ImmutableList;

import static com.cmc.Room.RoomType.Suite;

public class HotelSystem {
    public static int k;
    public static int maxRoomNumber;
    public static int m;

    private Room suiteRoom;
    private Room juniorSuiteRoom;
    private Room singleRoom;
    private Room doubleRoom;
    private Room doubleExtraBedRoom;

    private ImmutableList<Room> rooms = ImmutableList.of(suiteRoom, juniorSuiteRoom, singleRoom, doubleRoom, doubleExtraBedRoom);

    HotelSystem(int k, int m) {
        suiteRoom = new Room(Suite);
    }

    public void book(Room.RoomType roomType, BookingInfo info) {
        try {
            rooms.stream().filter(x -> x.getType().equals(roomType)).findFirst().get().book(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkIn(Room.RoomType roomType,  BookingInfo info) {
        try {
            rooms.stream().filter(x -> x.getType().equals(roomType)).findFirst().get().checkIn(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
