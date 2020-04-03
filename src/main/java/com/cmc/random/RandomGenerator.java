package com.cmc.random;

import com.cmc.info.BookingInfo;
import com.cmc.typed.RoomType;
import com.google.common.collect.ImmutableList;
import org.javatuples.Pair;

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
    private Random randomAction;
    private Random randomDelta;
    private Random randomPayed;


    public long generateDeltaHours() {
        int result = randomDelta.nextInt(5);
        while (result <= 0) {
            result = randomDelta.nextInt(5);
        }
        return result;
    }

    public boolean getPayed() {
        return randomPayed.nextBoolean();
    }

    public enum ActionType {
        CheckIn("Check In"),
        Book("Book");

        private String name;

        ActionType(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }
    }


    private final ImmutableList<String> names = ImmutableList.of("Алина", "Анастасия", "Анжела", "Анна", "Валерия", "Вероника", "Виктория", "Дарья", "Евгения", "Екатерина", "Елена",
            "Елизавета", "Карина", "Кира", "Ксения", "Марина", "Мария", "Надежда", "Наталья",
            "Виктор", "Владимир", "Денис", "Дмитpий", "Евгений", "Егор", "Иван", "Максим", "Михаил", "Никита",
            "Николай", "Олег", "Семен", "Сергей", "Станислав"
    );

    public RandomGenerator(LocalDate startDate, LocalDate finishDate, int param) {
        Random random = new Random(param);
        this.randomDate = new Random(random.nextInt());
        this.randomName = new Random(random.nextInt());
        this.randomRoom = new Random(random.nextInt());
        this.randomAction = new Random(random.nextInt());
        this.randomDelta = new Random(random.nextInt());
        this.randomPayed = new Random(random.nextInt());
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public ActionType generateEvent() {
        return randomAction.nextBoolean() ? ActionType.Book : ActionType.CheckIn;
    }

    public BookingInfo generateBookInfo(LocalDate currentTime) {
        Pair<LocalDate, LocalDate> fromToLocalDate = generateDate(currentTime);
        LocalDate from = fromToLocalDate.getValue0();
        LocalDate to = fromToLocalDate.getValue1();
        String name = pickName();
        return new BookingInfo(from, to, name);
    }

    public RoomType generateRoomType() {
        List<RoomType> roomTypes = Arrays.asList(RoomType.values());
        int randomIndex = randomRoom.nextInt(roomTypes.size());
        return roomTypes.get(randomIndex);
    }

    public Pair<LocalDate, LocalDate> generateDate(LocalDate currentTime) {
        int days = (int) Duration.between(currentTime.atStartOfDay(), finishDate.atStartOfDay()).toDays() + 1;
        int firstRandom = randomDate.nextInt(days);
        int secondRandom = randomDate.nextInt(days);
        while (firstRandom == secondRandom && days != 0) {
            secondRandom = randomDate.nextInt(days);
        }
        int min = min(firstRandom, secondRandom);
        int max = max(firstRandom, secondRandom);
        LocalDate from = currentTime.plusDays(min);
        LocalDate to = currentTime.plusDays(max);
        return Pair.with(from, to);
    }

    private String pickName() {
        int randomIndex = randomName.nextInt(names.size());
        return names.get(randomIndex);
    }
}
