package com.cmc.random;

import com.cmc.BookingInfo;
import com.sun.tools.javac.util.Pair;
import jdk.tools.jaotc.LoadedClass;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class RandomGenerator {
    private LocalDate startDate;
    private LocalDate finishDate;
    private Random random;

    public RandomGenerator(LocalDate startDate, LocalDate finishDate) {
        this.random = new Random();
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
    public BookingInfo genearteBookInfo(LocalDate currentTime) {
        Pair<LocalDate, LocalDate> fromToLocalDate = generateDate(currentTime);
        LocalDate from = fromToLocalDate.fst;
        LocalDate to = fromToLocalDate.snd;
        new BookingInfo()

    }
    public Pair<LocalDate, LocalDate> generateDate(LocalDate currentTime) {

        int days = (int) Duration.between(currentTime.atStartOfDay(), finishDate.atStartOfDay()).toDays();
        int fisrtRandom = random.nextInt(days+ 1);
        int secondRandom = random.nextInt(days + 1);
        int min = min(fisrtRandom, secondRandom);
        int max = max(fisrtRandom, secondRandom);
        LocalDate from = currentTime.plusDays(min);
        LocalDate to = currentTime.plusDays(max);
        return Pair.of(from, to);
    }
}
