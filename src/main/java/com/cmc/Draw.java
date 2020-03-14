package com.cmc;

import com.cmc.exceptions.ArgumentType;
import com.cmc.exceptions.WrongInputException;
import com.google.common.collect.ImmutableList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.InputMismatchException;

import static com.cmc.HotelSystemModeling.createHotelSystemModeling;
import static com.cmc.exceptions.ArgumentType.DaysNumber;
import static com.cmc.exceptions.ArgumentType.RoomNumber;

public class Draw extends JFrame {
    private final String tryAgainMessage = "Incorrect input";
    private final String welcomeMessage = "Please, enter number of days and total number of rooms";
    private final String wrongParams = "%s are wrong";
    private final static String newline = "\n";


    private JTextField daysNumberInputField;
    private final JTextField roomsNumberInputField;
    private final JLabel label;
    private JTable table1;


    // Модель данных таблицы
    private DefaultTableModel tableModel;
    // Заголовки столбцов
    private static Object[] columnsHeader = RoomType.values();



    private HotelSystemModeling hotelSystemModeling;
    private int numberOfDays;
    private int numberOfRooms;

    /*public void paint(Graphics g) {
        super.paint(g);
        g.drawLine(0, 0, 100, 100);

    }*/
    public static void main(String[] args) {
        new Draw();
    }

    Draw() {
        super("Draw");
        //JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createHorizontalBox();

        daysNumberInputField = new JTextField(2);
        roomsNumberInputField = new JTextField(10);

        //создание заголовка
        label = new JLabel(welcomeMessage + newline);

        // Создание кнопки Start
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            String m = daysNumberInputField.getText();
            String k = roomsNumberInputField.getText();
            try {
                numberOfDays = Integer.valueOf(m);
                DaysNumber.check(numberOfDays);
                numberOfRooms = Integer.valueOf(k);
                RoomNumber.check(numberOfRooms);
            } catch (NumberFormatException exception) {
                label.setText(tryAgainMessage + newline);
            } catch (WrongInputException ex) {
                label.setText(String.format(wrongParams, ex.getType().getMessageToRender()) + newline);
            }
            hotelSystemModeling = createHotelSystemModeling(5, 20);
            hotelSystemModeling.start();

        });


        //this.table1 = getjTable();
        Box contents = new Box(BoxLayout.Y_AXIS);
        //contents.add(new JScrollPane(table1));

        contents.add(label);
        contents.add(daysNumberInputField);
        contents.add(roomsNumberInputField);
        contents.add(start);

        // Вывод окна на экран
        getContentPane().add(contents);
        setSize(800, 600);
        setVisible(true);
    }

    private JTable getjTable() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnsHeader);
        // System.out.println(Arrays.toString(columnsHeader));
        pack();

        LocalDate.now();
        ImmutableList<RoomTypedRequestHandler> roomActionHadlers = hotelSystemModeling.getRoomActionHadlers();
        Object[] array = roomActionHadlers.stream().map(x -> x.getBookInfoList().toString()).toArray();
        tableModel.addRow(array);

        return new JTable(tableModel);
    }

}
