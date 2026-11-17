package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * 01 - Sign in. The app's entry point: repurposed from the generated template to host the
 * Figma "Sign in" screen (node 1:4) rather than a separate launcher + sign-in activity pair.
 * Accounts live in {@link AuthStore} (SharedPreferences, no backend).
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AuthStore.seedDemoAccountIfNeeded(this);

        TextInputLayout layoutEmail = findViewById(R.id.layout_email);
        TextInputLayout layoutPassword = findViewById(R.id.layout_password);
        TextInputEditText inputEmail = findViewById(R.id.input_email);
        TextInputEditText inputPassword = findViewById(R.id.input_password);

        findViewById(R.id.button_sign_in).setOnClickListener(v -> {
            layoutEmail.setError(null);
            layoutPassword.setError(null);

            String email = String.valueOf(inputEmail.getText()).trim();
            String password = String.valueOf(inputPassword.getText());

            if (TextUtils.isEmpty(email)) {
                layoutEmail.setError(getString(R.string.error_email_required));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                layoutPassword.setError(getString(R.string.error_password_required));
                return;
            }
            if (!AuthStore.verify(this, email, password)) {
                layoutPassword.setError(getString(R.string.error_invalid_credentials));
                return;
            }

            startActivity(new Intent(this, GroupsHomeActivity.class));
            finish();
        });

        findViewById(R.id.link_create_account).setOnClickListener(v ->
                startActivity(new Intent(this, CreateAccountActivity.class)));
    }
}
