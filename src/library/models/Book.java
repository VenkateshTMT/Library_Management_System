package library.models;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String status;

    public Book() {

    }

    public Book(int bookId, String title, String author, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    public String displayString(Book book) {
        int id = book.getBookId();
        String title = book.getTitle();
        String author = book.getAuthor();
        return "id: " + id + " title: " + title + " author: " + author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "bookId: " + bookId + " title: " + title + " author: " + author + " status: " + status;
    }
}
