package com.example.tally;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class Expense {
    final String id;
    final String groupId;
    final String description;
    final long amountCents;
    final String paidBy;
    final List<String> splitWith;
    final long createdAt;
    final String category;

    Expense(String id, String groupId, String description, long amountCents,
            String paidBy, List<String> splitWith, long createdAt, String category) {
        this.id = id;
        this.groupId = groupId;
        this.description = description;
        this.amountCents = amountCents;
        this.paidBy = paidBy;
        this.splitWith = splitWith;
        this.createdAt = createdAt;
        this.category = category;
    }

    static Expense create(String groupId, String description, double amount,
                          String paidBy, List<String> splitWith, String category) {
        long cents = Math.round(amount * 100.0);
        return new Expense(UUID.randomUUID().toString(), groupId, description, cents,
                paidBy, splitWith, System.currentTimeMillis(), category);
    }

    JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("groupId", groupId);
        json.put("description", description);
        json.put("amountCents", amountCents);
        json.put("paidBy", paidBy);
        json.put("createdAt", createdAt);
        json.put("category", category);
        JSONArray members = new JSONArray();
        for (String member : splitWith) {
            members.put(member);
        }
        json.put("splitWith", members);
        return json;
    }

    static Expense fromJson(JSONObject json) throws JSONException {
        List<String> members = new ArrayList<>();
        JSONArray memberJson = json.optJSONArray("splitWith");
        if (memberJson != null) {
            for (int i = 0; i < memberJson.length(); i++) {
                members.add(memberJson.optString(i));
            }
        }
        return new Expense(
                json.optString("id"),
                json.optString("groupId"),
                json.optString("description"),
                json.optLong("amountCents"),
                json.optString("paidBy"),
                members,
                json.optLong("createdAt"),
                json.optString("category", "Other"));
    }
}
