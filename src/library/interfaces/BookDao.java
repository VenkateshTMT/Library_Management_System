package library.interfaces;

import library.models.Book;

import java.util.List;

public interface BookDao {
    Book getBookId(int id);

    List<Book> getBookByTitle(String title);

    List<Book> getAllBooks();

    List<Book> getAllAvailableBooks();

    int updateBookOnBorrow(int bookId);

    void updateBookOnReturn(int bookId);

    Book getSpecificBookTitle(String title);

}
