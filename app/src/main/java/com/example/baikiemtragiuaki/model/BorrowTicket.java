package com.example.baikiemtragiuaki.model;

import java.io.Serializable;

public class BorrowTicket implements Serializable {
    private int id;
    private String bookId;
    private String bookTitle;
    private String borrowerName;
    private String borrowerPhone;
    private String borrowDate;
    private String returnDate;
    private String status;

    public BorrowTicket() {
    }

    public BorrowTicket(int id, String bookId, String bookTitle, String borrowerName, String borrowerPhone, String borrowDate, String returnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.borrowerPhone = borrowerPhone;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public BorrowTicket(String bookId, String bookTitle, String borrowerName, String borrowerPhone, String borrowDate, String returnDate, String status) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.borrowerPhone = borrowerPhone;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
