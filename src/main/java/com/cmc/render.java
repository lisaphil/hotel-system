package com.cmc;

import com.cmc.exceptions.WrongInputException;
import com.google.common.collect.ImmutableList;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cmc.Draw.*;
import static com.cmc.HotelSystemModeling.createHotelSystemModeling;
import static com.cmc.HotelSystemModeling.createHotelSystemModelingWithDefaultArgs;
import static com.cmc.exceptions.ArgumentType.DaysNumber;
import static com.cmc.exceptions.ArgumentType.RoomNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class render extends JDialog implements ActionListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField daysNumberFormattedTextField;
    private JFormattedTextField roomsNumberFormattedTextField;
    private JButton checkButton;
    private JLabel label;
    private JTable tableBook;
    private JTable tableCheckIn;
    private int numberOfDays;
    private int numberOfRooms;
    private boolean correctInput = false;
    private final static String newline = "\n";
    private HotelSystemModeling hotelSystemModeling;
    private final Timer timer = new Timer(1000, this);
    private ImmutableList<RoomTypedRequestHandler> roomActionHandlers;
    private DefaultTableModel tableBookModel;
    private DefaultTableModel tableCheckInModel;
    private static Object[] columnsBookHeader = Stream.of(BookingInfo.class.getDeclaredFields()).map(Field::getName).toArray();
    private static Object[] columnsCheckInHeader = Stream.of(CheckInInfo.class.getDeclaredFields()).map(Field::getName).toArray();

    private java.util.List<java.util.List<String>> bookingInfos = new ArrayList<>();
    private java.util.List<java.util.List<String>> checkInInfos = new ArrayList<>();

    public render() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(getStartButtonListener());

        getBookjTable();
        getCheckInjTable();

        tableBook.setModel(tableBookModel);
        tableCheckIn.setModel(tableCheckInModel);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        checkButton.addActionListener(getCheckButtonListener());
    }


    private ActionListener getStartButtonListener() {
        return e -> {
            hotelSystemModeling = correctInput
                    ? createHotelSystemModeling(5, 20)
                    : createHotelSystemModelingWithDefaultArgs();
            setSize(800, 600);

            Thread myThready = new Thread(hotelSystemModeling);    //Создание потока "myThready"
            myThready.start();
            timer.start();
            roomActionHandlers = hotelSystemModeling.getRoomActionHandlers();

        };
    }

    private ActionListener getCheckButtonListener() {
        return e -> {
            String m = daysNumberFormattedTextField.getText();
            String k = roomsNumberFormattedTextField.getText();
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
        daysNumberFormattedTextField.setText("");
        roomsNumberFormattedTextField.setText("");
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        render dialog = new render();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(900, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableBook = new JTable();
        tableBook.setEnabled(false);
        tableBook.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        scrollPane1.setViewportView(tableBook);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel1.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableCheckIn = new JTable();
        tableCheckIn.setGridColor(new Color(-16777216));
        scrollPane2.setViewportView(tableCheckIn);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
        daysNumberFormattedTextField = new JFormattedTextField();
        daysNumberFormattedTextField.setText("");
        panel2.add(daysNumberFormattedTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        roomsNumberFormattedTextField = new JFormattedTextField();
        roomsNumberFormattedTextField.setText("");
        panel2.add(roomsNumberFormattedTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label = new JLabel();
        label.setText("Hello");
        panel2.add(label, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Start");
        panel3.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel3.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkButton = new JButton();
        checkButton.setText("Check");
        panel3.add(checkButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void getBookjTable() {
        tableBookModel = new DefaultTableModel();
        java.util.List<Object> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.add("room type");
        tableBookModel.setColumnIdentifiers(columnHeaderList.toArray());
        tableBookModel.addRow(Stream.of(columnsBookHeader).map(x -> "tt").toArray(String[]::new));
    }

    private void getCheckInjTable() {
        tableCheckInModel = new DefaultTableModel();
        java.util.List<Object> checkInHeaders = new ArrayList<>(Arrays.asList(columnsCheckInHeader));
        java.util.List<Object> columnHeaderList = new ArrayList<>(Arrays.asList(columnsBookHeader));
        columnHeaderList.addAll(checkInHeaders);
        columnHeaderList.add("room type");
        tableCheckInModel.setColumnIdentifiers(columnHeaderList.toArray());
    }


    public java.util.List<java.util.List<String>> getNewBookRows() {
        java.util.List<java.util.List<String>> newBookInfos = roomActionHandlers.stream()
                .map(x -> x.getBookInfoList().stream().map(y -> {
                    java.util.List<String> bookInfo = y.getBookInfo();
                    bookInfo.add(x.getType().getName());
                    return bookInfo;
                }).collect(toList()))
                .flatMap(Collection::stream)
                .collect(toList());
        java.util.List<java.util.List<String>> difference = newBookInfos.stream().filter(x -> !bookingInfos.contains(x)).collect(Collectors.toList());
        bookingInfos = newBookInfos;
        return difference;
    }

    public java.util.List<java.util.List<String>> getCheckInBookRows() {
        java.util.List<java.util.List<String>> newCheckInInfos = roomActionHandlers.stream()
                .map(x ->
                        x.getGuestInformation().stream()
                        .map(y -> {
                            java.util.List<String> checkInInfo = y.getCheckInInfo();
                            checkInInfo.add(x.getType().getName());
                            return checkInInfo;
                        })
                        .collect(toList()))
                .flatMap(Collection::stream)
                .collect(toList());
        java.util.List<java.util.List<String>> difference = newCheckInInfos.stream().filter(x -> !checkInInfos.contains(x)).collect(Collectors.toList());
        checkInInfos = newCheckInInfos;
        return difference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (java.util.List<String> info : getNewBookRows()) {
            tableBookModel.addRow(info.toArray());
        }
        for (java.util.List<String> info : getCheckInBookRows()) {
            tableCheckInModel.addRow(info.toArray());
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
