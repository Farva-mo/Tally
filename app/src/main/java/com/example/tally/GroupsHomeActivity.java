package com.example.tally;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 03 - Groups (home). Shows hard-coded sample groups for now.
 */
public class GroupsHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_groups_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.nav_home).setOnClickListener(v -> openHome());
        findViewById(R.id.nav_profile).setOnClickListener(v ->
                GroupNav.openTop(this, ProfileSettingsActivity.class));

        // Only Cocoa Beach has fake data, so every group card opens the same balances page.
        int[] groupCards = {R.id.card_group_1, R.id.card_group_2, R.id.card_group_3};
        for (int id : groupCards) {
            findViewById(id).setOnClickListener(v ->
                    startActivity(new Intent(this, GroupBalancesActivity.class)));
        }

        findViewById(R.id.button_new_group).setOnClickListener(v ->
                startActivity(new Intent(this, NewGroupActivity.class)));
    }

    private void openHome() {
        Intent intent = new Intent(this, GroupsHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
