import DaoImpl.BookDaoImpl;
import DaoImpl.BorrowedRecordsDaoImpl;
import LibraryInterfaces.BookDao;
import LibraryInterfaces.BorrowedRecordsDao;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static Scanner input=new Scanner(System.in);

    public static BookDao bookObject=new BookDaoImpl();

    public static BorrowedRecordsDao mainBRObj=new BorrowedRecordsDaoImpl();

    public static void main(String[] args) {
        String message=myMenu();
        System.out.println(message);
    }

    private static void closeAllConnections(){
        mainBRObj.closeBorrowedRecordsConnection();
        bookObject.closeBookConnection();
        input.close();
    }

    public static String myMenu() {
        while(true){
            System.out.println("-----------------------------------------------------------");
            System.out.println("""
                    Enter any of the menu operation from below:
                    1:Borrow a book
                    2:Return a Book
                    3:search for a book
                    4:Check Available Books
                    5:Generate Fee Report
                    6:Exit the System""");
            String option=input.next();
            switch(option){
                case "1":
                    UserBook.borrowingBook();
                    break;
                case "2":
                    UserBook.returningBook();
                    break;
                case "3":
                    BookOperations.searchForBook();
                    break;
                case "4":
                    BookOperations.checkAvailableBooks();
                    break;
                case "5":
                    UserBook.generateFeeReport();
                    break;
                case "6":
                    closeAllConnections();
                    return "Exit the System";
                default:
                    System.out.println("Enter valid input");
                    break;
            }
        }
    }
}