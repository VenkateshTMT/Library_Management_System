package library.implementation;

import db.connection.GetConnection;
import exeception.CustomExeception;
import library.interfaces.BorrowedRecordsDao;
import library.models.BorrowedRecords;
import library.models.Report;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class BorrowedRecordsDaoImpl implements BorrowedRecordsDao {

    private static final Logger logger = Logger.getLogger(BorrowedRecordsDaoImpl.class.getName());

    public static final String SELECT_RECORD = "select * from borrowed_books where bookId=? and userId=?";

    public static final String INSERT_RECORD = "insert into borrowed_books (bookId,userId,borrowedDate,returnDate,lateFee) values (?,?,?,?,?)";

    public static final String UPDATE_RECORD = "update `borrowed_books` set `returnDate`=?,`lateFee`=? where bookId=? and userId=?";

    public static final String GENERATE_REPORT_QUERY = "select * from borrowed_books inner join " +
            "book on borrowed_books.bookId=book.bookId " +
            "inner join user on borrowed_books.userID=user.userId " +
            "where borrowed_books.borrowedDate between ? and ? " +
            "order by borrowed_books.userId";

    Connection connection = null;

    public BorrowedRecordsDaoImpl() {
        connection= GetConnection.makeConnection();
    }

    @Override
    public int recordBorrowing(int userId, int bookId) {
        PreparedStatement pstmt;
        Date currentDate = Date.valueOf(LocalDate.now());
        int count = 0;
        try {
            pstmt = connection.prepareStatement(INSERT_RECORD);
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, currentDate);
            pstmt.setDate(4, null);
            pstmt.setBigDecimal(5, null);
            count = pstmt.executeUpdate();
            logger.info("Successfully recorded borrowing for User ID: " + userId + " and Book ID: " + bookId);
        } catch (SQLException e) {
            logger.severe("Error while recording borrowing: " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }
        return count;
    }

    @Override
    public BorrowedRecords getRecord(int bookId, int userId) {
        PreparedStatement pstmt;
        ResultSet res;
        try {
            pstmt = connection.prepareStatement(SELECT_RECORD);
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, userId);
            res = pstmt.executeQuery();
            if (res.next()) {
                int borrowId = res.getInt("borrowId");
                Date borrowedDate = res.getDate("borrowedDate");
                Date returnDate = res.getDate("returnDate");
                double lateFee = res.getDouble("lateFee");
                return new BorrowedRecords(borrowId, bookId, userId, borrowedDate, returnDate, lateFee);
            }
        } catch (SQLException e) {
            logger.severe("Error while fetching borrowed record for User ID: " + userId + " and Book ID: " + bookId + " - " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }
        return null;
    }

    @Override
    public void returnBook(double lateFee, Date returnDate, int bookId, int userId) {
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(UPDATE_RECORD);
            pstmt.setDate(1, returnDate);
            pstmt.setDouble(2, lateFee);
            pstmt.setInt(3, bookId);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
            logger.info("Successfully returned book for User ID: " + userId + " and Book ID: " + bookId);
        } catch (SQLException e) {
            logger.severe("Error while returning book for User ID: " + userId + " and Book ID: " + bookId + " - " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }
    }


    private long overDueDays(Date borrowedDate, Date returnDate) {
        LocalDate startLocalDate = borrowedDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        long days = 0;
        if (returnDate == null) {
            days = ChronoUnit.DAYS.between(startLocalDate, currentDate);
            if (days > 37) {
                days -= 37;
            } else {
                days = 0;
            }
        } else {
            days = ChronoUnit.DAYS.between(startLocalDate, returnDate.toLocalDate());
            if (days > 37) {
                days -= 37;
            } else {
                days = 0;
            }
        }
        return days;
    }

    @Override
    public List<Report> generateReport(String startDate, String endDate) {
        PreparedStatement pstmt;
        ResultSet res = null;
        ArrayList<Report> reportsList = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(GENERATE_REPORT_QUERY);
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            res = pstmt.executeQuery();
            double lateFee;
            while (res.next()) {
                int bookId = res.getInt("bookId");
                String title = res.getString("title");
                String userName = res.getString("name");
                lateFee = res.getDouble("lateFee");
                Date borrowedDate = res.getDate("borrowedDate");
                Date returnedDate = res.getDate("returnDate");
                long daysOverDue = overDueDays(borrowedDate, returnedDate);
                if (returnedDate == null && daysOverDue > 0) {
                    lateFee = Math.min(daysOverDue * 0.5, 20);
                }
                Report report = new Report(bookId, title, userName, daysOverDue, lateFee);
                reportsList.add(report);
            }
        } catch (SQLException e) {
            logger.severe("Error while generating report: " + e.getMessage());
            throw new CustomExeception(e.getMessage());
        }
        return reportsList;
    }

}
