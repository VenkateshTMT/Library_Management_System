package library.models;

public class Report {
    private int bookId;
    private String title;
    private String borrowerName;
    private long daysOverDue;
    private double lateFee;

    public Report(int bookId, String title, String borrowerName, long daysOverDue, double lateFee) {
        this.bookId = bookId;
        this.title = title;
        this.borrowerName = borrowerName;
        this.daysOverDue = daysOverDue;
        this.lateFee = lateFee;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public long getDaysOverDue() {
        return daysOverDue;
    }

    public void setDaysOverDue(long daysOverDue) {
        this.daysOverDue = daysOverDue;
    }

    public double getLateFee() {
        return lateFee;
    }

    public void setLateFee(double lateFee) {
        this.lateFee = lateFee;
    }

    @Override
    public String toString() {
        return "bookId: " + bookId + " title: " + title + " Borrower Name: "
                + borrowerName + " Days OverDue: " + daysOverDue + " Late fee collected " + lateFee;
    }
}
