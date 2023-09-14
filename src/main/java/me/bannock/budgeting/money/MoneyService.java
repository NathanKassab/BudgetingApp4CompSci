package me.bannock.budgeting.money;

import java.math.BigDecimal;

public interface MoneyService {

    /**
     * Adds income
     * @param amount The amount of income
     * @param payDelay The delay in hours between each payment
     * @return The id of the assigned transaction, can be used to later remove it
     */
    String addIncome(BigDecimal amount, int payDelay);

    /**
     * Adds an expense
     * @param amount The amount of the expense
     * @param payDelay The delay in hours between each purchase
     * @return The id of the assigned transaction, can be used to later remove it
     */
    String addExpense(BigDecimal amount, int payDelay);

    /**
     * Removes a transaction
     * @param id The id of the transaction to remove
     * @return true if the transaction was successfully removed, otherwise false
     */
    boolean removeTransaction(String id);

    /**
     * Clears all transactions, effectively resetting the service
     */
    void clearTransactions();

    /**
     * Runs the calculations to find costs and expenses, as well as savings for x days
     * @param days The amount of days to calculate
     */
    void doCalculations(int days);

    /**
     * Gets the total amount of money spent after running the calculations
     * @return The total amount of money spent
     */
    BigDecimal getTotalSpending();

    /**
     * Gets the total amount of money saved after running the calculations
     * @return The total amount of money saved
     */
    BigDecimal getTotalSavingsAfter();

}
