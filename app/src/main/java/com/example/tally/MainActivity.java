package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * 01 - Sign in. The app's entry point: repurposed from the generated template to host the
 * Figma "Sign in" screen (node 1:4) rather than a separate launcher + sign-in activity pair.
 * Accounts live in {@link AuthStore} (SharedPreferences, no backend).
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_EMAIL_ERROR = "email_error";
    private static final String KEY_PASSWORD_ERROR = "password_error";

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

        // Restore validation errors across rotation -- TextInputLayout.setError() is
        // runtime-only state that Android does not restore on its own.
        if (savedInstanceState != null) {
            layoutEmail.setError(savedInstanceState.getString(KEY_EMAIL_ERROR));
            layoutPassword.setError(savedInstanceState.getString(KEY_PASSWORD_ERROR));
        }

        findViewById(R.id.button_sign_in).setOnClickListener(v -> {
            layoutEmail.setError(null);
            layoutPassword.setError(null);

            String email = String.valueOf(inputEmail.getText()).trim();
            String password = String.valueOf(inputPassword.getText());

            if (TextUtils.isEmpty(email)) {
                layoutEmail.setError(getString(R.string.error_email_required));
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                layoutEmail.setError(getString(R.string.error_email_invalid));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                layoutPassword.setError(getString(R.string.error_password_required));
                return;
            }
            if (!AuthStore.verify(this, email, password)) {
                layoutPassword.setError(getString(R.string.error_invalid_credentials));
                Snackbar.make(findViewById(R.id.main), R.string.error_invalid_credentials,
                        Snackbar.LENGTH_LONG).show();
                return;
            }

            startActivity(new Intent(this, GroupsHomeActivity.class));
            finish();
        });

        findViewById(R.id.link_create_account).setOnClickListener(v ->
                startActivity(new Intent(this, CreateAccountActivity.class)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextInputLayout layoutEmail = findViewById(R.id.layout_email);
        TextInputLayout layoutPassword = findViewById(R.id.layout_password);
        if (layoutEmail.getError() != null) {
            outState.putString(KEY_EMAIL_ERROR, layoutEmail.getError().toString());
        }
        if (layoutPassword.getError() != null) {
            outState.putString(KEY_PASSWORD_ERROR, layoutPassword.getError().toString());
        }
    }
}
