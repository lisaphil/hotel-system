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
    private static Object[] columnsBookHeader = Stream.of(BookingInfo.class.getDeclaredFields()).map(Field::getName).toArray();
    private static Object[] columnsCheckInHeader = Stream.of(CheckInInfo.class.getDeclaredFields()).map(Field::getName).toArray();

    public DefaultTableModel getBookjTable() {
        tableBookModel = new DefaultTableModel();
        java.util.List<Object> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.add("room type");
        tableBookModel.setColumnIdentifiers(columnHeaderList.toArray());
        return tableBookModel;
    }

    public DefaultTableModel getCheckInjTable() {
        tableCheckInModel = new DefaultTableModel();
        java.util.List<Object> checkInHeaders = new ArrayList<>(Arrays.asList(columnsCheckInHeader));
        java.util.List<Object> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.addAll(checkInHeaders);
        columnHeaderList.add("room type");
        tableCheckInModel.setColumnIdentifiers(columnHeaderList.toArray());
        return tableCheckInModel;
    }
}
