package com.example.tally;

/** Mock per-member balance row shown on the Group · Balances screen. */
class MemberBalance {
    final String initial;
    final String name;
    final String status;
    final int statusColorRes;

    MemberBalance(String initial, String name, String status, int statusColorRes) {
        this.initial = initial;
        this.name = name;
        this.status = status;
        this.statusColorRes = statusColorRes;
    }
}
