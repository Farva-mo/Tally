package com.example.tally;

/**
 * Intent extra key constants shared across activities. Centralised here so screens
 * passing mock data to one another (group name, join code, etc.) agree on the key names.
 */
public final class Extras {
    private Extras() {
    }

    public static final String GROUP_NAME = "extra_group_name";
    public static final String JOIN_CODE = "extra_join_code";
}
