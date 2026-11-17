package com.example.tally;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

/**
 * Small shared helpers for wiring up the bottom navigation bar and the group-level
 * (Balances / Expenses / Settle up) tabs that repeat across several screens. This is a plain
 * static helper, not an architectural layer -- each Activity still owns its own navigation.
 */
final class NavHelper {

    private NavHelper() {
    }

    /** Wires a BottomNavigationView, marking {@code selectedItemId} as the current tab. */
    static void setupBottomNav(Activity activity, BottomNavigationView nav, int selectedItemId) {
        nav.setSelectedItemId(selectedItemId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) {
                return true;
            }
            if (id == R.id.nav_groups) {
                activity.startActivity(new Intent(activity, GroupsHomeActivity.class));
                activity.finish();
                return true;
            } else if (id == R.id.nav_profile) {
                activity.startActivity(new Intent(activity, ProfileSettingsActivity.class));
                activity.finish();
                return true;
            } else {
                Toast.makeText(activity, R.string.action_coming_soon, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /**
     * Wires the Balances / Expenses / Settle up TabLayout, selecting {@code selectedIndex}
     * (0 = Balances, 1 = Expenses, 2 = Settle up) and navigating to the matching screen when a
     * different tab is tapped.
     */
    static void setupGroupTabs(Activity activity, TabLayout tabs, int selectedIndex, String groupName) {
        TabLayout.Tab current = tabs.getTabAt(selectedIndex);
        if (current != null) {
            current.select();
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == selectedIndex) {
                    return;
                }
                Class<? extends Activity> target;
                if (position == 0) {
                    target = GroupBalancesActivity.class;
                } else if (position == 1) {
                    target = ExpenseHistoryActivity.class;
                } else {
                    target = SettleUpActivity.class;
                }
                Intent intent = new Intent(activity, target);
                intent.putExtra(Extras.GROUP_NAME, groupName);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
