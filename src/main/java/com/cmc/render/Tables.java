package com.cmc.render;

import com.cmc.info.BookingInfo;
import com.cmc.info.CheckInInfo;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Tables {
    private DefaultTableModel tableBookModel;
    private DefaultTableModel tableCheckInModel;
    private static String[] columnsBookHeader = Stream.of(BookingInfo.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
    private static String[] columnsCheckInHeader = Stream.of(CheckInInfo.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);

    public DefaultTableModel getBookjTable() {
        tableBookModel = new DefaultTableModel();
        java.util.List<String> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.add("room type");
        tableBookModel.setColumnIdentifiers(columnHeaderList.toArray());
        return tableBookModel;
    }

    public DefaultTableModel getCheckInjTable() {
        tableCheckInModel = new DefaultTableModel();
        java.util.List<String> checkInHeaders = new ArrayList<>(Arrays.asList(columnsCheckInHeader));
        java.util.List<String> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.addAll(checkInHeaders);
        columnHeaderList.add("room type");
        tableCheckInModel.setColumnIdentifiers(columnHeaderList.toArray());
        return tableCheckInModel;
    }
}
