package me.bannock.budgeting.money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MoneyServiceImpl implements MoneyService {

    private final Map<String, Transaction> transactions = new HashMap<>();
    private boolean calculatedSpending = false;
    private BigDecimal totalCalculatedSpending = BigDecimal.ZERO;
    private BigDecimal totalCalculatedMoney = BigDecimal.ZERO;

    @Override
    public String addIncome(BigDecimal amount, int payDelay) {
        // Make sure the income is positive
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }

        // Create new transaction, generate key, and add it to the map
        Transaction transaction = new Transaction(amount, payDelay);
        String key = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX)
                + Long.toString(System.nanoTime(), Character.MAX_RADIX);
        transactions.put(key, transaction);

        // Return key because it may be used by the user to later remove this specific transaction
        return key;
    }

    @Override
    public String addExpense(BigDecimal amount, int payDelay) {
        // If amount is positive then negate it
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            amount = amount.multiply(BigDecimal.valueOf(-1));
        }

        // Create new transaction, generate key, and add it to the map
        Transaction transaction = new Transaction(amount, payDelay);
        String key = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX)
                + Long.toString(System.nanoTime(), Character.MAX_RADIX);
        transactions.put(key, transaction);

        // Return key because it may be used by the user to later remove this specific transaction
        return key;
    }

    @Override
    public boolean removeTransaction(String id) {
        return transactions.remove(id) != null;
    }

    @Override
    public void clearTransactions() {
        transactions.clear();
    }

    @Override
    public void doCalculations(int days) {
        // Reset
        totalCalculatedSpending = BigDecimal.ZERO;
        totalCalculatedMoney = BigDecimal.ZERO;

        // Simulate the hours
        for (int hour = 0; hour < days * 24; hour++){
            for (Transaction transaction : transactions.values()){
                // Continue if the transaction is not due yet
                if (hour % transaction.payDelay() != 0)
                    continue;

                // Update result variables
                if (transaction.amount().compareTo(BigDecimal.ZERO) < 0)
                    totalCalculatedSpending = totalCalculatedSpending.add(transaction.amount());
                totalCalculatedMoney = totalCalculatedMoney.add(transaction.amount());
            }
        }

        // Set calculated spending boolean to true so the user can use the result variable getters
        calculatedSpending = true;
    }

    @Override
    public BigDecimal getTotalSpending() {
        if (!calculatedSpending) {
            throw new IllegalStateException("Calculations have not been run yet");
        }
        return totalCalculatedSpending;
    }

    @Override
    public BigDecimal getTotalSavingsAfter() {
        if (!calculatedSpending) {
            throw new IllegalStateException("Calculations have not been run yet");
        }
        return totalCalculatedMoney;
    }

}
