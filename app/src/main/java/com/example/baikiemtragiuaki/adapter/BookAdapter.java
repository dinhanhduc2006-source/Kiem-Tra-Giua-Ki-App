package com.example.baikiemtragiuaki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.database.DatabaseHelper;
import com.example.baikiemtragiuaki.model.Book;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> bookList;
    private final OnBookActionListener actionListener;

    public interface OnBookActionListener {
        void onEdit(Book book);
        void onDelete(Book book);
    }

    public BookAdapter(@NonNull Context context, @NonNull List<Book> bookList, OnBookActionListener actionListener) {
        super(context, 0, bookList);
        this.context = context;
        this.bookList = bookList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        Book book = bookList.get(position);

        TextView tvBookId = view.findViewById(R.id.tvBookId);
        TextView tvBookTitle = view.findViewById(R.id.tvBookTitle);
        TextView tvBookAuthor = view.findViewById(R.id.tvBookAuthor);
        TextView tvBookCategory = view.findViewById(R.id.tvBookCategory);
        TextView tvBookStatus = view.findViewById(R.id.tvBookStatus);
        Button btnEditBook = view.findViewById(R.id.btnEditBook);
        Button btnDeleteBook = view.findViewById(R.id.btnDeleteBook);

        tvBookId.setText(book.getId());
        tvBookTitle.setText(book.getTitle());
        tvBookAuthor.setText("Tác giả: " + book.getAuthor());
        tvBookCategory.setText("Thể loại: " + book.getCategory());
        tvBookStatus.setText("Trạng thái: " + book.getStatus());

        if (DatabaseHelper.STATUS_AVAILABLE.equals(book.getStatus())) {
            tvBookStatus.setTextColor(ContextCompat.getColor(context, R.color.status_available));
        } else {
            tvBookStatus.setTextColor(ContextCompat.getColor(context, R.color.status_borrowed));
        }

        btnEditBook.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(book);
            }
        });

        btnDeleteBook.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(book);
            }
        });

        return view;
    }
}
