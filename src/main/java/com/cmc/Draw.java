package com.cmc;

import com.cmc.exceptions.WrongInputException;
import com.google.common.collect.ImmutableList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import static com.cmc.HotelSystemModeling.createHotelSystemModeling;
import static com.cmc.HotelSystemModeling.createHotelSystemModelingWithDefaultArgs;
import static com.cmc.exceptions.ArgumentType.DaysNumber;
import static com.cmc.exceptions.ArgumentType.RoomNumber;

public class Draw extends JFrame {
    private final String successMessage = "success!!!!!!";
    private final String tryAgainMessage = "Incorrect input";
    private final String welcomeMessage = "Please, enter number of days and total number of rooms";
    private final String wrongParams = "%s are wrong";

    private final static String newline = "\n";
    private final Box contents = new Box(BoxLayout.Y_AXIS);
    private boolean correctInput = false;
    ;


    private JTextField daysNumberInputField;
    private final JTextField roomsNumberInputField;
    private final JLabel label;
    private final JButton start = new JButton("Start");
    private final JButton check = new JButton("Check");

    private JTable table1;


    // Модель данных таблицы
    private DefaultTableModel tableModel;
    // Заголовки столбцов
    private static Object[] columnsHeader = RoomType.values();


    private HotelSystemModeling hotelSystemModeling;
    private int numberOfDays;
    private int numberOfRooms;

    public static void main(String[] args) {
        new Draw();
    }

    Draw() {
        super("Draw");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createHorizontalBox();

        daysNumberInputField = new JTextField(2);
        roomsNumberInputField = new JTextField(10);

        //создание заголовка
        label = new JLabel("hello");
        label.setText(welcomeMessage + newline);
        check.addActionListener(getCheckButtonListener());

        start.addActionListener(getStartButtonListener());

        initialState();

        // Вывод окна на экран
        getContentPane().add(contents);
        setSize(800, 600);
        setVisible(true);
    }

    private void initialState() {
        contents.add(label);
        contents.add(daysNumberInputField);
        contents.add(roomsNumberInputField);
        contents.add(start);
        contents.add(check);
    }

    private ActionListener getStartButtonListener() {
        return e -> {
            hotelSystemModeling = correctInput
                    ? createHotelSystemModeling(5, 20)
                    : createHotelSystemModelingWithDefaultArgs();
            hotelSystemModeling.start();
            contents.removeAll();
            contents.updateUI();
            this.table1 = getjTable();
            contents.add(new JScrollPane(table1));
            setSize(800, 600);
        };
    }

    private ActionListener getCheckButtonListener() {
        return e -> {
            String m = daysNumberInputField.getText();
            String k = roomsNumberInputField.getText();
            try {
                numberOfDays = Integer.parseInt(m);
                DaysNumber.check(numberOfDays);
                numberOfRooms = Integer.parseInt(k);
                RoomNumber.check(numberOfRooms);
                label.setText(successMessage);
                correctInput = true;
            } catch (NumberFormatException exception) {
                clearInputFields();
                label.setText(tryAgainMessage + newline);
            } catch (WrongInputException ex) {
                clearInputFields();
                label.setText(String.format(wrongParams, ex.getType().getMessageToRender()) + newline);
            }
        };
    }

    private void clearInputFields() {
        daysNumberInputField.setText("");
        roomsNumberInputField.setText("");
    }

    private JTable getjTable() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnsHeader);
        // System.out.println(Arrays.toString(columnsHeader));
        pack();

        LocalDate.now();
        ImmutableList<RoomTypedRequestHandler> roomActionHandlers = hotelSystemModeling.getRoomActionHandlers();
        Object[] array = roomActionHandlers.stream().map(x -> x.getBookInfoList().toString()).toArray();
        tableModel.addRow(array);

        return new JTable(tableModel);
    }

}
