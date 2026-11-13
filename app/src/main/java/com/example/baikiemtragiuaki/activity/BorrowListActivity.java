package com.example.baikiemtragiuaki.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.adapter.BorrowTicketAdapter;
import com.example.baikiemtragiuaki.database.DatabaseHelper;
import com.example.baikiemtragiuaki.model.BorrowTicket;

import java.util.ArrayList;
import java.util.List;

public class BorrowListActivity extends AppCompatActivity {

    private ListView lvBorrowTickets;
    private TextView tvEmptyTickets;

    private DatabaseHelper databaseHelper;
    private List<BorrowTicket> ticketList;
    private BorrowTicketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_list);

        // Hiển thị nút quay lại trên ActionBar hệ thống
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách phiếu mượn");
        }

        // Ánh xạ View
        Button btnBack = findViewById(R.id.btnBack);
        lvBorrowTickets = findViewById(R.id.lvBorrowTickets);
        tvEmptyTickets = findViewById(R.id.tvEmptyTickets);

        databaseHelper = new DatabaseHelper(this);
        ticketList = new ArrayList<>();

        // Nút quay lại về màn hình chính
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo Adapter với sự kiện bấm Nút Trả Sách
        ticketAdapter = new BorrowTicketAdapter(this, ticketList, ticket -> showReturnConfirmDialog(ticket));

        lvBorrowTickets.setAdapter(ticketAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTicketData();
    }

    // Nạp danh sách phiếu mượn đang còn hiệu lực ("Đang mượn")
    private void loadTicketData() {
        ticketList.clear();
        ticketList.addAll(databaseHelper.getActiveBorrowTickets());

        ticketAdapter.notifyDataSetChanged();

        if (ticketList.isEmpty()) {
            tvEmptyTickets.setVisibility(View.VISIBLE);
            lvBorrowTickets.setVisibility(View.GONE);
        } else {
            tvEmptyTickets.setVisibility(View.GONE);
            lvBorrowTickets.setVisibility(View.VISIBLE);
        }
    }

    // Chức năng 3: Cho phép thực hiện trả sách & Cập nhật trạng thái sách về "Có sẵn"
    private void showReturnConfirmDialog(BorrowTicket ticket) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Trả sách")
                .setMessage("Xác nhận nhận lại sách \"" + ticket.getBookTitle() + "\" từ người mượn " + ticket.getBorrowerName() + "?")
                .setPositiveButton("Trả sách", (dialog, which) -> {
                    boolean success = databaseHelper.returnBook(ticket.getId(), ticket.getBookId());
                    if (success) {
                        Toast.makeText(BorrowListActivity.this, "Trả sách thành công! Trạng thái sách đã chuyển về \"Có sẵn\".", Toast.LENGTH_LONG).show();
                        loadTicketData();
                    } else {
                        Toast.makeText(BorrowListActivity.this, "Xử lý trả sách thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
