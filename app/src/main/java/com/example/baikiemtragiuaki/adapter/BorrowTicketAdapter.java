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

import com.example.baikiemtragiuaki.R;
import com.example.baikiemtragiuaki.model.BorrowTicket;

import java.util.List;

public class BorrowTicketAdapter extends ArrayAdapter<BorrowTicket> {

    private final Context context;
    private final List<BorrowTicket> ticketList;
    private final OnTicketActionListener actionListener;

    public interface OnTicketActionListener {
        void onReturnBook(BorrowTicket ticket);
    }

    public BorrowTicketAdapter(@NonNull Context context, @NonNull List<BorrowTicket> ticketList, OnTicketActionListener actionListener) {
        super(context, 0, ticketList);
        this.context = context;
        this.ticketList = ticketList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_borrow_ticket, parent, false);
        }

        BorrowTicket ticket = ticketList.get(position);

        TextView tvTicketBookTitle = view.findViewById(R.id.tvTicketBookTitle);
        TextView tvBorrowerName = view.findViewById(R.id.tvBorrowerName);
        TextView tvBorrowerPhone = view.findViewById(R.id.tvBorrowerPhone);
        TextView tvBorrowDate = view.findViewById(R.id.tvBorrowDate);
        TextView tvReturnDate = view.findViewById(R.id.tvReturnDate);
        Button btnReturnBook = view.findViewById(R.id.btnReturnBook);

        tvTicketBookTitle.setText(ticket.getBookTitle() != null ? ticket.getBookTitle() : ticket.getBookId());
        tvBorrowerName.setText("Người mượn: " + ticket.getBorrowerName());
        tvBorrowerPhone.setText("SĐT: " + ticket.getBorrowerPhone());
        tvBorrowDate.setText("Ngày mượn: " + ticket.getBorrowDate());
        tvReturnDate.setText("Hạn trả: " + ticket.getReturnDate());

        btnReturnBook.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onReturnBook(ticket);
            }
        });

        return view;
    }
}
