package com.example.tally;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 08 - Expense history. Layout only for now, no navigation wired up yet.
 */
public class ExpenseHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GroupNav.bind(this);
        String groupId = getIntent().getStringExtra(Extras.GROUP_ID);
        if (groupId == null) groupId = "cocoa-beach";
        List<Expense> expenses = ExpenseStore.listForGroup(this, groupId);
        LinearLayout container = findViewById(R.id.layout_expense_container);
        if (container != null) {
            container.removeAllViews();
            if (expenses.isEmpty()) {
                TextView empty = new TextView(this);
                empty.setText("No expenses yet");
                empty.setGravity(Gravity.CENTER);
                empty.setPadding(0, 32, 0, 32);
                container.addView(empty);
            } else {
                for (Expense expense : expenses) {
                    TextView row = new TextView(this);
                    row.setText(String.format(Locale.US, "%s\nPaid by %s  •  $%.2f",
                            expense.description, expense.paidBy, expense.amountCents / 100.0));
                    row.setPadding(24, 24, 24, 24);
                    row.setTextColor(getColor(R.color.tally_text_primary));
                    container.addView(row);
                }
            }
        }
        TextView clear = findViewById(R.id.chip_clear);
        if (clear != null) clear.setOnClickListener(v ->
                android.widget.Toast.makeText(this, "Filters cleared", android.widget.Toast.LENGTH_SHORT).show());
        TextView members = findViewById(R.id.chip_members);
        if (members != null) members.setOnClickListener(v ->
                android.widget.Toast.makeText(this, "Showing all members", android.widget.Toast.LENGTH_SHORT).show());
        TextView category = findViewById(R.id.chip_category);
        if (category != null) category.setOnClickListener(v ->
                android.widget.Toast.makeText(this, "Showing saved expenses", android.widget.Toast.LENGTH_SHORT).show());
    }
}
