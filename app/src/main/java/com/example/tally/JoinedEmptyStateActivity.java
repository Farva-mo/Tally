package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

/** 12 - Joined · no expenses yet (empty state) (node 7:55). */
public class JoinedEmptyStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_empty_state);

        String groupName = getIntent().getStringExtra(Extras.GROUP_NAME);
        if (TextUtils.isEmpty(groupName)) {
            groupName = getString(R.string.group_name_cocoa_beach);
        }

        TextView title = findViewById(R.id.text_app_bar_title);
        title.setText(groupName);
        findViewById(R.id.button_back).setOnClickListener(v -> finish());
        findViewById(R.id.button_app_bar_action).setOnClickListener(v ->
                Toast.makeText(this, R.string.action_coming_soon, Toast.LENGTH_SHORT).show());

        TextView memberCount = findViewById(R.id.text_member_count);
        memberCount.setText(getString(R.string.members_count_fmt, 5));

        TextView banner = findViewById(R.id.text_joined_banner);
        banner.setText("✓  " + getString(R.string.joined_banner_fmt, groupName));

        TabLayout tabs = findViewById(R.id.group_tabs);
        NavHelper.setupGroupTabs(this, tabs, 0, groupName);

        String finalGroupName = groupName;
        findViewById(R.id.button_add_first_expense).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddExpenseActivity.class);
            intent.putExtra(Extras.GROUP_NAME, finalGroupName);
            startActivity(intent);
        });

        TextView inviteMore = findViewById(R.id.button_invite_more);
        inviteMore.setText(getString(R.string.action_invite_more_members_fmt, getString(R.string.sample_join_code)));
        inviteMore.setOnClickListener(v ->
                Toast.makeText(this, R.string.share_join_code_label, Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_groups).setOnClickListener(v ->
                GroupNav.openTop(this, GroupsHomeActivity.class));
        findViewById(R.id.nav_home).setOnClickListener(v ->
                GroupNav.openTop(this, GroupsHomeActivity.class));
        findViewById(R.id.nav_profile).setOnClickListener(v ->
                GroupNav.openTop(this, ProfileSettingsActivity.class));
    }
}
