import DaoImpl.BorrowedRecordsDaoImpl;
import LibraryInterfaces.BorrowedRecordsDao;
import ModelDaoclasses.Book;
import ModelDaoclasses.BorrowedRecords;
import ModelDaoclasses.Report;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class UserBook {

    public static BorrowedRecordsDao borrowObj = new BorrowedRecordsDaoImpl();

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //1 this method check is book is available to borrow
    public static void borrowingBook() {

        Book book = null;

        System.out.println("Currently Available books");
        BookOperations.checkAvailableBooks();
        System.out.println("----------------------------------------------------");

        System.out.println("How you want to search for a book by ID or Title");
        String userInput = Main.input.next();

        if (userInput.equalsIgnoreCase("title")) {
            System.out.println("Enter the book title:");
            String bookTitle = Main.input.next();
            book = BookOperations.bookObject.getSpecificBookTitle(bookTitle.toLowerCase());
            validateBook(book);
        } else if (userInput.equalsIgnoreCase("ID")) {
            System.out.println("enter the book id you want to borrow:");
            int id = Main.input.nextInt();
            book = BookOperations.bookObject.getBookId(id);
            validateBook(book);
        } else {
            System.out.println("enter valid Input either ID or Title");
        }


    }

    private static void validateBook(Book book) {
        if (book == null) {
            System.out.println("Invalid Book ID or Title");
        } else if (!book.getStatus().equals("Available")) {
            System.out.println("Book not available");
        } else {
            recordingBook(book);

        }
    }

    //1.1 if book available this method will execute
    private static void recordingBook(Book book) {
        System.out.println("Enter userId:");
        int userId = Main.input.nextInt();
        int bookId = book.getBookId();
        int count = borrowObj.recordBorrowing(userId, bookId);
        if (count > 0) {
            BookOperations.bookObject.updateBookOnBorrow(bookId);
            System.out.println("Book borrowed successfully");
        } else {
            System.out.println("borrow unsuccessful try again");
        }
    }

    //2 returning a book
    public static void returningBook() {
        Book book = null;

        System.out.println("Enter how you want to return, by Book ID or Book Title");
        String userInput = Main.input.next();

        if (userInput.equalsIgnoreCase("title")) {
            System.out.println("Enter the title of the Book that you want to return:");
            String bookTitle = Main.input.next();
            book = BookOperations.bookObject.getSpecificBookTitle(bookTitle.toLowerCase());
        }
        else if (userInput.equalsIgnoreCase("id")) {
            System.out.println("enter bookID you want to return:");
            int userBookId = Main.input.nextInt();
            book = BookOperations.bookObject.getBookId(userBookId);
        }
        else {
            System.out.println("Invalid input,Select either Title or ID.");
            return;
        }

        BorrowedRecords record = null;
        int userId;

        if (book == null) {
            System.out.println("Invalid BookId,Title or Return Date. Please try again.");
        }
        else if (book.getStatus().equals("Borrowed")) {
            System.out.println("Enter the returning user userID :");
            userId = Main.input.nextInt();
            int bookId = book.getBookId();
            record = borrowObj.getRecord(bookId, userId);
            if (record == null) {
                System.out.println("User does not match");
            } else if (record.getReturnDate() != null) {
                System.out.println("The book is already returned");
            } else {
                doReturnOperation(record, bookId, userId);
            }
        }else{
            System.out.println("The book is not currently borrowed");
        }
    }

    //2.1 performing returning book operation
    public static void doReturnOperation(BorrowedRecords record, int bookId, int userId) {

        System.out.println("Enter return date in yyyy-mm-dd format");
        String userInputDate = Main.input.next();

        LocalDate returnDate = null;
        long days = 0;
        double lateFee = 0;
        LocalDate borrowedDate;
        try {
            returnDate = LocalDate.parse(userInputDate, dateFormatter);
            borrowedDate = record.getBorrowDate().toLocalDate();
            days = ChronoUnit.DAYS.between(borrowedDate, returnDate);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        if (days == 0) {
            System.out.println("Invalid book ID, Title or return date");
        } else {
            if (days > 37) {
                lateFee = Math.min((days - 37) * 0.5, 20);
            }
            borrowObj.returnBook(lateFee, Date.valueOf(returnDate), bookId, userId);
            BookOperations.bookObject.updateBookOnReturn(bookId);
            System.out.println("Book returned successfully");
            if (lateFee > 0) System.out.println("Late fee applied:$" + lateFee);
            else System.out.println("No late fee applied");
        }
    }

    //5 performing Generate Fee report operation
    public static void generateFeeReport() {
        System.out.println("Enter start date in the format yyyy-MM-dd:");
        String startDateStr = Main.input.next();

        System.out.println("Enter end date in the format yyyy-MM-dd:");
        String endDateStr = Main.input.next();
        LocalDate startDate = null;
        LocalDate endDate = null;
        List<Report> reportList = new ArrayList<>();
        int totalLateFee = 0;
        try {
            startDate = LocalDate.parse(startDateStr, dateFormatter);
            endDate = LocalDate.parse(endDateStr, dateFormatter);
            if (!endDate.isAfter(startDate)) {
                System.out.println("end Date Should be greater than start date");
                return;
            } else {
                reportList = borrowObj.generateReport(startDate.toString(), endDate.toString());
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        if (reportList.isEmpty()) {
            System.out.println("No overdue books or late fees recorded during the specified period");
        } else {
            for (Report report : reportList) {
                System.out.println(report);
                totalLateFee += report.getLateFee();
            }
            System.out.println("Total late fees collected: $" + totalLateFee);
        }
    }


}
