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
        roomTypedRequestHandler.setRoomsNumber(5);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        boolean result = roomTypedRequestHandler.book(lisa);
        boolean expectedResult = true;

        //Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testBookWithSeveralBooks() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        roomTypedRequestHandler.setRoomsNumber(5);

        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookInfoList(sasha);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 18);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookInfoList(anya);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 25);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo(fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookInfoList(gadel);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 21);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 23);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        boolean result = roomTypedRequestHandler.book(lisa);
        boolean expectedResult = true;

        //Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testBookWithSeveralBooksAnotherRoom() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        roomTypedRequestHandler.setRoomsNumber(5);

        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookInfoList(sasha);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 18);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookInfoList(anya);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 25);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo(fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookInfoList(gadel);

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 14);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 23);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        boolean result = roomTypedRequestHandler.book(lisa);
        boolean expectedResult = true;

        //Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testBookAllFull() throws Exception {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);
        roomTypedRequestHandler.setRoomsNumber(5);

        LocalDate fromSasha = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toSasha = LocalDate.of(2014, Month.JUNE, 17);
        BookingInfo sasha = new BookingInfo(fromSasha,
                toSasha,
                "Sasha");
        roomTypedRequestHandler.addToBookInfoList(sasha);

        LocalDate fromAnya = LocalDate.of(2014, Month.JUNE, 10);
        LocalDate toAnya = LocalDate.of(2014, Month.JUNE, 20);
        BookingInfo anya = new BookingInfo(fromAnya,
                toAnya,
                "Anya");
        roomTypedRequestHandler.addToBookInfoList(anya);

        LocalDate fromGadel = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toGadel = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo gadel = new BookingInfo(fromGadel,
                toGadel,
                "Gadel");
        roomTypedRequestHandler.addToBookInfoList(gadel);

        LocalDate fromSergey = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toSergey = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo sergey = new BookingInfo(fromSergey,
                toSergey,
                "Sergey");
        roomTypedRequestHandler.addToBookInfoList(gadel);

        LocalDate fromDima = LocalDate.of(2014, Month.JUNE, 15);
        LocalDate toDima = LocalDate.of(2014, Month.JUNE, 26);
        BookingInfo dima = new BookingInfo(fromDima,
                toDima,
                "Dima");
        roomTypedRequestHandler.addToBookInfoList(gadel);

        String message = "";

        //When
        LocalDate from = LocalDate.of(2014, Month.JUNE, 14);
        LocalDate to = LocalDate.of(2014, Month.JUNE, 18);
        BookingInfo lisa = new BookingInfo(from,
                to,
                "Lisa");
        boolean result = roomTypedRequestHandler.book(lisa);
        boolean expectedResult = false;

        //Then
        Assert.assertEquals(expectedResult, result);
    }
}
