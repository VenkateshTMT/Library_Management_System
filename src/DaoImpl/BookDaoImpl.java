package DaoImpl;

import LibraryInterfaces.BookDao;
import ModelDaoclasses.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private static final String UPDATE_BOOK_STATUS_ON_BORROW="update book set status='Borrowed' where bookId=?";
    private static final String UPDATE_BOOK_STATUS_ON_RETURN="update book set status='Available' where bookId=?";
    private static final String SELECT_Book_BY_ID="select * from `book` where bookId=?";
    private static final String SELECT_ALL_BOOKS="select * from `book`";
    private static final String SElECT_BOOK_TITLE="select * from book where LOWER(title) like ?";

    Connection connection;

    //making the sql connection when the class is instantiated

    public BookDaoImpl(){
        String url="jdbc:mysql://localhost:3306/library_management";
        String username="root";
        String password="root";
        try {
            connection = DriverManager.getConnection(url, username, password);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public void updateBookOnBorrow(int bookId){
        PreparedStatement pstmt;
        try{
            pstmt=connection.prepareStatement(UPDATE_BOOK_STATUS_ON_BORROW);
            pstmt.setInt(1,bookId);
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateBookOnReturn(int bookId){
        PreparedStatement pstmt;
        try{
            pstmt=connection.prepareStatement(UPDATE_BOOK_STATUS_ON_RETURN);
            pstmt.setInt(1,bookId);
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }



    public List<Book> getBookByTitle(String title){
        PreparedStatement pstmt=null;
        ResultSet res=null;
        ArrayList<Book> booksList=new ArrayList<>();
        try{
            pstmt=connection.prepareStatement(SElECT_BOOK_TITLE);
            pstmt.setString(1,"%"+title+"%");
            res=pstmt.executeQuery();

            while(res.next()){
                int bookId=res.getInt("bookId");
                String titles=res.getString("title");
                String author=res.getString("author");
                String status=res.getString("status");
                Book book= new Book(bookId,titles,author,status);
                booksList.add(book);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return booksList;
    }

    //getBook method
    public Book getBookId(int id){
        PreparedStatement pstmt=null;
        ResultSet res=null;
        String status=null;
        try{
            pstmt=connection.prepareStatement(SELECT_Book_BY_ID);
            pstmt.setInt(1,id);
            res=pstmt.executeQuery();
            if (res.next()){
                int bookId=res.getInt("bookId");
                String title=res.getString("title");
                String author=res.getString("author");
                status=res.getString("status");
                return new Book(bookId,title,author,status);
            }
        } catch (SQLException e){
           e.printStackTrace();
        }
        return null;
    }

    public List<Book> getAllBooks(){
        Statement stmt=null;
        ResultSet res=null;
        ArrayList<Book> booksList=new ArrayList<>();
        try{
            stmt=connection.createStatement();
            res=stmt.executeQuery(SELECT_ALL_BOOKS);
            while(res.next()){
                int bookId=res.getInt("bookId");
                String title=res.getString("title");
                String author=res.getString("author");
                String status=res.getString("status");
                Book book= new Book(bookId,title,author,status);
                booksList.add(book);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return booksList;
    }

    public List<Book> getAllAvailableBooks(){
        Statement stmt=null;
        ResultSet res=null;
        ArrayList<Book> booksList=new ArrayList<>();
        try{
            stmt=connection.createStatement();
            res=stmt.executeQuery(SELECT_ALL_BOOKS);
            while(res.next()){
                int bookId=res.getInt("bookId");
                String title=res.getString("title");
                String author=res.getString("author");
                String status=res.getString("status");
                Book book= new Book(bookId,title,author,status);
                if (status.equals("Available")){
                    booksList.add(book);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return booksList;
    }

    public Book getSpecificBookTitle(String title){
        PreparedStatement pstmt=null;
        ResultSet res=null;
        Book book=null;
        try{
            pstmt=connection.prepareStatement(SElECT_BOOK_TITLE);
            pstmt.setString(1,title);
            res=pstmt.executeQuery();
            if(res.next()){
                int bookId=res.getInt("bookId");
                String author=res.getString("author");
                String status=res.getString("status");
                book= new Book(bookId,title,author,status);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return book;
    }

    public void closeBookConnection(){
        try{
            if (connection!=null){
                connection.close();
            }else{
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
