package com.example.tally;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 07 - Settle up. Layout only for now, no navigation wired up yet.
 */
public class SettleUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settle_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GroupNav.bind(this);

        findViewById(R.id.button_history).setOnClickListener(v ->
                GroupNav.openTop(this, ExpenseHistoryActivity.class));

        String groupId = getIntent().getStringExtra(Extras.GROUP_ID);
        if (groupId == null) groupId = "cocoa-beach";
        List<Expense> expenses = ExpenseStore.listForGroup(this, groupId);
        List<BalanceCalculator.Settlement> settlements = BalanceCalculator.settlements(expenses);
        bindPaymentCard(R.id.card_payment_2, R.id.text_payment_route_2,
                R.id.text_payment_amount_2, R.id.button_mark_settled_2, settlements, 1);
        bindPaymentCard(R.id.card_payment_3, R.id.text_payment_route_3,
                R.id.text_payment_amount_3, R.id.button_mark_settled_3, settlements, 2);
        if (!settlements.isEmpty()) {
            bindPaymentCard(R.id.card_payment_1, R.id.text_payment_route_1,
                    R.id.text_payment_amount_1, R.id.button_mark_settled_1, settlements, 0);
        } else {
            View firstCard = (View) findViewById(R.id.button_mark_settled_1).getParent().getParent();
            firstCard.setVisibility(View.GONE);
        }
        if (settlements.size() > 1) {
            findViewById(R.id.card_payment_2).setVisibility(View.VISIBLE);
        }
        if (settlements.size() > 2) {
            findViewById(R.id.card_payment_3).setVisibility(View.VISIBLE);
        }
    }

    private void bindPaymentCard(Integer cardId, int routeId, int amountId, int buttonId,
                                 List<BalanceCalculator.Settlement> settlements, int index) {
        if (index >= settlements.size()) {
            if (cardId != null) findViewById(cardId).setVisibility(View.GONE);
            return;
        }
        BalanceCalculator.Settlement settlement = settlements.get(index);
        TextView route = findViewById(routeId);
        TextView amount = findViewById(amountId);
        route.setText(settlement.from + " pays " + settlement.to);
        amount.setText(String.format(Locale.US, "$%.2f", settlement.amountCents / 100.0));
        findViewById(buttonId).setOnClickListener(v -> {
            findViewById(cardId != null ? cardId : R.id.button_mark_settled_1).setVisibility(View.GONE);
            Toast.makeText(this, "Marked as settled", Toast.LENGTH_SHORT).show();
        });
    }
}
