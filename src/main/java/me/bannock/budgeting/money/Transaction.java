package me.bannock.budgeting.money;

import java.math.BigDecimal;

public record Transaction(BigDecimal amount, int payDelay) {

        public Transaction {
            if (payDelay <= 0) {
                throw new IllegalArgumentException("Pay delay must be greater than 0");
            }
        }

}
