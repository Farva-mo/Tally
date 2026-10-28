package com.example.tally;

import android.app.Activity;
import android.content.Intent;

/**
 * Navigation shared by the group pages (Balances, Expenses, Settle up): back arrow, the three
 * tabs and the bottom nav.
 */
final class GroupNav {

    private GroupNav() {
    }

    static void bind(Activity activity) {
        activity.findViewById(R.id.button_back).setOnClickListener(v ->
                openTop(activity, GroupsHomeActivity.class));
        activity.findViewById(R.id.nav_home).setOnClickListener(v ->
                openTop(activity, GroupsHomeActivity.class));
        activity.findViewById(R.id.nav_groups).setOnClickListener(v ->
                openTop(activity, GroupsHomeActivity.class));

        activity.findViewById(R.id.nav_profile).setOnClickListener(v ->
                openTop(activity, ProfileSettingsActivity.class));

        activity.findViewById(R.id.tab_balances).setOnClickListener(v ->
                openTop(activity, GroupBalancesActivity.class));
        activity.findViewById(R.id.tab_expenses).setOnClickListener(v ->
                openTop(activity, ExpenseHistoryActivity.class));
        activity.findViewById(R.id.tab_settle_up).setOnClickListener(v ->
                openTop(activity, SettleUpActivity.class));
    }

    /** Opens a screen, reusing it if it is already in the back stack. */
    static void openTop(Activity from, Class<? extends Activity> target) {
        if (from.getClass() == target) {
            return;
        }
        Intent intent = new Intent(from, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        from.startActivity(intent);
    }
}
