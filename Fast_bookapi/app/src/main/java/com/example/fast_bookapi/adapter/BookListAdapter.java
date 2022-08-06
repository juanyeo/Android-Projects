package com.example.fast_bookapi.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fast_bookapi.databinding.ListItemBinding;
import com.example.fast_bookapi.model.Book;

import java.util.List;

public class BookListAdapter extends ListAdapter<Book, BookListAdapter.ViewHolderClass> {

    public BookListAdapter(@NonNull DiffUtil.ItemCallback<Book> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderClass viewHolder = new ViewHolderClass(ListItemBinding.inflate(inflater, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        List<Book> currentList = getCurrentList();
        holder.bind(currentList.get(position));
    }

    class ViewHolderClass extends RecyclerView.ViewHolder {

        ListItemBinding binding;

        public ViewHolderClass(@NonNull ListItemBinding b) {
            super(b.getRoot());
            binding = b;
        }

        public void bind(Book bookModel) {
            binding.titleTextView.setText(bookModel.getTitle());
            binding.descriptionTextView.setText(bookModel.getDescription());

            Glide.with(binding.coverImageView.getContext()).load(bookModel.getCoverSmallUrl())
                    .into(binding.coverImageView);
        }
    }
}

