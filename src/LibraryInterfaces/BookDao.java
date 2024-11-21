package LibraryInterfaces;

import ModelDaoclasses.Book;

import java.util.List;

public interface BookDao {
    Book getBookId(int id);

    List<Book> getBookByTitle(String title);

    List<Book> getAllBooks();

    List<Book> getAllAvailableBooks();

    void updateBookOnBorrow(int bookId);

    void updateBookOnReturn(int bookId);

    Book getSpecificBookTitle(String title);

    void closeBookConnection();
}
