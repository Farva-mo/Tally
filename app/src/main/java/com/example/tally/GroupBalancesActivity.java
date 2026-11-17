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
            settleUpButton.setOnClickListener(v -> GroupNav.openTop(this, SettleUpActivity.class));
        }

        findViewById(R.id.button_add_expense).setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class)));
    }
}
