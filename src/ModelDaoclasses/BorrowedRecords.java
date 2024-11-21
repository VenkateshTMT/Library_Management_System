package ModelDaoclasses;

import java.sql.Date;

public class BorrowedRecords {
    private int borrowId;
    private int bookId;
    private int userId;
    private Date borrowDate;
    private Date returnDate;
    private double lateFee;

    public BorrowedRecords(){}

    public BorrowedRecords(int borrowId, int bookId, int userId, Date borrowDate, Date returnDate, double lateFee) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.lateFee = lateFee;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    @Override
    public String toString() {
        return "borrowedId: "+borrowId+" bookId: "+bookId
                +" userId: "+userId+" borrowedDate: "+borrowDate+" returnDate: "+returnDate
                +" late fee: "+lateFee;
    }
}
