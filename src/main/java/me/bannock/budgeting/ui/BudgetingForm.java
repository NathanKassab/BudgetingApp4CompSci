package me.bannock.budgeting.ui;

import com.google.inject.Inject;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import me.bannock.budgeting.money.MoneyService;
import me.bannock.budgeting.money.Transaction;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BudgetingForm {

    private final MoneyService moneyService;
    private final Map<Long, Transaction> expensesMap = new HashMap<>();

    private JPanel formPanel;
    private JComboBox incomeType;
    private JButton runCalculationsButton;
    private JSpinner incomeField;
    private JLabel totalMonthlySpendingLabel;
    private JLabel totalMonthlySavingsLabel;
    private JPanel incomeAndStatsPanel;
    private JComboBox addItemBoxName;
    private JTextField addItemBoxNameText;
    private JButton addItemBoxSubmit;
    private JPanel addItemBox;
    private JSpinner addItemBoxAmount;
    private JPanel addItemBoxPadding;
    private JPanel expensesListPanel;

    @Inject
    public BudgetingForm(MoneyService moneyService) {
        this.moneyService = moneyService;
        $$$setupUI$$$();
        runCalculationsButton.addActionListener(e -> runCalculations());
        addItemBoxName.addActionListener(e -> {
            if (((String) addItemBoxName.getSelectedItem()).isBlank())
                return;

            // Enable submit button
            addItemBoxSubmit.setEnabled(true);

            // Replace spinner with text field
            addItemBoxPadding.remove(addItemBoxName);
            // Creating the text field component
            addItemBoxNameText = new JTextField();
            addItemBoxNameText.setText((addItemBoxName.getSelectedItem()).equals("Other") ?
                    "" : (String) addItemBoxName.getSelectedItem());
            // Add and repaint
            addItemBoxPadding.add(addItemBoxNameText, BorderLayout.CENTER);
            addItemBoxPadding.revalidate();
            addItemBoxPadding.repaint();
            if ((addItemBoxName.getSelectedItem()).equals("Other")) {
                addItemBoxNameText.requestFocus();
            } else {
                addItemBoxAmount.requestFocus();

                // Spaghetti code from stackoverflow to select all text in the spinner
                JSpinner.DefaultEditor editor =
                        (JSpinner.DefaultEditor) addItemBoxAmount.getEditor();
                editor.getTextField().addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent event) {
                        final JTextField textField = (JTextField) event.getComponent();
                        EventQueue.invokeLater(textField::selectAll);
                    }
                });
            }
        });

        // Submit new expense listener
        addItemBoxSubmit.addActionListener(e -> {
            // Add expense to the map
            long key = System.currentTimeMillis();
            expensesMap.put(key,
                    new Transaction((BigDecimal) addItemBoxAmount.getValue(), 24 * 7 * 4));
            // Create UI element to show the expense
            JPanel expenseDisplayPanel = new JPanel(new BorderLayout());
            expenseDisplayPanel.add(new JLabel(addItemBoxNameText.getText()
                            + ": " + formatDollar((BigDecimal) addItemBoxAmount.getValue())),
                    BorderLayout.CENTER);
            JButton removeButton = new JButton("Remove");
            removeButton.addActionListener(l -> {
                // Code to remove the expense from the map and ui
                expensesMap.remove(key);
                expensesListPanel.remove(expenseDisplayPanel);
                expensesListPanel.revalidate();
                expensesListPanel.repaint();

                // Something changed and we need to reset the summary
                resetSummary();
            });
            expenseDisplayPanel.add(removeButton, BorderLayout.EAST);
            expenseDisplayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                    expenseDisplayPanel.getPreferredSize().height));
            expensesListPanel.add(expenseDisplayPanel);
            expensesListPanel.revalidate();
            expensesListPanel.repaint();

            // Reset the add item box
            addItemBoxSubmit.setEnabled(false);
            addItemBoxPadding.remove(addItemBoxNameText);
            addItemBoxName.setSelectedIndex(0);
            addItemBoxPadding.add(addItemBoxName, BorderLayout.CENTER);
            addItemBoxAmount.setValue(BigDecimal.ZERO);
            addItemBoxPadding.revalidate();
            addItemBoxPadding.repaint();

            // We added a new expense, so we need to reset the summary
            resetSummary();

            // Focus the name box
            addItemBoxName.requestFocus();
        });

        // Add listener to the income field so we can reset
        // the summery when the number is updated
        incomeField.addChangeListener(e -> resetSummary());

        // Add listener to the income type so we can reset
        // the summery when the type is updated
        incomeType.addActionListener(e -> resetSummary());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayoutManager(6, 1, new Insets(8, 8, 8, 8), -1, -1));
        incomeAndStatsPanel = new JPanel();
        incomeAndStatsPanel.setLayout(new GridLayoutManager(4, 5, new Insets(5, 5, 5, 5), -1, -1, false, true));
        formPanel.add(incomeAndStatsPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        incomeAndStatsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Total Monthly Spending: ");
        incomeAndStatsPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Total Monthly Savings: ");
        incomeAndStatsPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalMonthlySpendingLabel = new JLabel();
        totalMonthlySpendingLabel.setText("$?");
        incomeAndStatsPanel.add(totalMonthlySpendingLabel, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalMonthlySavingsLabel = new JLabel();
        totalMonthlySavingsLabel.setText("$?");
        incomeAndStatsPanel.add(totalMonthlySavingsLabel, new GridConstraints(2, 2, 1, 3, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Total Income: ");
        incomeAndStatsPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incomeType = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Hour");
        defaultComboBoxModel1.addElement("Week");
        defaultComboBoxModel1.addElement("Month");
        defaultComboBoxModel1.addElement("Year");
        incomeType.setModel(defaultComboBoxModel1);
        incomeAndStatsPanel.add(incomeType, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        incomeAndStatsPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("/");
        incomeAndStatsPanel.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        runCalculationsButton = new JButton();
        runCalculationsButton.setText("Run Calculations");
        incomeAndStatsPanel.add(runCalculationsButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incomeAndStatsPanel.add(incomeField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setEnabled(true);
        scrollPane1.setHorizontalScrollBarPolicy(31);
        formPanel.add(scrollPane1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        scrollPane1.setViewportView(panel1);
        panel1.add(expensesListPanel, BorderLayout.CENTER);
        expensesListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new CardLayout(0, 0));
        formPanel.add(panel2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addItemBox = new JPanel();
        addItemBox.setLayout(new BorderLayout(0, 0));
        panel2.add(addItemBox, "Card1");
        addItemBoxSubmit = new JButton();
        addItemBoxSubmit.setEnabled(false);
        addItemBoxSubmit.setText("Add");
        addItemBox.add(addItemBoxSubmit, BorderLayout.EAST);
        addItemBoxPadding = new JPanel();
        addItemBoxPadding.setLayout(new BorderLayout(0, 0));
        addItemBox.add(addItemBoxPadding, BorderLayout.CENTER);
        addItemBoxName = new JComboBox();
        addItemBoxName.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("");
        defaultComboBoxModel2.addElement("Car Payment");
        defaultComboBoxModel2.addElement("Insurance");
        defaultComboBoxModel2.addElement("Groceries (Monthly)");
        defaultComboBoxModel2.addElement("Gym");
        defaultComboBoxModel2.addElement("Utilities");
        defaultComboBoxModel2.addElement("Phone Plan");
        defaultComboBoxModel2.addElement("Other");
        addItemBoxName.setModel(defaultComboBoxModel2);
        addItemBoxPadding.add(addItemBoxName, BorderLayout.CENTER);
        addItemBoxPadding.add(addItemBoxAmount, BorderLayout.EAST);
        final JLabel label5 = new JLabel();
        label5.setText("Monthly Expenses: ");
        formPanel.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setEnabled(true);
        formPanel.add(separator1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Summary: ");
        formPanel.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return formPanel;
    }

    /**
     * Creates and opens a new budgeting window for the user
     */
    public void createWindow() {
        JFrame frame = new JFrame("Budgeting Calculator");
        frame.setContentPane(formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(525, 575);
        frame.setVisible(true);
    }

    /**
     * UI components that were in the form builder, but I wanted to initilize them myself for
     * one reason or another
     */
    private void createUIComponents() {
        incomeField = new JSpinner(new SpinnerNumberModel(new BigDecimal(20),
                new BigDecimal(0), new BigDecimal(1000000000), new BigDecimal("0.01")));
        addItemBoxAmount = new JSpinner(new SpinnerNumberModel(new BigDecimal(0),
                new BigDecimal(0), new BigDecimal(1000000000), new BigDecimal("0.01")));
        expensesListPanel = new JPanel();
        expensesListPanel.setLayout(new BoxLayout(expensesListPanel, BoxLayout.Y_AXIS));
    }

    /**
     * Updates the reported income to the money service, does not keep track
     * of whether an income has already been added or not
     *
     * @return The id of the income transaction
     */
    private String updateIncome() {

        // Get the pay delay based on the payment frequency
        if (incomeType.getSelectedItem() == null)
            throw new IllegalStateException("Income type is null");

        // We need to find the pay delay based on the income type
        BigDecimal income = (BigDecimal) incomeField.getValue();
        int paymentDelay = 0;
        switch ((String) incomeType.getSelectedItem()) {
            case "Hour":
                // Assuming hourly is a full 40 hour work week, we can convert to weekly
                paymentDelay = 168;
                income = income.multiply(new BigDecimal(40));
                break;
            case "Week":
                paymentDelay = 168;
                break;
            case "Month":
                paymentDelay = 672;
                break;
            case "Year":
                // Yearly won't properly measure because the ui only keeps track of one month
                // because of this we convert to monthly
                paymentDelay = 672;
                income = income.divide(new BigDecimal(12), 2, RoundingMode.DOWN);
                break;
        }

        // If the income type isn't in the switch then it will be 0, this should never happen
        if (paymentDelay == 0)
            throw new IllegalStateException("Payment delay is 0");

        // Add the income transaction to the money service
        return moneyService.addIncome(income, paymentDelay);
    }

    /**
     * Runs calculations and updates labels accordingly
     */
    private void runCalculations() {
        // First reset money service
        moneyService.clearTransactions();

        // Second make sure the income amount is properly set
        updateIncome();

        // Third add all other expenses
        for (Transaction transaction : expensesMap.values())
            moneyService.addExpense(transaction.amount(), transaction.payDelay());

        // Finally run calculations
        moneyService.doCalculations(7 * 4);

        // Lastly update labels and refresh ui
        totalMonthlySpendingLabel.setText(formatDollar(moneyService.getTotalSpending().abs()));
        totalMonthlySavingsLabel.setText(formatDollar(moneyService.getTotalSavingsAfter()));
        incomeAndStatsPanel.revalidate();
        incomeAndStatsPanel.repaint();
    }

    /**
     * Resets the summary labels on the display, used when something is updated
     * and the summary needs to be recalculated
     */
    private void resetSummary() {
        totalMonthlySpendingLabel.setText("$?");
        totalMonthlySavingsLabel.setText("$?");
        incomeAndStatsPanel.revalidate();
        incomeAndStatsPanel.repaint();
    }

    /**
     * Formats a dollar amount to a string
     *
     * @param dollar The dollar amount to format
     * @return The formatted string
     */
    private String formatDollar(BigDecimal dollar) {
        if (dollar.compareTo(BigDecimal.ZERO) < 0)
            return "-$" + dollar.abs().toString();
        return "$" + dollar.toString();
    }

}
