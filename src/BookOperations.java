import DaoImpl.BookDaoImpl;
import LibraryInterfaces.BookDao;
import ModelDaoclasses.Book;

import java.util.ArrayList;
import java.util.List;

public class BookOperations {
    public static BookDao bookObject=new BookDaoImpl();

    /* public static void searchForBook(){
        System.out.println("Enter the id");
        Book book=bookObject.getBook(Main.input.nextInt());
        if (book!=null){
            System.out.println(book);
        }else{
            System.out.println("book not found");
        }
    }*/

    //3 Search for Book operation
    public static void searchForBook(){
        System.out.println("Enter the book title");
        String userInput=Main.input.next();
        List<Book> bookList=bookObject.getBookByTitle(userInput.toLowerCase());
        if (bookList.isEmpty()){
            System.out.println("No books found the matching the search term");
        }else{
            for(Book book:bookList){
                System.out.println(book);
            }
        }
    }

    public static void getAllBooks(){
        List<Book> libraryBooksList=bookObject.getAllBooks();
       for (Book book:libraryBooksList){
               System.out.println(book);
       }
    }
    //4 check Available books
    public static void checkAvailableBooks(){
        List<Book> libraryBooksList=bookObject.getAllAvailableBooks();
        for (Book book:libraryBooksList){
            System.out.println(Book.displayString(book));
        }
    }

}
