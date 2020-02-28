package com.cmc;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.cmc.HotelSystemModeling.createHotelSystemModeling;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int m = 0;
        int k = 0;
        boolean correctInput = false;
        while (!correctInput) {
            try {
                m = in.nextInt();
                k = in.nextInt();
                HotelSystemModeling hotelSystemModeling = createHotelSystemModeling(k, m);
                hotelSystemModeling.start();
                correctInput = true;
            } catch (InputMismatchException e) {
                System.err.println("not correct parameters");
                correctInput = false;
            }
        }
    }

}
