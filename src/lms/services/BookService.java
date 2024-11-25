package lms.services;

import exeception.CustomExeception;
import library.interfaces.BookDao;
import library.implementation.BookDaoImpl;
import library.models.Book;

import java.util.List;
import java.util.Scanner;

public class BookService {

    public static final Scanner input = new Scanner(System.in);
    public static BookDao bookObject = new BookDaoImpl();

    //3 Search for Book operation
    public static void searchForBook() {
        try{
            System.out.println("Enter the book title");
            String userInput = input.nextLine();
            List<Book> bookList = bookObject.getBookByTitle(userInput.toLowerCase());
            if (bookList.isEmpty()) {
                System.out.println("No books found the matching the search term");
            } else {
                bookList.stream().forEach(each-> System.out.println(each));
            }
        } catch (Exception e) {
            throw new CustomExeception(e.getMessage());
        }

    }

    //4 check Available books
    public static void checkAvailableBooks() {
        List<Book> libraryBooksList = bookObject.getAllAvailableBooks();
        libraryBooksList.stream().forEach(each-> System.out.println(each.displayString(each)));
    }

}
