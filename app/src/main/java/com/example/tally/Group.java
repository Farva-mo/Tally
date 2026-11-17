package com.example.tally;

import org.json.JSONException;
import org.json.JSONObject;

final class Group {
    final String id;
    final String name;
    final String joinCode;

    Group(String id, String name, String joinCode) {
        this.id = id;
        this.name = name;
        this.joinCode = joinCode;
    }

    JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("joinCode", joinCode);
        return json;
    }

    static Group fromJson(JSONObject json) {
        return new Group(
                json.optString("id"),
                json.optString("name"),
                json.optString("joinCode"));
    }
}
