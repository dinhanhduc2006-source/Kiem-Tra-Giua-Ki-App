package com.example.baikiemtragiuaki.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.database.DatabaseHelper;
import com.example.baikiemtragiuaki.model.Book;
import com.example.baikiemtragiuaki.model.BorrowTicket;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddBorrowActivity extends AppCompatActivity {

    private Spinner spBooks;
    private EditText etBorrowerName, etBorrowerPhone, etBorrowDate, etReturnDate;
    private Button btnSaveBorrow;

    private DatabaseHelper databaseHelper;
    private List<Book> availableBooks;
    private final Calendar calendarBorrow = Calendar.getInstance();
    private final Calendar calendarReturn = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_borrow);

        // Hiển thị nút quay lại trên ActionBar hệ thống
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lập phiếu mượn");
        }

        // Ánh xạ View
        Button btnBack = findViewById(R.id.btnBack);
        spBooks = findViewById(R.id.spBooks);
        etBorrowerName = findViewById(R.id.etBorrowerName);
        etBorrowerPhone = findViewById(R.id.etBorrowerPhone);
        etBorrowDate = findViewById(R.id.etBorrowDate);
        etReturnDate = findViewById(R.id.etReturnDate);
        btnSaveBorrow = findViewById(R.id.btnSaveBorrow);
        Button btnCancelBorrow = findViewById(R.id.btnCancelBorrow);

        databaseHelper = new DatabaseHelper(this);

        // Nút quay lại về màn hình chính
        btnBack.setOnClickListener(v -> finish());

        // Set ngày mặc định (Ngày mượn = Hôm nay, Hạn trả = 7 ngày sau)
        calendarReturn.add(Calendar.DAY_OF_MONTH, 7);
        etBorrowDate.setText(dateFormat.format(calendarBorrow.getTime()));
        etReturnDate.setText(dateFormat.format(calendarReturn.getTime()));

        // Chức năng 3: Không cho phép mượn sách đang được người khác mượn
        // Chỉ nạp danh sách các cuốn sách có trạng thái "Có sẵn"
        loadAvailableBooks();

        // Sự kiện chọn ngày mượn qua DatePickerDialog
        etBorrowDate.setOnClickListener(v -> showDatePicker(calendarBorrow, etBorrowDate));

        // Sự kiện chọn ngày dự kiến trả qua DatePickerDialog
        etReturnDate.setOnClickListener(v -> showDatePicker(calendarReturn, etReturnDate));

        btnCancelBorrow.setOnClickListener(v -> finish());

        btnSaveBorrow.setOnClickListener(v -> saveBorrowTicket());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadAvailableBooks() {
        availableBooks = databaseHelper.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            Toast.makeText(this, "Hiện không có sách nào có sẵn trong thư viện để mượn!", Toast.LENGTH_LONG).show();
            btnSaveBorrow.setEnabled(false);
        } else {
            btnSaveBorrow.setEnabled(true);
        }

        // Dùng Spinner item tùy chỉnh với màu chữ đen rõ ràng
        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, availableBooks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBooks.setAdapter(adapter);
    }

    private void showDatePicker(Calendar calendar, EditText targetEditText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthSelected, daySelected) -> {
            calendar.set(yearSelected, monthSelected, daySelected);
            targetEditText.setText(dateFormat.format(calendar.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void saveBorrowTicket() {
        if (availableBooks.isEmpty() || spBooks.getSelectedItem() == null) {
            Toast.makeText(this, "Không có sách để mượn!", Toast.LENGTH_SHORT).show();
            return;
        }

        Book selectedBook = (Book) spBooks.getSelectedItem();
        String name = etBorrowerName.getText().toString().trim();
        String phone = etBorrowerPhone.getText().toString().trim();
        String borrowDate = etBorrowDate.getText().toString().trim();
        String returnDate = etReturnDate.getText().toString().trim();

        // Chức năng 3: Kiểm tra thông tin bắt buộc trước khi lưu
        if (name.isEmpty() || phone.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin mượn sách (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại phải đúng 10 chữ số
        if (phone.length() != 10 || !phone.matches("\\d{10}")) {
            Toast.makeText(this, "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số.", Toast.LENGTH_LONG).show();
            etBorrowerPhone.requestFocus();
            return;
        }

        // Tạo đối tượng Phiếu mượn
        BorrowTicket ticket = new BorrowTicket(
                selectedBook.getId(),
                selectedBook.getTitle(),
                name,
                phone,
                borrowDate,
                returnDate,
                DatabaseHelper.TICKET_STATUS_ACTIVE
        );

        // Lưu phiếu mượn và tự động đổi trạng thái sách thành "Đang được mượn"
        boolean success = databaseHelper.addBorrowTicket(ticket);
        if (success) {
            Toast.makeText(this, "Tạo phiếu mượn sách thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Tạo phiếu mượn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
