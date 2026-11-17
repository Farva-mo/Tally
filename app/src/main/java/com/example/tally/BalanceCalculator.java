package com.example.tally;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class BalanceCalculator {
    static final class Settlement {
        final String from;
        final String to;
        final long amountCents;

        Settlement(String from, String to, long amountCents) {
            this.from = from;
            this.to = to;
            this.amountCents = amountCents;
        }
    }

    private BalanceCalculator() {
    }

    static Map<String, Long> balances(List<Expense> expenses) {
        Map<String, Long> balances = new LinkedHashMap<>();
        for (Expense expense : expenses) {
            balances.putIfAbsent(expense.paidBy, 0L);
            for (String member : expense.splitWith) {
                balances.putIfAbsent(member, 0L);
            }
            balances.put(expense.paidBy,
                    balances.get(expense.paidBy) + expense.amountCents);
            if (!expense.splitWith.isEmpty()) {
                long baseShare = expense.amountCents / expense.splitWith.size();
                long remainder = expense.amountCents % expense.splitWith.size();
                for (int i = 0; i < expense.splitWith.size(); i++) {
                    String member = expense.splitWith.get(i);
                    long share = baseShare + (i < remainder ? 1 : 0);
                    balances.put(member, balances.get(member) - share);
                }
            }
        }
        return balances;
    }

    static List<Settlement> settlements(List<Expense> expenses) {
        Map<String, Long> balances = balances(expenses);
        List<Map.Entry<String, Long>> debtors = new ArrayList<>();
        List<Map.Entry<String, Long>> creditors = new ArrayList<>();
        for (Map.Entry<String, Long> entry : balances.entrySet()) {
            if (entry.getValue() < 0) {
                debtors.add(entry);
            } else if (entry.getValue() > 0) {
                creditors.add(entry);
            }
        }
        return settle(debtors, creditors);
    }

    private static List<Settlement> settle(List<Map.Entry<String, Long>> debtors,
                                           List<Map.Entry<String, Long>> creditors) {
        List<Settlement> settlements = new ArrayList<>();
        int debtorIndex = 0;
        int creditorIndex = 0;
        while (debtorIndex < debtors.size() && creditorIndex < creditors.size()) {
            Map.Entry<String, Long> debtor = debtors.get(debtorIndex);
            Map.Entry<String, Long> creditor = creditors.get(creditorIndex);
            long amount = Math.min(-debtor.getValue(), creditor.getValue());
            settlements.add(new Settlement(debtor.getKey(), creditor.getKey(), amount));
            debtor.setValue(debtor.getValue() + amount);
            creditor.setValue(creditor.getValue() - amount);
            if (debtor.getValue() == 0) debtorIndex++;
            if (creditor.getValue() == 0) creditorIndex++;
        }
        return settlements;
    }
}
