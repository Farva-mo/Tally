package com.example.tally;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Local, on-device account store backed by SharedPreferences. There is no backend, so
 * passwords are kept in plain text here -- acceptable for this prototype, not for a shipped app.
 */
final class AuthStore {

    private static final String PREFS_NAME = "tally_accounts";
    private static final String KEY_PASSWORD_PREFIX = "password_";
    private static final String KEY_NAME_PREFIX = "name_";

    static final String DEMO_EMAIL = "demo@tally.app";
    static final String DEMO_PASSWORD = "tally1234";
    private static final String DEMO_DISPLAY_NAME = "Demo User";

    private AuthStore() {
    }

    /** Seeds a ready-to-use demo account on first run so sign-in works without creating one. */
    static void seedDemoAccountIfNeeded(Context context) {
        if (!accountExists(context, DEMO_EMAIL)) {
            createAccount(context, DEMO_EMAIL, DEMO_PASSWORD, DEMO_DISPLAY_NAME);
        }
    }

    static boolean accountExists(Context context, String email) {
        return prefs(context).contains(KEY_PASSWORD_PREFIX + normalizedKey(email));
    }

    static void createAccount(Context context, String email, String password, String displayName) {
        String key = normalizedKey(email);
        prefs(context).edit()
                .putString(KEY_PASSWORD_PREFIX + key, password)
                .putString(KEY_NAME_PREFIX + key, displayName)
                .apply();
    }

    static boolean verify(Context context, String email, String password) {
        String stored = prefs(context).getString(KEY_PASSWORD_PREFIX + normalizedKey(email), null);
        return stored != null && stored.equals(password);
    }

    static String getDisplayName(Context context, String email) {
        return prefs(context).getString(KEY_NAME_PREFIX + normalizedKey(email), null);
    }

    private static String normalizedKey(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
