package com.cmc.rooms;

public interface Room {
    int getPrice();
    void book () throws Exception;
    void checkIn () throws Exception;
}
