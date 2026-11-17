package com.example.tally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Binds {@link MemberBalance} rows for the "Everyone" list on the Group · Balances screen. */
class MemberBalanceAdapter extends RecyclerView.Adapter<MemberBalanceAdapter.ViewHolder> {

    private final List<MemberBalance> items;

    MemberBalanceAdapter(List<MemberBalance> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_balance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberBalance item = items.get(position);
        holder.avatar.setText(item.initial);
        holder.name.setText(item.name);
        holder.status.setText(item.status);
        holder.status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), item.statusColorRes));
        holder.divider.setVisibility(position == items.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView avatar;
        final TextView name;
        final TextView status;
        final View divider;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_initial);
            name = itemView.findViewById(R.id.text_member_name);
            status = itemView.findViewById(R.id.text_member_status);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
