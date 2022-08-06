package com.example.fast_bookapi.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.fast_bookapi.model.Book;

public class DiffUtilClass extends DiffUtil.ItemCallback<Book> {

    @Override
    public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
        return oldItem.getItemId() == newItem.getItemId();
    }
}
