package com.example.tally;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.SecureRandom;

/**
 * 04 - Add a group. "Create new" generates a random 6-character join code; "Join with code"
 * takes one. No backend yet, so nothing is saved.
 */
public class NewGroupActivity extends AppCompatActivity {

    // No 0/O or 1/I so codes are easy to read out loud.
    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;

    private final SecureRandom random = new SecureRandom();
    private String joinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View panelCreate = findViewById(R.id.panel_create);
        View panelJoin = findViewById(R.id.panel_join);
        View cardJoinCode = findViewById(R.id.card_join_code);
        TextView textJoinCode = findViewById(R.id.text_join_code);
        TextInputLayout layoutGroupName = findViewById(R.id.layout_group_name);
        TextInputEditText inputGroupName = findViewById(R.id.input_group_name);
        TextInputLayout layoutJoinCode = findViewById(R.id.layout_join_code);
        TextInputEditText inputJoinCode = findViewById(R.id.input_join_code);

        findViewById(R.id.link_back).setOnClickListener(v -> finish());

        findViewById(R.id.nav_profile).setOnClickListener(v ->
                GroupNav.openTop(this, ProfileSettingsActivity.class));

        findViewById(R.id.nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupsHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        MaterialButtonToggleGroup toggle = findViewById(R.id.toggle_mode);
        toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                return;
            }
            boolean creating = checkedId == R.id.segment_create;
            panelCreate.setVisibility(creating ? View.VISIBLE : View.GONE);
            panelJoin.setVisibility(creating ? View.GONE : View.VISIBLE);
        });

        findViewById(R.id.button_create_group).setOnClickListener(v -> {
            String name = String.valueOf(inputGroupName.getText()).trim();
            if (name.isEmpty()) {
                layoutGroupName.setError(getString(R.string.error_group_name_required));
                return;
            }
            layoutGroupName.setError(null);

            joinCode = generateJoinCode();
            textJoinCode.setText(formatJoinCode(joinCode));
            cardJoinCode.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.button_copy_code).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("Join code", joinCode));
            Toast.makeText(this, R.string.code_copied, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.button_share_code).setOnClickListener(v -> {
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_join_code_message, joinCode));
            startActivity(Intent.createChooser(send, getString(R.string.share_join_code_chooser)));
        });

        findViewById(R.id.button_join_group).setOnClickListener(v -> {
            String code = String.valueOf(inputJoinCode.getText()).trim();
            if (code.length() != CODE_LENGTH) {
                layoutJoinCode.setError(getString(R.string.error_join_code_invalid));
                return;
            }
            layoutJoinCode.setError(null);
            Intent confirm = new Intent(this, ConfirmJoinTripActivity.class);
            confirm.putExtra(ConfirmJoinTripActivity.EXTRA_JOIN_CODE, code);
            startActivity(confirm);
        });
    }

    private String generateJoinCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return code.toString();
    }

    private String formatJoinCode(String code) {
        return code.substring(0, 3) + " · " + code.substring(3);
    }
}
