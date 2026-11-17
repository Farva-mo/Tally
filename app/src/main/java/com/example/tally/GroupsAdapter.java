package com.example.tally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

/** Binds the mock {@link GroupItem} list to cards on the Groups home screen. */
class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    interface OnGroupClickListener {
        void onGroupClick(GroupItem item);
    }

    private final List<GroupItem> items;
    private final OnGroupClickListener listener;

    GroupsAdapter(List<GroupItem> items, OnGroupClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupItem item = items.get(position);
        holder.name.setText(item.name);
        holder.subtitle.setText(item.settled
                ? holder.itemView.getContext().getString(R.string.group_settled_fmt, item.members)
                : holder.itemView.getContext().getString(R.string.group_members_expenses_fmt, item.members, item.expenses));

        String amount;
        int color;
        if (item.balance > 0) {
            amount = String.format(Locale.US, "+$%.2f", item.balance);
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.tally_primary);
        } else if (item.balance < 0) {
            amount = String.format(Locale.US, "−$%.2f", Math.abs(item.balance));
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.tally_negative);
        } else {
            amount = "$0.00";
            color = ContextCompat.getColor(holder.itemView.getContext(), R.color.tally_text_secondary);
        }
        holder.balance.setText(amount);
        holder.balance.setTextColor(color);

        holder.itemView.setOnClickListener(v -> listener.onGroupClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView subtitle;
        final TextView balance;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_group_name);
            subtitle = itemView.findViewById(R.id.text_group_subtitle);
            balance = itemView.findViewById(R.id.text_group_balance);
        }
    }
}
