package com.example.tally;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class GroupStore {
    private static final String PREFS_NAME = "tally_groups";
    private static final String KEY_GROUPS = "groups";
    private static final String KEY_SEEDED = "seeded";

    private GroupStore() {
    }

    static List<Group> list(Context context) {
        seedIfNeeded(context);
        SharedPreferences prefs = prefs(context);
        String raw = prefs.getString(KEY_GROUPS, "[]");
        List<Group> groups = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(raw);
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.optJSONObject(i);
                if (item != null) {
                    groups.add(Group.fromJson(item));
                }
            }
        } catch (JSONException ignored) {
            prefs.edit().remove(KEY_GROUPS).apply();
        }
        return groups;
    }

    static Group get(Context context, String id) {
        if (id == null) {
            return null;
        }
        for (Group group : list(context)) {
            if (id.equals(group.id)) {
                return group;
            }
        }
        return null;
    }

    static Group findByJoinCode(Context context, String joinCode) {
        if (joinCode == null) {
            return null;
        }
        String normalized = joinCode.trim().toUpperCase();
        for (Group group : list(context)) {
            if (normalized.equals(group.joinCode)) {
                return group;
            }
        }
        return null;
    }

    static Group create(Context context, String name) {
        String code;
        do {
            code = generateJoinCode();
        } while (findByJoinCode(context, code) != null);

        Group group = new Group(UUID.randomUUID().toString(), name.trim(), code);
        save(context, group);
        return group;
    }

    private static void save(Context context, Group group) {
        List<Group> groups = list(context);
        groups.add(group);
        write(context, groups);
    }

    private static void write(Context context, List<Group> groups) {
        JSONArray json = new JSONArray();
        try {
            for (Group group : groups) {
                json.put(group.toJson());
            }
        } catch (JSONException ignored) {
            return;
        }
        prefs(context).edit().putString(KEY_GROUPS, json.toString()).apply();
    }

    private static void seedIfNeeded(Context context) {
        SharedPreferences prefs = prefs(context);
        if (prefs.getBoolean(KEY_SEEDED, false)) {
            return;
        }
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("cocoa-beach", "Cocoa Beach trip", "COCOA1"));
        groups.add(new Group("cabin-weekend", "Cabin Weekend", "CABIN1"));
        groups.add(new Group("dinner-club", "Dinner Club", "DINNER"));
        write(context, groups);
        prefs.edit().putBoolean(KEY_SEEDED, true).apply();
    }

    private static String generateJoinCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder code = new StringBuilder(6);
        java.util.Random random = new java.security.SecureRandom();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
