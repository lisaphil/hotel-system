package com.cmc;

import com.cmc.exceptions.ArgumentType;
import com.cmc.exceptions.WrongInputException;
import com.google.common.collect.ImmutableList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.InputMismatchException;

import static com.cmc.HotelSystemModeling.createHotelSystemModeling;

public class Draw extends JFrame {
    private final String warningMessage = "Incorrect input";
    private JTextField mInputField;
    private final JTextField kInputField;
    // Модель данных таблицы
    private DefaultTableModel tableModel;
    private JTable table1;
    // Заголовки столбцов
    private static Object[] columnsHeader = RoomType.values();

    private boolean tmp;
    private final static String newline = "\n";

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

        mInputField = new JTextField(10);
        kInputField = new JTextField(10);

        // Создание кнопки Start
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            String m = mInputField.getText();
            String k = kInputField.getText();
            tmp = true;
            try {
                int numberOfDays = Integer.valueOf(m);
                int numberOfRooms = Integer.valueOf(k);
                if (numberOfDays > 30 || numberOfDays < 12) {
                    throw new WrongInputException("", ArgumentType.DaysNumber, numberOfDays);
                }
                if (numberOfRooms > 30 || numberOfRooms < 20) {
                    throw new WrongInputException("", ArgumentType.RoomNumber, numberOfRooms);
                }
            } catch (NumberFormatException exception) {
                textArea.append(warningMessage + newline);
            } catch (WrongInputException ex) {
                ex.printStackTrace();//TODO
            }

            // Номер выделенной строки
            int idx = table1.getSelectedRow();
            // Удаление выделенной строки
            tableModel.removeRow(idx);
        });

        int m;
        int k;
        boolean correctInput = false;
        while (!correctInput) {
            try {
                m = in.nextInt();
                k = in.nextInt();
                correctInput = true;
            } catch (InputMismatchException e) {
                System.err.println("not correct parameters");
                correctInput = false;
            }
        }

        this.table1 = getjTable();
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));

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


        HotelSystemModeling hotelSystemModeling = createHotelSystemModeling(5, 20);
        hotelSystemModeling.start();

        LocalDate.now();

        ImmutableList<RoomTypedRequestHandler> roomActionHadlers = hotelSystemModeling.getRoomActionHadlers();
        Object[] array = roomActionHadlers.stream().map(x -> x.getBookInfoList().toString()).toArray();
        tableModel.addRow(array);

        return new JTable(tableModel);
    }

}
