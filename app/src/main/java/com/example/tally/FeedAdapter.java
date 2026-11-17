package com.example.tally;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Renders the mixed header / row feed on the Expense history screen. */
class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FeedEntry> entries;

    FeedAdapter(List<FeedEntry> entries) {
        this.entries = entries;
    }

    @Override
    public int getItemViewType(int position) {
        return entries.get(position).type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == FeedEntry.TYPE_HEADER) {
            return new HeaderHolder(inflater.inflate(R.layout.item_feed_header, parent, false));
        }
        return new RowHolder(inflater.inflate(R.layout.item_feed_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FeedEntry entry = entries.get(position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).header.setText(entry.header);
        } else if (holder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) holder;
            rowHolder.title.setText(entry.title);
            rowHolder.subtitle.setText(entry.subtitle);
            rowHolder.amount.setText(entry.amount);
            rowHolder.caption.setText(entry.caption);
            if (entry.settlement) {
                rowHolder.icon.setText("✓");
                rowHolder.icon.setBackgroundResource(R.drawable.bg_square_icon_light);
                rowHolder.icon.setTextColor(rowHolder.icon.getResources().getColor(R.color.tally_primary, null));
            } else {
                rowHolder.icon.setText("");
                rowHolder.icon.setBackgroundResource(R.drawable.bg_square_icon_gray);
            }
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        final TextView header;

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
            header = (TextView) itemView;
        }
    }

    static class RowHolder extends RecyclerView.ViewHolder {
        final TextView icon;
        final TextView title;
        final TextView subtitle;
        final TextView amount;
        final TextView caption;

        RowHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_square);
            title = itemView.findViewById(R.id.text_feed_title);
            subtitle = itemView.findViewById(R.id.text_feed_subtitle);
            amount = itemView.findViewById(R.id.text_feed_amount);
            caption = itemView.findViewById(R.id.text_feed_caption);
        }
    }
}
