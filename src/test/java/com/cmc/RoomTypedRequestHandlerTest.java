package com.cmc;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoomTypedRequestHandlerTest extends TestCase {

    @Test
    public void testBook() {
        //Given
        RoomTypedRequestHandler roomTypedRequestHandler = new RoomTypedRequestHandler(RoomType.Suite);

        //When
        try {
            Date from = new Date();
            Date to = new Date();
            roomTypedRequestHandler.book(new BookingInfo(1,
                    new GregorianCalendar(2017, Calendar.JANUARY , 25),
                    new GregorianCalendar(2017, Calendar.JANUARY , 30),
                    "Lisa" ));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        //Then
    }
}