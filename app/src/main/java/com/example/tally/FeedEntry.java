package com.example.tally;

/**
 * One row in the Expense history feed: either a section header ("THIS WEEK") or an expense /
 * settlement row. Kept as a single simple model with a type flag rather than a class hierarchy,
 * since the feed is small, static mock data.
 */
class FeedEntry {
    static final int TYPE_HEADER = 0;
    static final int TYPE_ROW = 1;

    final int type;
    final String header;

    final String title;
    final String subtitle;
    final String amount;
    final String caption;
    final boolean settlement;

    private FeedEntry(int type, String header, String title, String subtitle, String amount, String caption, boolean settlement) {
        this.type = type;
        this.header = header;
        this.title = title;
        this.subtitle = subtitle;
        this.amount = amount;
        this.caption = caption;
        this.settlement = settlement;
    }

    static FeedEntry header(String text) {
        return new FeedEntry(TYPE_HEADER, text, null, null, null, null, false);
    }

    static FeedEntry expense(String title, String subtitle, String amount, String caption) {
        return new FeedEntry(TYPE_ROW, null, title, subtitle, amount, caption, false);
    }

    static FeedEntry settlement(String title, String subtitle, String amount, String caption) {
        return new FeedEntry(TYPE_ROW, null, title, subtitle, amount, caption, true);
    }
}
