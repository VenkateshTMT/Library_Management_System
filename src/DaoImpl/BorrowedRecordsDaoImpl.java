package DaoImpl;

import LibraryInterfaces.BorrowedRecordsDao;
import ModelDaoclasses.BorrowedRecords;
import ModelDaoclasses.Report;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BorrowedRecordsDaoImpl implements BorrowedRecordsDao {

    public static final String SELECT_RECORD = "select * from borrowed_books where bookId=? and userId=?";

    public static final String INSERT_RECORD = "insert into borrowed_books (bookId,userId,borrowedDate,returnDate,lateFee) values (?,?,?,?,?)";

    public static final String UPDATE_RECORD = "update `borrowed_books` set `returnDate`=?,`lateFee`=? where bookId=? and userId=?";

    public static final String GENERATE_REPORT_QUERY = "select * from borrowed_books inner join " +
            "book on borrowed_books.bookId=book.bookId " +
            "inner join user on borrowed_books.userID=user.userId " +
            "where borrowed_books.returnDate between ? and ? " +
            "order by borrowed_books.userId";

    Connection connection = null;

    public BorrowedRecordsDaoImpl() {
        String url = "jdbc:mysql://localhost:3306/library_management";
        String username = "root";
        String password = "root";
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

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
            e.printStackTrace();
        }
        return null;
    }

    public void returnBook(double lateFee, Date returnDate, int bookId, int userId) {
        PreparedStatement pstmt;
        try {
            pstmt = connection.prepareStatement(UPDATE_RECORD);
            pstmt.setDate(1, returnDate);
            pstmt.setDouble(2, lateFee);
            pstmt.setInt(3, bookId);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private long overDueDays(Date startDate, Date endDate) {
        LocalDate startLocalDate = startDate.toLocalDate();
        LocalDate endLocalDate = endDate.toLocalDate();
        long days = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        if (days>37){
            return days - 37;
        }
       return 0;
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
            while (res.next()) {
                int bookId = res.getInt("bookId");
                String title = res.getString("title");
                String userName = res.getString("name");
                double lateFee = res.getDouble("lateFee");
                Date borrowedDate = res.getDate("borrowedDate");
                Date returnedDate = res.getDate("returnDate");
                long daysOverDue = overDueDays(borrowedDate, returnedDate);
                Report report = new Report(bookId, title, userName, daysOverDue, lateFee);
                reportsList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportsList;
    }

    public void closeBorrowedRecordsConnection() {
        try {
            if (connection != null) {
                connection.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
