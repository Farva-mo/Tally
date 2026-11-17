package com.example.tally;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * 06 - Add expense. Validates the amount, description and split before accepting it; no
 * backend yet, so nothing is persisted once the validation passes.
 */
public class AddExpenseActivity extends AppCompatActivity {

    private static final String KEY_AMOUNT_ERROR = "amount_error";
    private static final String KEY_DESCRIPTION_ERROR = "description_error";
    private static final String KEY_SPLIT_WARNING = "split_warning";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputLayout layoutAmount = findViewById(R.id.layout_amount);
        TextInputLayout layoutDescription = findViewById(R.id.layout_description);
        TextInputEditText inputAmount = findViewById(R.id.input_amount);
        TextInputEditText inputDescription = findViewById(R.id.input_description);
        TextView splitWarning = findViewById(R.id.text_split_warning);

        // Restore validation messages across rotation -- TextInputLayout.setError() and a plain
        // TextView's visibility are both runtime-only state that Android does not restore itself.
        if (savedInstanceState != null) {
            layoutAmount.setError(savedInstanceState.getString(KEY_AMOUNT_ERROR));
            layoutDescription.setError(savedInstanceState.getString(KEY_DESCRIPTION_ERROR));
            String splitWarningText = savedInstanceState.getString(KEY_SPLIT_WARNING);
            if (splitWarningText != null) {
                splitWarning.setText(splitWarningText);
                splitWarning.setVisibility(View.VISIBLE);
            }
        }

        CheckBox[] splitMembers = {
                findViewById(R.id.check_you),
                findViewById(R.id.check_ethan),
                findViewById(R.id.check_mohammad),
                findViewById(R.id.check_jordan),
                findViewById(R.id.check_priya),
        };

        findViewById(R.id.button_cancel).setOnClickListener(v -> finish());

        findViewById(R.id.button_save).setOnClickListener(v -> {
            layoutAmount.setError(null);
            layoutDescription.setError(null);
            splitWarning.setVisibility(View.GONE);

            String amountText = String.valueOf(inputAmount.getText()).trim();
            String description = String.valueOf(inputDescription.getText()).trim();

            if (TextUtils.isEmpty(amountText)) {
                layoutAmount.setError(getString(R.string.error_amount_required));
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                layoutAmount.setError(getString(R.string.error_amount_invalid));
                return;
            }
            if (amount <= 0) {
                layoutAmount.setError(getString(R.string.error_amount_invalid));
                return;
            }

            if (TextUtils.isEmpty(description)) {
                layoutDescription.setError(getString(R.string.error_description_required));
                return;
            }

            boolean anyMemberChecked = false;
            for (CheckBox member : splitMembers) {
                if (member.isChecked()) {
                    anyMemberChecked = true;
                    break;
                }
            }
            if (!anyMemberChecked) {
                splitWarning.setText(R.string.error_split_empty);
                splitWarning.setVisibility(View.VISIBLE);
                return;
            }

            String groupId = getIntent().getStringExtra(Extras.GROUP_ID);
            if (groupId == null) {
                groupId = "cocoa-beach";
            }
            java.util.ArrayList<String> members = new java.util.ArrayList<>();
            if (splitMembers[0].isChecked()) members.add("You");
            if (splitMembers[1].isChecked()) members.add("Ethan");
            if (splitMembers[2].isChecked()) members.add("Mohammad");
            if (splitMembers[3].isChecked()) members.add("Jordan");
            if (splitMembers[4].isChecked()) members.add("Priya");
            Expense expense = Expense.create(groupId, description, amount, "You", members, "Other");
            ExpenseStore.save(this, expense);

            Toast.makeText(this, R.string.snackbar_expense_added, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextInputLayout layoutAmount = findViewById(R.id.layout_amount);
        TextInputLayout layoutDescription = findViewById(R.id.layout_description);
        TextView splitWarning = findViewById(R.id.text_split_warning);
        if (layoutAmount.getError() != null) {
            outState.putString(KEY_AMOUNT_ERROR, layoutAmount.getError().toString());
        }
        if (layoutDescription.getError() != null) {
            outState.putString(KEY_DESCRIPTION_ERROR, layoutDescription.getError().toString());
        }
        if (splitWarning.getVisibility() == View.VISIBLE) {
            outState.putString(KEY_SPLIT_WARNING, splitWarning.getText().toString());
        }
    }
}
