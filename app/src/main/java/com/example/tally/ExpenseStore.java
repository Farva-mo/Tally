package com.example.tally;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class ExpenseStore {
    private static final String PREFS_NAME = "tally_expenses";
    private static final String KEY_EXPENSES = "expenses";

    private ExpenseStore() {
    }

    static void save(Context context, Expense expense) {
        List<Expense> expenses = list(context);
        expenses.add(expense);
        JSONArray json = new JSONArray();
        try {
            for (Expense item : expenses) {
                json.put(item.toJson());
            }
        } catch (JSONException ignored) {
            return;
        }
        prefs(context).edit().putString(KEY_EXPENSES, json.toString()).apply();
    }

    static List<Expense> list(Context context) {
        List<Expense> expenses = new ArrayList<>();
        String raw = prefs(context).getString(KEY_EXPENSES, "[]");
        try {
            JSONArray json = new JSONArray(raw);
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.optJSONObject(i);
                if (item != null) {
                    expenses.add(Expense.fromJson(item));
                }
            }
        } catch (JSONException ignored) {
        }
        return expenses;
    }

    static List<Expense> listForGroup(Context context, String groupId) {
        List<Expense> result = new ArrayList<>();
        for (Expense expense : list(context)) {
            if (groupId == null || groupId.equals(expense.groupId)) {
                result.add(expense);
            }
        }
        return result;
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
