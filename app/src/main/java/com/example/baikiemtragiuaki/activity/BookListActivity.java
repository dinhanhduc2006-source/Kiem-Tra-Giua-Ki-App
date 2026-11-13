package com.example.baikiemtragiuaki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.adapter.BookAdapter;
import com.example.baikiemtragiuaki.database.DatabaseHelper;
import com.example.baikiemtragiuaki.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvBooks;
    private TextView tvEmpty;

    private DatabaseHelper databaseHelper;
    private List<Book> bookList;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý danh sách sách");
        }

        Button btnBack = findViewById(R.id.btnBack);
        Button btnAddBook = findViewById(R.id.btnAddBook);
        etSearch = findViewById(R.id.etSearch);
        lvBooks = findViewById(R.id.lvBooks);
        tvEmpty = findViewById(R.id.tvEmpty);

        databaseHelper = new DatabaseHelper(this);
        bookList = new ArrayList<>();

        btnBack.setOnClickListener(v -> finish());

        bookAdapter = new BookAdapter(this, bookList, new BookAdapter.OnBookActionListener() {
            @Override
            public void onEdit(Book book) {
                Intent intent = new Intent(BookListActivity.this, AddEditBookActivity.class);
                intent.putExtra("BOOK_EXTRA", book);
                startActivity(intent);
            }

            @Override
            public void onDelete(Book book) {
                showDeleteConfirmDialog(book);
            }
        });

        lvBooks.setAdapter(bookAdapter);

        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(BookListActivity.this, AddEditBookActivity.class);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookData();
    }

    private void loadBookData() {
        String query = etSearch.getText().toString().trim();
        if (query.isEmpty()) {
            bookList.clear();
            bookList.addAll(databaseHelper.getAllBooks());
        } else {
            filterBooks(query);
            return;
        }

        updateUI();
    }

    private void filterBooks(String query) {
        bookList.clear();
        if (query.isEmpty()) {
            bookList.addAll(databaseHelper.getAllBooks());
        } else {
            bookList.addAll(databaseHelper.searchBooks(query));
        }
        updateUI();
    }

    private void updateUI() {
        bookAdapter.notifyDataSetChanged();
        if (bookList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvBooks.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvBooks.setVisibility(View.VISIBLE);
        }
    }

    private void showDeleteConfirmDialog(Book book) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sách \"" + book.getTitle() + "\" (Mã: " + book.getId() + ") không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = databaseHelper.deleteBook(book.getId());
                    if (success) {
                        Toast.makeText(BookListActivity.this, "Xóa sách thành công!", Toast.LENGTH_SHORT).show();
                        loadBookData();
                    } else {
                        Toast.makeText(BookListActivity.this, "Xóa sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
