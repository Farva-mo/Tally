package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

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

        List<Group> groups = GroupStore.list(this);
        int[] groupCards = {R.id.card_group_1, R.id.card_group_2, R.id.card_group_3};
        for (int i = 0; i < groupCards.length && i < groups.size(); i++) {
            Group group = groups.get(i);
            MaterialCardView card = findViewById(groupCards[i]);
            ((TextView) card.findViewById(R.id.text_group_name_placeholder))
                    .setText(group.name);
            card.setOnClickListener(v -> openGroup(group.id));
        }

        findViewById(R.id.button_new_group).setOnClickListener(v ->
                startActivity(new Intent(this, NewGroupActivity.class)));
    }

    private void openHome() {
        Intent intent = new Intent(this, GroupsHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void openGroup(String groupId) {
        Intent intent = new Intent(this, GroupBalancesActivity.class);
        intent.putExtra(Extras.GROUP_ID, groupId);
        startActivity(intent);
    }
}
