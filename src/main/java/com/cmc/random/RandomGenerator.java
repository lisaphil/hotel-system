package com.cmc.random;

import com.cmc.BookingInfo;
import com.cmc.RoomType;
import com.google.common.collect.ImmutableList;
import com.sun.tools.javac.util.Pair;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class RandomGenerator {
    private LocalDate startDate;
    private LocalDate finishDate;
    private Random randomDate;
    private Random randomName;
    private Random randomRoom;


    private final ImmutableList<String> names = ImmutableList.of("Алина", "Анастасия", "Анжела", "Анна", "Валерия", "Вероника", "Виктория", "Дарья", "Евгения", "Екатерина", "Елена",
            "Елизавета", "Карина", "Кира", "Ксения", "Марина", "Мария", "Надежда", "Наталья",
            "Виктор", "Владимир", "Денис", "Дмитpий", "Евгений", "Егор", "Иван", "Максим", "Михаил", "Никита",
            "Николай", "Олег", "Семен", "Сергей", "Станислав"
    );

    public RandomGenerator(LocalDate startDate, LocalDate finishDate) {
        this.randomDate = new Random(3); //TODO
        this.randomName = new Random(5);
        this.randomRoom = new Random(7);
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public BookingInfo generateBookInfo(LocalDate currentTime) {
        Pair<LocalDate, LocalDate> fromToLocalDate = generateDate(currentTime);
        LocalDate from = fromToLocalDate.fst;
        LocalDate to = fromToLocalDate.snd;
        String name = pickName();
        return new BookingInfo(from, to, name);
    }

    public RoomType generateRoomType() {
        List<RoomType> roomTypes = Arrays.asList(RoomType.values());
        int randomIndex = randomRoom.nextInt(roomTypes.size() + 1);
        return roomTypes.get(randomIndex);
    }
    public Pair<LocalDate, LocalDate> generateDate(LocalDate currentTime) {
        int days = (int) Duration.between(currentTime.atStartOfDay(), finishDate.atStartOfDay()).toDays();
        int fisrtRandom = randomDate.nextInt(days + 1);
        int secondRandom = randomDate.nextInt(days + 1);
        int min = min(fisrtRandom, secondRandom);
        int max = max(fisrtRandom, secondRandom);
        LocalDate from = currentTime.plusDays(min);
        LocalDate to = currentTime.plusDays(max);
        return Pair.of(from, to);
    }

    private String pickName() {
        int randomIndex = randomName.nextInt(names.size() + 1);
        return names.get(randomIndex);
    }
}
