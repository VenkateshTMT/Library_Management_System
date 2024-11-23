package library.interfaces;

import library.models.BorrowedRecords;
import library.models.Report;

import java.sql.Date;
import java.util.List;

public interface BorrowedRecordsDao {
    int recordBorrowing(int userId, int bookId);

    BorrowedRecords getRecord(int bookId, int userId);

    void returnBook(double lateFee, Date returnDate, int bookId, int userId);

    List<Report> generateReport(String startDate, String endDate);

}
