package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 11 - Confirm and join. Always shows the Cocoa Beach trip (fake data). "Join this group" opens
 * the group's balances page with a "You joined" popup.
 */
public class ConfirmJoinTripActivity extends AppCompatActivity {

    static final String EXTRA_JOIN_CODE = "join_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_join_trip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String code = getIntent().getStringExtra(EXTRA_JOIN_CODE);
        Group group = GroupStore.findByJoinCode(this, code);
        if (group == null) {
            finish();
            return;
        }
        TextView groupName = findViewById(R.id.text_confirm_group_name);
        if (groupName != null) {
            groupName.setText(group.name);
        }
        if (code != null && code.length() == 6) {
            String formatted = code.substring(0, 3) + " · " + code.substring(3);
            ((TextView) findViewById(R.id.text_code_matched))
                    .setText(getString(R.string.code_matched_fmt, formatted));
        }

        findViewById(R.id.button_back).setOnClickListener(v -> finish());
        findViewById(R.id.button_not_now).setOnClickListener(v -> finish());

        findViewById(R.id.button_join_this_group).setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupBalancesActivity.class);
            intent.putExtra(GroupBalancesActivity.EXTRA_JUST_JOINED, true);
            intent.putExtra(Extras.GROUP_ID, group.id);
            startActivity(intent);
            finish();
        });
    }
}
