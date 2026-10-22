package com.example.tally;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 01 - Sign in. The app's entry point: repurposed from the generated template to host the
 * Figma "Sign in" screen (node 1:4) rather than a separate launcher + sign-in activity pair.
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

        findViewById(R.id.button_sign_in).setOnClickListener(v -> {
            startActivity(new Intent(this, GroupsHomeActivity.class));
            finish();
        });

        findViewById(R.id.link_create_account).setOnClickListener(v ->
                startActivity(new Intent(this, CreateAccountActivity.class)));
    }
}
