package com.example.fast_dailyquotes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuoteViewPagerAdapter extends RecyclerView.Adapter<QuoteViewPagerAdapter.ViewHolder> {

    private List<Quote> list = null;
    private Boolean isNameRevealed = true;

    public QuoteViewPagerAdapter(List<Quote> list, Boolean isNameRevealed) {
        this.list = list;
        this.isNameRevealed = isNameRevealed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.viewpager_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = list.get(position);

        holder.contentTextView.setText("\"" + quote.text + "\"");

        if (isNameRevealed) {
            holder.nameTextView.setText(quote.name);
            holder.nameTextView.setVisibility(View.VISIBLE);
        } else {
            holder.nameTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentTextView = itemView.findViewById(R.id.quoteTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
