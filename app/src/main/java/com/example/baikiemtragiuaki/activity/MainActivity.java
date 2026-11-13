package com.example.baikiemtragiuaki.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalBooks;
    private TextView tvAvailableBooks;
    private TextView tvActiveTickets;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalBooks = findViewById(R.id.tvTotalBooks);
        tvAvailableBooks = findViewById(R.id.tvAvailableBooks);
        tvActiveTickets = findViewById(R.id.tvActiveTickets);

        CardView cardBookList = findViewById(R.id.cardBookList);
        CardView cardAddBorrow = findViewById(R.id.cardAddBorrow);
        CardView cardBorrowList = findViewById(R.id.cardBorrowList);

        databaseHelper = new DatabaseHelper(this);

        cardBookList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookListActivity.class);
            startActivity(intent);
        });

        cardAddBorrow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBorrowActivity.class);
            startActivity(intent);
        });

        cardBorrowList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BorrowListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }

    private void updateStatistics() {
        int total = databaseHelper.getTotalBooksCount();
        int available = databaseHelper.getAvailableBooksCount();
        int activeTickets = databaseHelper.getActiveTicketsCount();

        tvTotalBooks.setText(String.valueOf(total));
        tvAvailableBooks.setText(String.valueOf(available));
        tvActiveTickets.setText(String.valueOf(activeTickets));
    }
}
