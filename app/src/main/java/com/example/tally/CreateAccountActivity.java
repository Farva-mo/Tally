package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

/**
 * 02 - Create account. Fake flow for now: no backend, just checks the two passwords match
 * and moves on to the groups home.
 */
public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputEditText password = findViewById(R.id.input_password);
        TextInputEditText confirm = findViewById(R.id.input_confirm_password);
        TextView error = findViewById(R.id.text_password_error);

        findViewById(R.id.link_back).setOnClickListener(v -> finish());

        findViewById(R.id.button_create_account).setOnClickListener(v -> {
            String first = String.valueOf(password.getText());
            String second = String.valueOf(confirm.getText());
            if (!first.equals(second)) {
                error.setVisibility(View.VISIBLE);
                return;
            }
            error.setVisibility(View.GONE);

            Intent intent = new Intent(this, GroupsHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
