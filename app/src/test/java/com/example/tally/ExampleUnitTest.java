package com.example.tally;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void splitExpenseCreatesOppositeBalances() {
        java.util.List<String> members = java.util.Arrays.asList("You", "Ethan");
        Expense expense = Expense.create("cocoa", "Dinner", 30.0, "You", members, "Food");
        java.util.Map<String, Long> balances = BalanceCalculator.balances(
                java.util.Collections.singletonList(expense));

        assertEquals(1500L, (long) balances.get("You"));
        assertEquals(-1500L, (long) balances.get("Ethan"));
    }

    @Test
    public void settlementsMatchDebtorToCreditor() {
        java.util.List<String> members = java.util.Arrays.asList("You", "Ethan");
        Expense expense = Expense.create("cocoa", "Dinner", 20.0, "You", members, "Food");
        java.util.List<BalanceCalculator.Settlement> settlements =
                BalanceCalculator.settlements(java.util.Collections.singletonList(expense));

        assertEquals(1, settlements.size());
        assertEquals("Ethan", settlements.get(0).from);
        assertEquals("You", settlements.get(0).to);
        assertEquals(1000L, settlements.get(0).amountCents);
    }
}