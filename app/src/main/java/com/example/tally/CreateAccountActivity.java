package com.example.tally;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * 02 - Create account. No backend: validates the fields and saves the account to
 * {@link AuthStore} (SharedPreferences), then signs the user straight in.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private static final int MIN_PASSWORD_LENGTH = 8;

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

        TextInputLayout layoutDisplayName = findViewById(R.id.layout_display_name);
        TextInputLayout layoutEmail = findViewById(R.id.layout_email);
        TextInputEditText displayName = findViewById(R.id.input_display_name);
        TextInputEditText email = findViewById(R.id.input_email);
        TextInputEditText password = findViewById(R.id.input_password);
        TextInputEditText confirm = findViewById(R.id.input_confirm_password);
        TextView error = findViewById(R.id.text_password_error);

        findViewById(R.id.link_back).setOnClickListener(v -> finish());

        findViewById(R.id.button_create_account).setOnClickListener(v -> {
            layoutDisplayName.setError(null);
            layoutEmail.setError(null);
            error.setVisibility(View.GONE);

            String name = String.valueOf(displayName.getText()).trim();
            String emailValue = String.valueOf(email.getText()).trim();
            String first = String.valueOf(password.getText());
            String second = String.valueOf(confirm.getText());

            if (TextUtils.isEmpty(name)) {
                layoutDisplayName.setError(getString(R.string.error_display_name_required));
                return;
            }
            if (TextUtils.isEmpty(emailValue)) {
                layoutEmail.setError(getString(R.string.error_email_required));
                return;
            }
            if (first.length() < MIN_PASSWORD_LENGTH) {
                error.setText(R.string.error_password_too_short);
                error.setVisibility(View.VISIBLE);
                return;
            }
            if (!first.equals(second)) {
                error.setText(R.string.validation_password_mismatch);
                error.setVisibility(View.VISIBLE);
                return;
            }
            if (AuthStore.accountExists(this, emailValue)) {
                layoutEmail.setError(getString(R.string.error_account_exists));
                return;
            }

            AuthStore.createAccount(this, emailValue, first, name);

            Intent intent = new Intent(this, GroupsHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
