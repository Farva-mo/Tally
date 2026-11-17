package com.example.tally;

/** Plain mock data holder for a row on the Groups home screen. No persistence/backend yet. */
class GroupItem {
    final String name;
    final int members;
    final int expenses;
    final double balance; // positive = owed to you, negative = you owe, 0 = settled
    final boolean settled;

    GroupItem(String name, int members, int expenses, double balance, boolean settled) {
        this.name = name;
        this.members = members;
        this.expenses = expenses;
        this.balance = balance;
        this.settled = settled;
    }
}
