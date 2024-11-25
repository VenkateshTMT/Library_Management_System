import db.connection.GetConnection;
import exeception.CustomExeception;
import library.implementation.BookDaoImpl;
import lms.services.BookService;
import lms.services.UserBookService;

import java.util.Scanner;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class LmsMenu {

    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String message = myMenu();
        System.out.println(message);
    }

    private static void closeAllConnections() {
        GetConnection.closeConnection();
        input.close();
    }

    public static String myMenu() {
        while (true) {
            System.out.println("""
                    Enter any of the menu operation from below:
                    1:Borrow a book
                    2:Return a Book
                    3:search for a book
                    4:Check Available Books
                    5:Generate Fee Report
                    6:Exit the System""");
            try{
            int option = input.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("-".repeat(150));
                        UserBookService.borrowingBook();
                        System.out.println("-".repeat(150));
                        break;
                    case 2:
                        System.out.println("-".repeat(150));
                        UserBookService.returningBook();
                        System.out.println("-".repeat(150));
                        break;
                    case 3:
                        System.out.println("-".repeat(150));
                        BookService.searchForBook();
                        System.out.println("-".repeat(150));
                        break;
                    case 4:
                        System.out.println("-".repeat(150));
                        BookService.checkAvailableBooks();
                        System.out.println("-".repeat(150));
                        break;
                    case 5:
                        System.out.println("-".repeat(150));
                        UserBookService.generateFeeReport();
                        System.out.println("-".repeat(150));
                        break;
                    case 6:
                        closeAllConnections();
                        return "Exit the System";
                    default:
                        System.out.println("Enter valid input");
                        break;
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                throw new CustomExeception(e.getMessage());
            }
        }
    }
}