package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

/**
 * 05 - Group balances. Layout only for now, no navigation wired up yet.
 */
public class GroupBalancesActivity extends AppCompatActivity {

    static final String EXTRA_JUST_JOINED = "just_joined";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_balances);
        String extraGroupId = getIntent().getStringExtra(Extras.GROUP_ID);
        final String groupId = extraGroupId != null ? extraGroupId : "cocoa-beach";
        Group group = GroupStore.get(this, groupId);
        if (group != null) {
            android.widget.TextView title = findViewById(R.id.text_group_title);
            if (title != null) title.setText(group.name);
        }
        java.util.List<Expense> expenses = ExpenseStore.listForGroup(this, groupId);
        java.util.Map<String, Long> balances = BalanceCalculator.balances(expenses);
        long youBalance = balances.containsKey("You") ? balances.get("You") : 0L;
        android.widget.TextView net = findViewById(R.id.text_net_balance);
        android.widget.TextView detail = findViewById(R.id.text_net_detail);
        if (net != null) net.setText(formatMoney(youBalance));
        if (detail != null) {
            detail.setText(youBalance >= 0 ? "People owe you" : "You owe people");
        }
        bindMemberBalance(R.id.text_balance_ethan, balances, "Ethan");
        bindMemberBalance(R.id.text_balance_mohammad, balances, "Mohammad");
        bindMemberBalance(R.id.text_balance_jordan, balances, "Jordan");
        bindMemberBalance(R.id.text_balance_priya, balances, "Priya");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GroupNav.bindBackAndRail(this);

        if (getIntent().getBooleanExtra(EXTRA_JUST_JOINED, false)) {
            String message = getString(R.string.joined_banner_fmt,
                    getString(R.string.group_name_cocoa_beach));
            Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG).show();
        }

        // The landscape layout merges Balances + Settle up into one screen, so it has
        // neither the portrait tabs nor a dedicated "Settle up" button.
        View tabBalances = findViewById(R.id.tab_balances);
        if (tabBalances != null) {
            GroupNav.bindTabs(this);
        }

        View settleUpButton = findViewById(R.id.button_settle_up);
        if (settleUpButton != null) {
            settleUpButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, SettleUpActivity.class);
                intent.putExtra(Extras.GROUP_ID, groupId);
                startActivity(intent);
            });
        }

        findViewById(R.id.button_add_expense).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra(Extras.GROUP_ID, groupId);
            startActivity(intent);
        });
    }

    private void bindMemberBalance(int viewId, java.util.Map<String, Long> balances, String member) {
        android.widget.TextView view = findViewById(viewId);
        if (view != null) {
            view.setText(formatMoney(balances.containsKey(member) ? balances.get(member) : 0L));
            view.setTextColor(view.getContext().getColor(
                    balances.containsKey(member) && balances.get(member) >= 0
                            ? R.color.tally_primary : R.color.tally_negative));
        }
    }

    private String formatMoney(long cents) {
        java.util.Locale locale = java.util.Locale.US;
        return String.format(locale, "%s$%.2f",
                cents >= 0 ? "+" : "-",
                Math.abs(cents) / 100.0);
    }
}
