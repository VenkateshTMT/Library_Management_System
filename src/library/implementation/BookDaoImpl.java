package library.implementation;

import db.connection.GetConnection;
import exeception.CustomExeception;
import library.interfaces.BookDao;
import library.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mysql.cj.conf.PropertyKey.logger;

public class BookDaoImpl implements BookDao {
    private static final Logger logger = Logger.getLogger(BookDaoImpl.class.getName());

    private static final String UPDATE_BOOK_STATUS_ON_BORROW = "update book set status='Borrowed' where bookId=?";
    private static final String UPDATE_BOOK_STATUS_ON_RETURN = "update book set status='Available' where bookId=?";
    private static final String SELECT_Book_BY_ID = "select * from `book` where bookId=?";
    private static final String SELECT_ALL_BOOKS = "select * from `book`";
    private static final String SElECT_BOOK_TITLE = "select * from book where LOWER(title)like ?";

    Connection connection=null;

    //making the sql connection when the class is instantiated

    public BookDaoImpl() {
        connection= GetConnection.makeConnection();
    }


    public int updateBookOnBorrow(int bookId) {
        PreparedStatement pstmt;
        int count=0;
        try {
            pstmt = connection.prepareStatement(UPDATE_BOOK_STATUS_ON_BORROW);
            pstmt.setInt(1, bookId);
            count=pstmt.executeUpdate();
            logger.info("Book with ID " + bookId + " marked as Borrowed.");
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error updating book status on borrow", e);
            throw new CustomExeception(e.getMessage());
        }
        return count;
    }

    public void updateBookOnReturn(int bookId) {
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(UPDATE_BOOK_STATUS_ON_RETURN);
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            logger.info("Book with ID " + bookId + " marked as Available.");
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error updating book status on return", e);
            throw new CustomExeception(e.getMessage());
        }
    }

    @Override
    public List<Book> getBookByTitle(String title) {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        ArrayList<Book> booksList = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(SElECT_BOOK_TITLE);
            pstmt.setString(1, "%" + title + "%");
            res = pstmt.executeQuery();

            while (res.next()) {
                int bookId = res.getInt("bookId");
                String titles = res.getString("title");
                String author = res.getString("author");
                String status = res.getString("status");
                Book book = new Book(bookId, titles, author, status);
                booksList.add(book);
            }

        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving books by title", e);
            throw new CustomExeception(e.getMessage());
        }
        return booksList;
    }

    //getBook method
    @Override
    public Book getBookId(int id) {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        String status = null;
        try {
            pstmt = connection.prepareStatement(SELECT_Book_BY_ID);
            pstmt.setInt(1, id);
            res = pstmt.executeQuery();
            if (res.next()) {
                int bookId = res.getInt("bookId");
                String title = res.getString("title");
                String author = res.getString("author");
                status = res.getString("status");
                return new Book(bookId,title,author,status);
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving book by ID", e);
            throw new CustomExeception(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        Statement stmt = null;
        ResultSet res = null;
        ArrayList<Book> booksList = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            res = stmt.executeQuery(SELECT_ALL_BOOKS);
            while (res.next()) {
                int bookId = res.getInt("bookId");
                String title = res.getString("title");
                String author = res.getString("author");
                String status = res.getString("status");
                Book book = new Book(bookId, title, author, status);
                booksList.add(book);
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving all books", e);
            throw new CustomExeception(e.getMessage());
        }
        return booksList;
    }

    @Override
    public List<Book> getAllAvailableBooks() {
        Statement stmt = null;
        ResultSet res = null;
        ArrayList<Book> booksList = new ArrayList<>();
        try {
            stmt = connection.createStatement();
            res = stmt.executeQuery(SELECT_ALL_BOOKS);
            while (res.next()) {
                int bookId = res.getInt("bookId");
                String title = res.getString("title");
                String author = res.getString("author");
                String status = res.getString("status");
                Book book = new Book(bookId, title, author, status);
                if (status.equals("Available")) {
                    booksList.add(book);
                }
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving available books", e);
            throw new CustomExeception(e.getMessage());
        }
        return booksList;
    }

    @Override
    public Book getSpecificBookTitle(String title) {
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Book book = null;
        try {
            pstmt = connection.prepareStatement(SElECT_BOOK_TITLE);
            pstmt.setString(1, title);
            res = pstmt.executeQuery();
            if (res.next()) {
                int bookId = res.getInt("bookId");
                String author = res.getString("author");
                String status = res.getString("status");
                book = new Book(bookId, title, author, status);
            }
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
            logger.log(Level.SEVERE, "Error retrieving specific book by title", e);
            throw new CustomExeception(e.getMessage());
        }
        return book;
    }


}
