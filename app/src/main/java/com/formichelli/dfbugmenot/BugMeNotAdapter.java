package com.formichelli.dfbugmenot;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BugMeNotAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List<BugMeNotResult> items;

    public BugMeNotAdapter(Context mContext) {
        this.mContext = mContext;
        this.items = new ArrayList<>();
    }

    public static class BugMeNotViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView password;
        public TextView successRate;
        public TextView votes;
        public TextView eta;

        public BugMeNotViewHolder(final Context mContext, View v) {
            super(v);

            View.OnClickListener copyToClipBoard = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager)
                            mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("username", ((TextView) v).getText().toString());
                    clipboard.setPrimaryClip(clip);
                }
            };

            this.username = (TextView) v.findViewById(R.id.username);
            this.username.setOnClickListener(copyToClipBoard);
            this.password = (TextView) v.findViewById(R.id.password);
            this.password.setOnClickListener(copyToClipBoard);
            this.successRate = (TextView) v.findViewById(R.id.success_rate);
            this.votes = (TextView) v.findViewById(R.id.votes);
            this.eta = (TextView) v.findViewById(R.id.eta);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        return new BugMeNotViewHolder(mContext, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bugmenot_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Resources resources = mContext.getResources();

        // set fields of the view
        BugMeNotResult item = items.get(position);

        BugMeNotViewHolder bugMeNotViewHolder = ((BugMeNotViewHolder) holder);

        bugMeNotViewHolder.username.setText(item.getUsername());

        bugMeNotViewHolder.password.setText(item.getPassword());

        int successRateValue = (int) item.getSuccessRate();
        bugMeNotViewHolder.successRate.setText(resources.getString(R.string.success_rate, successRateValue));
        if (successRateValue < 34)
            bugMeNotViewHolder.successRate.setTextColor(Color.RED);
        else if (successRateValue < 67)
            bugMeNotViewHolder.successRate.setTextColor(Color.YELLOW);
        else
            bugMeNotViewHolder.successRate.setTextColor(Color.GREEN);

        bugMeNotViewHolder.votes.setText(resources.getQuantityString(R.plurals.votes, item.getVotes(), item.getVotes()));

        int etaValue = item.getEta();
        String etaWeightedValue;
        switch (item.getEtaWeight()) {
            case DAYS:
                etaWeightedValue = resources.getQuantityString(R.plurals.days, etaValue, etaValue);
                break;

            case MONTHS:
                etaWeightedValue = resources.getQuantityString(R.plurals.months, etaValue, etaValue);
                break;

            case YEARS:
                etaWeightedValue = resources.getQuantityString(R.plurals.years, etaValue, etaValue);
                break;

            default:
                throw new IllegalArgumentException("Unknown eta weight: " + item.getEtaWeight());
        }
        bugMeNotViewHolder.eta.setText(resources.getString(R.string.eta, etaWeightedValue));

        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(BugMeNotResult item) {
        add(item, items.size());
    }

    public void add(List<BugMeNotResult> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add(BugMeNotResult item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(BugMeNotResult item) {
        remove(items.indexOf(item));
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void replace(List<BugMeNotResult> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
