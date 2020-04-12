package com.cmc;

import com.cmc.exceptions.WrongInputException;
import com.cmc.hotel.HotelSystemModeling;
import com.cmc.info.BookingInfo;
import com.cmc.info.CheckInInfo;
import com.cmc.random.RandomGenerator;
import com.cmc.render.Tables;
import com.cmc.statistics.AverageStatistics;
import com.cmc.typed.RoomType;
import com.cmc.typed.RoomTypedRequestHandler;
import com.cmc.typed.TypedNumberOfRooms;
import com.google.common.collect.ImmutableList;
import com.intellij.uiDesigner.core.GridConstraints;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cmc.hotel.HotelSystemModeling.createHotelSystemModeling;
import static com.cmc.hotel.HotelSystemModeling.createHotelSystemModelingWithDefaultArgs;
import static com.cmc.exceptions.ArgumentType.DaysNumber;
import static com.cmc.exceptions.ArgumentType.RoomNumber;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Render extends JDialog implements ActionListener {
    public static final String successMessage = "success!!!!!!";
    public static final String tryAgainMessage = "Incorrect input";
    public static final String wrongParams = "%s are wrong";
    private final Tables tables;


    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonPause;
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
    private JSpinner randomSpinner;
    private JLabel time;
    private final static String newline = "\n";
    private HotelSystemModeling hotelSystemModeling;
    private final Timer timer = new Timer(1000, this);
    private ImmutableList<RoomTypedRequestHandler> roomActionHandlers;
    private DefaultTableModel tableBookModel;
    private DefaultTableModel tableCheckInModel;
    private static Object[] columnsBookHeader = Stream.of(BookingInfo.class.getDeclaredFields()).map(Field::getName).toArray();
    private static Object[] columnsCheckInHeader = Stream.of(CheckInInfo.class.getDeclaredFields()).map(Field::getName).toArray();

    private java.util.List<java.util.List<String>> bookingInfos = new ArrayList<>();
    private java.util.List<java.util.List<String>> allBookingInfos = new ArrayList<>();

    private java.util.List<java.util.List<String>> checkInInfos = new ArrayList<>();
    private java.util.List<java.util.List<String>> allCheckInInfos = new ArrayList<>();
    private Thread myThready;
    private java.util.List<JSpinner> inputSpinners;


    public Render() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(getStartButtonListener());

        this.tables = new Tables();

        setInputToDefault();
        tableBookModel = tables.getBookjTable();
        tableCheckInModel = tables.getCheckInjTable();
        tableBook.setModel(tableBookModel);
        tableCheckIn.setModel(tableCheckInModel);

        buttonPause.addActionListener(e -> onPause());
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
        JMenuItem busyness = new JMenu("Busyness");
        JMenuItem handledRequests = new JMenu("Handled Requests");

        JMenuItem allRooms = new JMenuItem("All Rooms");
        statisticsMenuItem.add(busyness);
        statisticsMenuItem.add(handledRequests);
        new ArrayList<>(Arrays.asList(RandomGenerator.ActionType.values())).stream()
                .map(x -> {
                    JMenuItem menuItem = new JMenuItem(x.getName());
                    menuItem.addActionListener(getHandledRequestsStatisticsActionListener(x));
                    handledRequests.add(menuItem);
                    return menuItem;
                }).collect(Collectors.toList());

        allRooms.addActionListener(getBusynessStatisticsActionListener(null));
        java.util.List<JMenuItem> menuItems = new ArrayList<>(Arrays.asList(RoomType.values()))
                .stream()
                .map(x -> {
                    JMenuItem menuItem = new JMenuItem(x.getName());
                    menuItem.addActionListener(getBusynessStatisticsActionListener(x));
                    busyness.add(menuItem);
                    return menuItem;
                }).collect(toList());
        menuBar.add(statisticsMenuItem);
        busyness.add(allRooms);
        menuBar.add(Box.createHorizontalGlue());
        setJMenuBar(menuBar);
    }
    private ActionListener getHandledRequestsStatisticsActionListener(RandomGenerator.ActionType type) {
        return e -> {
            if (hotelSystemModeling == null) {
                JOptionPane.showMessageDialog(Render.this,
                        "Not started! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else if (!hotelSystemModeling.isFinish()) {
                JOptionPane.showMessageDialog(Render.this,
                        "Not finished! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else {

                String format = String.format("%s handled request:%.2f%s", type.getName(), hotelSystemModeling.getRequestsStatistics(type), "%");
                JOptionPane.showMessageDialog(Render.this,
                        "<html>"+ format + "</html>",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }

    private ActionListener getBusynessStatisticsActionListener(RoomType type) {
        return e -> {
            if (hotelSystemModeling == null) {
                JOptionPane.showMessageDialog(Render.this,
                        "Not started! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else if (!hotelSystemModeling.isFinish()) {
                JOptionPane.showMessageDialog(Render.this,
                        "Not finished! Sorry(",
                        "Statistics", JOptionPane.WARNING_MESSAGE);
            } else {
                String str =
                        hotelSystemModeling.getRoomActionHandlers()
                                .stream()
                                .filter(x -> type == null || x.getType() == type)
                                .map(x -> {
                                    AverageStatistics averageStatistics = x.getAverageStatistics();
                                    String avBusyness = String.format("<dd> <b>average busyness</b> is %.2f%s</dd>", averageStatistics.getAvBusyness()*100, "%");
                                    String maxBusyness = String.format("<dd> <b>max busyness</b> is %.2f%s in %s </dd>", averageStatistics.getMaxBusyness()*100,"%", averageStatistics.getWhenMaxBusyness().toString());
                                    String minBusyness = String.format("<dd> <b>min busyness</b> is %.2f%s in %s </dd>", averageStatistics.getMinBusyness()*100, "%", averageStatistics.getWhenMinBusyness().toString());
                                    return "<dt>" + x.getType().toString() + "</dt>"
                                            + avBusyness + maxBusyness + minBusyness;
                                })
                                .reduce("", (x,y)-> x+ y);
                JOptionPane.showMessageDialog(Render.this,
                        "<html>"+ "<dl>" + str + "</dl>" + "</html>",
                        "Statistics", JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }

    private void onCancel() {
        dispose();
    }

    private void onPause() {
        hotelSystemModeling.setPause(!hotelSystemModeling.isPause());
    }

    public static void main(String[] args) {
        Render dialog = new Render();
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
        buttonPause = new JButton();
        buttonPause.setText("Cancel");
        panel2.add(buttonPause, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

    public java.util.List<java.util.List<String>> getNewBookRows() {
        java.util.List<java.util.List<String>> newBookInfos = roomActionHandlers.stream()
                .map(x -> x.getBookInfoList().stream().map(y -> {
                    java.util.List<String> bookInfo = y.getBookInfo();
                    bookInfo.add(x.getType().getName());
                    return bookInfo;
                }).collect(toList()))
                .flatMap(Collection::parallelStream)
                .collect(toList());
        java.util.List<java.util.List<String>> difference =
                newBookInfos.stream()
                        .filter(x -> !bookingInfos.contains(x))
                        .collect(Collectors.toList());
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
                .flatMap(Collection::parallelStream)
                .collect(toList());
        java.util.List<java.util.List<String>> difference = newCheckInInfos.stream().filter(x -> !checkInInfos.contains(x)).collect(Collectors.toList());
        checkInInfos = newCheckInInfos;
        return difference;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        buttonPause.setText(hotelSystemModeling.isPause() ? "Continue" : "Pause");
        time.setText(hotelSystemModeling.getCurrentTime().toString());
        for (java.util.List<String> info : getNewBookRows()) {
            tableBookModel.addRow(info.toArray());
            allBookingInfos.add(info);
        }
        for (java.util.List<String> info : getCheckInBookRows()) {
            tableCheckInModel.addRow(info.toArray());
            allCheckInInfos.add(info);
        }
        for (int i : getBookRowsToRemove()) {
            if (tableBookModel.getRowCount() > i) {
                tableBookModel.removeRow(i);
            }
        }
        for (int i : getCheckInRowsToRemove()) {
            if (tableCheckInModel.getRowCount() > i) {
                tableCheckInModel.removeRow(i);
            }
        }
    }

    private java.util.List<Integer> getBookRowsToRemove() {
        Set<String> bookIdsSet = bookingInfos.stream().map(x -> x.get(0)).collect(Collectors.toSet());
        int index = 0;
        ArrayList<Integer> result = new ArrayList<>();
        for (java.util.List<String> bookInfo: allBookingInfos) {
            if (!bookIdsSet.contains(bookInfo.get(0))) {
                result.add(index);
            }
            index++;
        }
        return result;
    }

    private java.util.List<Integer> getCheckInRowsToRemove() {
        Set<String> checkInIdsSet = checkInInfos.stream().map(x -> x.get(0)).collect(Collectors.toSet());
        int index = 0;
        ArrayList<Integer> result = new ArrayList<>();
        for (java.util.List<String> checkInInfo: allCheckInInfos) {
            if (!checkInIdsSet.contains(checkInInfo.get(0))) {
                result.add(index);
            }
            index++;
        }
        return result;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private ActionListener getStartButtonListener() {
        return e -> {
            try {
                java.util.List<Integer> args = checkSpinners();
                TypedNumberOfRooms typedNumberOfRooms = new TypedNumberOfRooms(args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
                hotelSystemModeling = createHotelSystemModeling(typedNumberOfRooms, args.get(0));
            } catch (WrongInputException ex) {
                label.setText(String.format("<html>Wrong input of %s , <br> created Hotel System with default arguments:<br> (check input fields) </html> ", ex.getType()));
                setInputToDefault();
                hotelSystemModeling = createHotelSystemModelingWithDefaultArgs();
            }
            setMenu();
            hotelSystemModeling.setRandomParam(Integer.parseInt(randomSpinner.getValue().toString()));
            myThready = new Thread(hotelSystemModeling);
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
        inputSpinners = Stream.of(numberOfDaysSpinner, suiteSpinner, juniorSuiteSpinner, singleSpinner, doubleSpinner, doubleExtraSpinner).collect(Collectors.toList());
        java.util.List<String> strings = inputSpinners.stream().map(x -> x.getValue().toString()).collect(toList());
        String numberOfDaysStr = strings.get(0);
        int numberOfDays = Integer.parseInt(numberOfDaysStr);
        DaysNumber.check(numberOfDays);
        strings.remove(0);
        int numberOfRooms = strings.stream().map(Integer::parseInt).reduce(0, Integer::sum);
        RoomNumber.check(numberOfRooms);
        label.setText(successMessage);
        strings.add(0, numberOfDaysStr);
        return strings.stream().map(Integer::parseInt).collect(toList());
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
