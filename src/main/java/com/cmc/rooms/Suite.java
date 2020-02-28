package com.cmc.rooms;

public class Suite implements Room {
    private final int price = 400;
    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void book() throws Exception {

    }

    @Override
    public void checkIn() throws Exception {

    }
}
