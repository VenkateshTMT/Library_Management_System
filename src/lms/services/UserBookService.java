package lms.services;

import exeception.CustomExeception;
import library.models.Book;
import library.models.BorrowedRecords;
import library.models.Report;
import library.interfaces.BorrowedRecordsDao;
import library.implementation.BorrowedRecordsDaoImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static lms.services.BookService.bookObject;

public class UserBookService {

    public static final Scanner input = new Scanner(System.in);

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static BorrowedRecordsDao borrowedRecordsDaoObj = new BorrowedRecordsDaoImpl();

    //1 this method check is book is available to borrow
    public static void borrowingBook() {
        try {
            Book book = null;

            System.out.println("Currently Available books");
            BookService.checkAvailableBooks();
            System.out.println("----------------------------------------------------");

            System.out.println("How you want to search for a book by ID or Title");
            String userInput = input.next();

            if (userInput.equalsIgnoreCase("title")) {
                System.out.println("Enter the book title:");
                String bookTitle = input.next();
                book = bookObject.getSpecificBookTitle(bookTitle.toLowerCase());
                validateAndRecordBook(book);
            } else if (userInput.equalsIgnoreCase("ID")) {
                System.out.println("enter the book id you want to borrow:");
                int id = input.nextInt();
                book = bookObject.getBookId(id);
                validateAndRecordBook(book);
            } else {
                System.out.println("enter valid Input either ID or Title");
            }
        } catch (Exception e) {
            throw new CustomExeception(e.getMessage());
        }

    }

    private static void validateAndRecordBook(Book book) {
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
        try{
            System.out.println("Enter userId:");
            int userId = input.nextInt();
            int bookId = book.getBookId();
            int count = borrowedRecordsDaoObj.recordBorrowing(userId, bookId);
            if (count > 0) {
                int updateCount=bookObject.updateBookOnBorrow(bookId);
                if (updateCount>0){
                    System.out.println("Book borrowed successfully");
                }else{
                    System.out.println("borrow unsuccessful");
                }
            } else {
                System.out.println("borrow unsuccessful try again");
            }
        }catch (Exception e){
            throw new CustomExeception(e.getMessage());
        }
    }

    //2 returning a book
    public static void returningBook() {
        try{
            Book book = null;

            System.out.println("Enter how you want to return, by Book ID or Book Title");
            String userInput = input.next();

            if (userInput.equalsIgnoreCase("title")) {
                System.out.println("Enter the title of the Book that you want to return:");
                String bookTitle = input.next();
                book = bookObject.getSpecificBookTitle(bookTitle.toLowerCase());
            } else if (userInput.equalsIgnoreCase("id")) {
                System.out.println("enter bookID you want to return:");
                int userBookId = input.nextInt();
                book = bookObject.getBookId(userBookId);
            } else {
                System.out.println("Invalid input,Select either Title or ID.");
                return;
            }

            BorrowedRecords record = null;
            int userId;

            if (book == null) {
                System.out.println("Invalid BookId,Title or Return Date. Please try again.");
            } else if (book.getStatus().equals("Borrowed")) {
                System.out.println("Enter the returning user userID :");
                userId = input.nextInt();
                int bookId = book.getBookId();
                record = borrowedRecordsDaoObj.getRecord(bookId, userId);
                if (record == null) {
                    System.out.println("User does not match");
                } else if (record.getReturnDate() != null) {
                    System.out.println("The book is already returned");
                } else {
                    doReturnOperation(record, bookId, userId);
                }
            } else {
                System.out.println("The book is not currently borrowed");
            }
        }catch (Exception e){
            throw new CustomExeception(e.getMessage());
        }

    }

    //2.1 performing returning book operation
    public static void doReturnOperation(BorrowedRecords record, int bookId, int userId) {

        System.out.println("Enter return date in yyyy-mm-dd format");
        String userInputDate = input.next();

        LocalDate returnDate = null;
        long days = 0;
        double lateFee = 0;
        LocalDate borrowedDate;
        try {
            returnDate = LocalDate.parse(userInputDate, dateFormatter);
            borrowedDate = record.getBorrowDate().toLocalDate();
            days = ChronoUnit.DAYS.between(borrowedDate, returnDate);
        } catch (DateTimeParseException e) {
            //System.out.println("Enter valid Date format " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }

        if (days == 0) {
            System.out.println("Invalid book ID, Title or return date");
        } else {
            if (days > 37) {
                lateFee = Math.min((days - 37) * 0.5, 20);
            }
            borrowedRecordsDaoObj.returnBook(lateFee, Date.valueOf(returnDate), bookId, userId);
            bookObject.updateBookOnReturn(bookId);
            System.out.println("Book returned successfully");
            if (lateFee > 0) System.out.println("Late fee applied:$" + lateFee);
            else System.out.println("No late fee applied");
        }

    }

    //5 performing Generate Fee report operation
    public static void generateFeeReport() {
        System.out.println("Enter start date in the format yyyy-MM-dd:");
        String startDateStr = input.next();

        System.out.println("Enter end date in the format yyyy-MM-dd:");
        String endDateStr = input.next();
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
                reportList = borrowedRecordsDaoObj.generateReport(startDate.toString(), endDate.toString());
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


        } catch (Exception e) {
            //System.out.println("Enter valid Date format " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }

    }


}
