package com.example.baikiemtragiuaki.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.database.DatabaseHelper;
import com.example.baikiemtragiuaki.model.Book;

public class AddEditBookActivity extends AppCompatActivity {

    private EditText etBookId, etBookTitle, etBookAuthor, etBookCategory;

    private DatabaseHelper databaseHelper;
    private Book editingBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin sách");
        }

        Button btnBack = findViewById(R.id.btnBack);
        TextView tvFormTitle = findViewById(R.id.tvFormTitle);
        etBookId = findViewById(R.id.etBookId);
        etBookTitle = findViewById(R.id.etBookTitle);
        etBookAuthor = findViewById(R.id.etBookAuthor);
        etBookCategory = findViewById(R.id.etBookCategory);
        Button btnSaveBook = findViewById(R.id.btnSaveBook);
        Button btnCancel = findViewById(R.id.btnCancel);

        databaseHelper = new DatabaseHelper(this);

        btnBack.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("BOOK_EXTRA")) {
            editingBook = (Book) getIntent().getSerializableExtra("BOOK_EXTRA");
        }

        if (editingBook != null) {
            tvFormTitle.setText("CẬP NHẬT THÔNG TIN SÁCH");
            etBookId.setText(editingBook.getId());
            etBookId.setEnabled(false);
            etBookTitle.setText(editingBook.getTitle());
            etBookAuthor.setText(editingBook.getAuthor());
            etBookCategory.setText(editingBook.getCategory());
        } else {
            tvFormTitle.setText("THÊM SÁCH MỚI");
        }

        btnCancel.setOnClickListener(v -> finish());

        btnSaveBook.setOnClickListener(v -> saveBook());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveBook() {
        String id = etBookId.getText().toString().trim();
        String title = etBookTitle.getText().toString().trim();
        String author = etBookAuthor.getText().toString().trim();
        String category = etBookCategory.getText().toString().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ các thông tin bắt buộc (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingBook == null) {
            if (databaseHelper.checkBookIdExists(id)) {
                Toast.makeText(this, "Mã sách \"" + id + "\" đã tồn tại! Vui lòng nhập mã sách khác.", Toast.LENGTH_LONG).show();
                etBookId.requestFocus();
                return;
            }

            Book newBook = new Book(id, title, author, category, DatabaseHelper.STATUS_AVAILABLE);
            boolean success = databaseHelper.addBook(newBook);
            if (success) {
                Toast.makeText(this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Thêm sách thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Book updatedBook = new Book(id, title, author, category, editingBook.getStatus());
            boolean success = databaseHelper.updateBook(updatedBook);
            if (success) {
                Toast.makeText(this, "Cập nhật thông tin sách thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
