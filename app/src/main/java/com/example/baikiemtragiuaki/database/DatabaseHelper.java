package com.example.baikiemtragiuaki.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baikiemtragiuaki.model.Book;
import com.example.baikiemtragiuaki.model.BorrowTicket;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LibraryManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKS = "books";
    public static final String COLUMN_BOOK_ID = "id";
    public static final String COLUMN_BOOK_TITLE = "title";
    public static final String COLUMN_BOOK_AUTHOR = "author";
    public static final String COLUMN_BOOK_CATEGORY = "category";
    public static final String COLUMN_BOOK_STATUS = "status";

    public static final String TABLE_BORROW = "borrow_tickets";
    public static final String COLUMN_BORROW_ID = "id";
    public static final String COLUMN_BORROW_BOOK_ID = "book_id";
    public static final String COLUMN_BORROW_NAME = "borrower_name";
    public static final String COLUMN_BORROW_PHONE = "borrower_phone";
    public static final String COLUMN_BORROW_DATE = "borrow_date";
    public static final String COLUMN_BORROW_RETURN_DATE = "return_date";
    public static final String COLUMN_BORROW_STATUS = "status";

    public static final String STATUS_AVAILABLE = "Có sẵn";
    public static final String STATUS_BORROWED = "Đang được mượn";
    public static final String TICKET_STATUS_ACTIVE = "Đang mượn";
    public static final String TICKET_STATUS_RETURNED = "Đã trả";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " ("
                + COLUMN_BOOK_ID + " TEXT PRIMARY KEY, "
                + COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + COLUMN_BOOK_CATEGORY + " TEXT NOT NULL, "
                + COLUMN_BOOK_STATUS + " TEXT NOT NULL" + ")";
        db.execSQL(createBooksTable);

        String createBorrowTable = "CREATE TABLE " + TABLE_BORROW + " ("
                + COLUMN_BORROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BORROW_BOOK_ID + " TEXT NOT NULL, "
                + COLUMN_BORROW_NAME + " TEXT NOT NULL, "
                + COLUMN_BORROW_PHONE + " TEXT NOT NULL, "
                + COLUMN_BORROW_DATE + " TEXT NOT NULL, "
                + COLUMN_BORROW_RETURN_DATE + " TEXT NOT NULL, "
                + COLUMN_BORROW_STATUS + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + COLUMN_BORROW_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + ")" + ")";
        db.execSQL(createBorrowTable);

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        ContentValues cv1 = new ContentValues();
        cv1.put(COLUMN_BOOK_ID, "MS01");
        cv1.put(COLUMN_BOOK_TITLE, "Lập trình Android với Java");
        cv1.put(COLUMN_BOOK_AUTHOR, "Nguyễn Văn A");
        cv1.put(COLUMN_BOOK_CATEGORY, "Công nghệ thông tin");
        cv1.put(COLUMN_BOOK_STATUS, STATUS_AVAILABLE);
        db.insert(TABLE_BOOKS, null, cv1);

        ContentValues cv2 = new ContentValues();
        cv2.put(COLUMN_BOOK_ID, "MS02");
        cv2.put(COLUMN_BOOK_TITLE, "Cấu trúc dữ liệu & Giải thuật");
        cv2.put(COLUMN_BOOK_AUTHOR, "Trần Thị B");
        cv2.put(COLUMN_BOOK_CATEGORY, "Khoa học máy tính");
        cv2.put(COLUMN_BOOK_STATUS, STATUS_AVAILABLE);
        db.insert(TABLE_BOOKS, null, cv2);

        ContentValues cv3 = new ContentValues();
        cv3.put(COLUMN_BOOK_ID, "MS03");
        cv3.put(COLUMN_BOOK_TITLE, "Đắc Nhân Tâm");
        cv3.put(COLUMN_BOOK_AUTHOR, "Dale Carnegie");
        cv3.put(COLUMN_BOOK_CATEGORY, "Kỹ năng sống");
        cv3.put(COLUMN_BOOK_STATUS, STATUS_AVAILABLE);
        db.insert(TABLE_BOOKS, null, cv3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BORROW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public boolean checkBookIdExists(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_ID + " = ?", new String[]{bookId});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean addBook(Book book) {
        if (checkBookIdExists(book.getId())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, book.getId());
        values.put(COLUMN_BOOK_TITLE, book.getTitle());
        values.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        values.put(COLUMN_BOOK_CATEGORY, book.getCategory());
        values.put(COLUMN_BOOK_STATUS, book.getStatus() != null ? book.getStatus() : STATUS_AVAILABLE);

        long result = db.insert(TABLE_BOOKS, null, values);
        return result != -1;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_TITLE, book.getTitle());
        values.put(COLUMN_BOOK_AUTHOR, book.getAuthor());
        values.put(COLUMN_BOOK_CATEGORY, book.getCategory());

        int rows = db.update(TABLE_BOOKS, values, COLUMN_BOOK_ID + " = ?", new String[]{book.getId()});
        return rows > 0;
    }

    public boolean deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BORROW, COLUMN_BORROW_BOOK_ID + " = ?", new String[]{bookId});
        int rows = db.delete(TABLE_BOOKS, COLUMN_BOOK_ID + " = ?", new String[]{bookId});
        return rows > 0;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS));
                bookList.add(new Book(id, title, author, category, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }

    public List<Book> getAvailableBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_STATUS + " = ?", new String[]{STATUS_AVAILABLE});

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS));
                bookList.add(new Book(id, title, author, category, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }

    public List<Book> searchBooks(String query) {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "%" + query + "%";
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_TITLE + " LIKE ? OR " + COLUMN_BOOK_AUTHOR + " LIKE ?", new String[]{searchQuery, searchQuery});

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_CATEGORY));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_STATUS));
                bookList.add(new Book(id, title, author, category, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }

    public boolean addBorrowTicket(BorrowTicket ticket) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BORROW_BOOK_ID, ticket.getBookId());
            values.put(COLUMN_BORROW_NAME, ticket.getBorrowerName());
            values.put(COLUMN_BORROW_PHONE, ticket.getBorrowerPhone());
            values.put(COLUMN_BORROW_DATE, ticket.getBorrowDate());
            values.put(COLUMN_BORROW_RETURN_DATE, ticket.getReturnDate());
            values.put(COLUMN_BORROW_STATUS, TICKET_STATUS_ACTIVE);

            long ticketResult = db.insert(TABLE_BORROW, null, values);
            if (ticketResult == -1) {
                return false;
            }

            ContentValues bookValues = new ContentValues();
            bookValues.put(COLUMN_BOOK_STATUS, STATUS_BORROWED);
            db.update(TABLE_BOOKS, bookValues, COLUMN_BOOK_ID + " = ?", new String[]{ticket.getBookId()});

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public List<BorrowTicket> getActiveBorrowTickets() {
        List<BorrowTicket> ticketList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT t.*, b." + COLUMN_BOOK_TITLE + " FROM " + TABLE_BORROW + " t "
                + " INNER JOIN " + TABLE_BOOKS + " b ON t." + COLUMN_BORROW_BOOK_ID + " = b." + COLUMN_BOOK_ID
                + " WHERE t." + COLUMN_BORROW_STATUS + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{TICKET_STATUS_ACTIVE});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BORROW_ID));
                String bookId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_BOOK_ID));
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE));
                String borrowerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_NAME));
                String borrowerPhone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_PHONE));
                String borrowDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_DATE));
                String returnDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_RETURN_DATE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_STATUS));

                ticketList.add(new BorrowTicket(id, bookId, bookTitle, borrowerName, borrowerPhone, borrowDate, returnDate, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ticketList;
    }

    public boolean returnBook(int ticketId, String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues ticketValues = new ContentValues();
            ticketValues.put(COLUMN_BORROW_STATUS, TICKET_STATUS_RETURNED);
            int ticketRows = db.update(TABLE_BORROW, ticketValues, COLUMN_BORROW_ID + " = ?", new String[]{String.valueOf(ticketId)});

            ContentValues bookValues = new ContentValues();
            bookValues.put(COLUMN_BOOK_STATUS, STATUS_AVAILABLE);
            db.update(TABLE_BOOKS, bookValues, COLUMN_BOOK_ID + " = ?", new String[]{bookId});

            if (ticketRows > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public int getTotalBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getAvailableBooksCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_STATUS + " = ?", new String[]{STATUS_AVAILABLE});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getActiveTicketsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BORROW + " WHERE " + COLUMN_BORROW_STATUS + " = ?", new String[]{TICKET_STATUS_ACTIVE});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
