package com.cmc;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static com.cmc.RoomTypedRequestHandler.fullBookingMessage;

public class RoomTypedRequestHandlerTest extends TestCase {

    @Test
    public void testBook() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo lisa = new BookingInfo( from,
                to,
                "Lisa");
        int roomNumber = roomTypedRequestHandler.book(lisa);
        int expectedRoomNumber = 1;

        //Then
        Assert.assertEquals(expectedRoomNumber, roomNumber);
    }

    @Test
    public void testBookWithSeveralBooks() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookingInformation(sasha, 1);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 18);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookingInformation(anya, 1);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 25);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo( fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookingInformation(gadel, 1);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 21);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 23);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        int roomNumber = roomTypedRequestHandler.book(lisa);
        int expectedRoomNumber = 1;

        //Then
        Assert.assertEquals(expectedRoomNumber, roomNumber);
    }

    @Test
    public void testBookWithSeveralBooksAnotherRoom() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookingInformation(sasha, 1);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 18);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookingInformation(anya, 1);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 25);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo(fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookingInformation(gadel, 1);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 14);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 23);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        int roomNumber = roomTypedRequestHandler.book(lisa);
        int expectedRoomNumber = 2;

        //Then
        Assert.assertEquals(expectedRoomNumber, roomNumber);
    }

    @Test
    public void testBookAllFull() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookingInformation(sasha, 1);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookingInformation(anya, 2);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo( fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookingInformation(gadel, 3);

        LocalDate fromSergey = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toSergey = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo sergey = new BookingInfo(fromSergey,
                toSergey,
                "Sergey");
        roomTypedRequestHandler.addToBookingInformation(gadel, 4);

        LocalDate fromDima = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toDima = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo dima = new BookingInfo(fromDima,
                toDima,
                "Dima");
        roomTypedRequestHandler.addToBookingInformation(gadel, 5);

        String message = "";

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 14);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 18);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        try {
            int roomNumber = roomTypedRequestHandler.book(lisa);

        } catch (Exception e) {
            message = e.getMessage();
        }

        //Then
        Assert.assertEquals(fullBookingMessage, message);
    }

    @Test
    public void testBookWithSeveralAvailableRooms() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 15);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookingInformation(sasha, 1);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 13);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookingInformation(anya, 1);


        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 21);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 23);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        int roomNumber = roomTypedRequestHandler.book(lisa);
        int expectedRoomNumber = 1;

        //Then
        Assert.assertEquals(expectedRoomNumber, roomNumber);
    }
}
