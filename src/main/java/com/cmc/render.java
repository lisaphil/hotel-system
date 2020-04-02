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

import static com.cmc.HotelSystemModeling.createHotelSystemModeling;
import static com.cmc.HotelSystemModeling.createHotelSystemModelingWithDefaultArgs;
import static com.cmc.exceptions.ArgumentType.DaysNumber;
import static com.cmc.exceptions.ArgumentType.RoomNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class render extends JDialog implements ActionListener {
    public static final String successMessage = "success!!!!!!";
    public static final String tryAgainMessage = "Incorrect input";
    public static final String wrongParams = "%s are wrong";


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton checkButton;
    private JLabel label;
    private JTable tableBook;
    private JTable tableCheckIn;
    private JSpinner numberOfDaysSpinner;
    private JSpinner suiteSpinner;
    private JSpinner juniorSuiteSpinner;
    private JSpinner singleSpinner;
    private JSpinner doubleSpinner;
    private JSpinner doubleExtraSpinner;
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
        setMenu();

        getBookjTable();
        getCheckInjTable();
        tableBook.setModel(tableBookModel);
        tableCheckIn.setModel(tableCheckInModel);

        buttonCancel.addActionListener(e -> onCancel());
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        checkButton.addActionListener(getCheckButtonListener());
    }

    private void setMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu statisticsMenuItem = new JMenu("Statistics");
        JMenuItem allRooms = new JMenuItem("All Rooms");

        JMenuItem suite = new JMenuItem(RoomType.Suite.getName());
        allRooms.addActionListener(getSatisticsActionListener(null));
        suite.addActionListener(getSatisticsActionListener(RoomType.Suite));
        java.util.List<JMenuItem> menuItems = new ArrayList<>(Arrays.asList(RoomType.values()))
                .stream()
                .map(x -> {
                    JMenuItem menuItem = new JMenuItem(x.getName());
                    menuItem.addActionListener(getSatisticsActionListener(x));
                    statisticsMenuItem.add(menuItem);
                    return menuItem;
                }).collect(toList());
        menuBar.add(statisticsMenuItem);
        statisticsMenuItem.add(allRooms);
        menuBar.add(Box.createHorizontalGlue());
        setJMenuBar(menuBar);
    }

    private ActionListener getSatisticsActionListener(RoomType type) {
        return e -> {
            if (hotelSystemModeling == null) {
                JOptionPane.showMessageDialog(render.this,
                        "Not started! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else if (!hotelSystemModeling.isFinish()) {
                JOptionPane.showMessageDialog(render.this,
                        "Not finished! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else {
                java.util.List<String> collect =
                        hotelSystemModeling.getRoomActionHandlers()
                                .stream()
                                .filter(x -> type == null || x.getType() == type)
                                .map(x -> x.getType().toString() + " " + x.getStatistics().getAvBusyness())
                                .collect(toList());
                JOptionPane.showMessageDialog(render.this,
                        collect.toString(),
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        };
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(7, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1, true, true));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("Start");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkButton = new JButton();
        checkButton.setText("Check");
        panel2.add(checkButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label = new JLabel();
        label.setText("Hello! Please enter folowing parameters:");
        panel1.add(label, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        numberOfDaysSpinner = new JSpinner();
        panel3.add(numberOfDaysSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Number of days");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Number of rooms of each type");
        panel4.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        juniorSuiteSpinner = new JSpinner();
        panel6.add(juniorSuiteSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Junior Suite");
        panel6.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Double");
        panel7.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doubleSpinner = new JSpinner();
        panel7.add(doubleSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel8, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        singleSpinner = new JSpinner();
        panel8.add(singleSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Single");
        panel8.add(label5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel9, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        suiteSpinner = new JSpinner();
        panel9.add(suiteSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Suite");
        panel9.add(label6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel10, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel10.setBorder(BorderFactory.createTitledBorder(""));
        doubleExtraSpinner = new JSpinner();
        panel10.add(doubleExtraSpinner, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Double + extra bed");
        panel10.add(label7, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel11, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel11.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableBook = new JTable();
        tableBook.setEnabled(false);
        tableBook.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        scrollPane1.setViewportView(tableBook);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel11.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableCheckIn = new JTable();
        tableCheckIn.setGridColor(new Color(-16777216));
        scrollPane2.setViewportView(tableCheckIn);
        final JLabel label8 = new JLabel();
        label8.setText("Booking information:");
        panel11.add(label8, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Check In information");
        panel11.add(label9, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
                .flatMap(Collection::parallelStream)
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

    private ActionListener getStartButtonListener() {
        return e -> {
            try {
                java.util.List<Integer> args = checkSpinners();
                hotelSystemModeling = createHotelSystemModeling(
                        args.get(1), args.get(2), args.get(3), args.get(4), args.get(5), args.get(0));
            } catch (WrongInputException ex) {
                label.setText(String.format("<html>Wrong input of %s , <br> created Hotel System with default arguments:<br> (check input fields) </html> ", ex.getType()));
                setInputToDefault();
                hotelSystemModeling = createHotelSystemModelingWithDefaultArgs();
            }
            //setSize(800, 600);
            Thread myThready = new Thread(hotelSystemModeling);    //Создание потока "myThready"
            myThready.start();
            timer.start();
            roomActionHandlers = hotelSystemModeling.getRoomActionHandlers();
        };
    }

    private ActionListener getCheckButtonListener() {
        return e -> {
            try {
                checkSpinners();
            } catch (NumberFormatException exception) {
                setInputToDefault();
                label.setText(tryAgainMessage + newline);
            } catch (WrongInputException ex) {
                setInputToDefault();
                label.setText(String.format(wrongParams, ex.getType().getMessageToRender()) + newline);
            }
        };
    }

    private java.util.List<Integer> checkSpinners() throws WrongInputException {
        java.util.List<JSpinner> inputSpinners = Stream.of(numberOfDaysSpinner, suiteSpinner, juniorSuiteSpinner, singleSpinner, doubleSpinner, doubleExtraSpinner).collect(Collectors.toList());
        java.util.List<String> strings = inputSpinners.stream().map(x -> x.getValue().toString()).collect(toList());
        String numberOfDaysStr = strings.get(0);
        int numberOfDays = Integer.parseInt(numberOfDaysStr);
        DaysNumber.check(numberOfDays);
        strings.remove(0);
        int numberOfRooms = strings.stream().map(x -> Integer.parseInt(x)).reduce(0, Integer::sum);
        RoomNumber.check(numberOfRooms);
        label.setText(successMessage);
        strings.add(0, numberOfDaysStr);
        return strings.stream().map(x -> Integer.parseInt(x)).collect(toList());
    }

    private void setInputToDefault() {
        int defaultNumberRooms = HotelSystemModeling.defaultNumberRooms;
        juniorSuiteSpinner.setValue(defaultNumberRooms);
        singleSpinner.setValue(defaultNumberRooms);
        doubleExtraSpinner.setValue(defaultNumberRooms);
        doubleSpinner.setValue(defaultNumberRooms);
        suiteSpinner.setValue(defaultNumberRooms);
        numberOfDaysSpinner.setValue(HotelSystemModeling.defaultNumberDays);
    }
}
