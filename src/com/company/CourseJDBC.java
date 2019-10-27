import java.sql.*;
import java.util.Scanner;


public class CourseJDBC {

    public Connection connect(String url)
            throws SQLException {
        return DriverManager.getConnection(url);
    }

    public void PresentStudents(ResultSet res)
            throws SQLException {
        if (res == null)
            System.out.println("No records for customer");
        while (res != null & res.next()) {
            String name = res.getString("Name");
            System.out.println(name);
        }
    }

    public ResultSet plainstatement(String student, Connection conn)
            throws SQLException {
        String query = "select Students.Name As Name, Courses.Name As Course, Study.Grade As Grade from Study" +
                " INNER JOIN Students ON Students.Studentid = Study.Studentid " +
                "INNER JOIN Courses ON Courses.Courseid = Study.Courseid" +
                " Where Students.Name = '" + student + "'";
        Statement stmt = null;
        ResultSet res = null;
        stmt = conn.createStatement();
        res = stmt.executeQuery(query);
        return res;
    }

    public PreparedStatement selectpreparedstatement(Connection conn)
            throws SQLException {
        String query = "select Students.Name As Name, Courses.Name As Course, Study.Grade As Grade from Study" +
                " INNER JOIN Students ON Students.Studentid = Study.Studentid " +
                "INNER JOIN Courses ON Courses.Courseid = Study.Courseid" +
                " Where Students.Name = ? ";
        PreparedStatement selectpstmt = null;
        selectpstmt = conn.prepareStatement(query);
        return selectpstmt;
    }

    public PreparedStatement Updatepreparedstatement(Connection conn)
            throws SQLException {
        String sqlUpdate = "UPDATE n Study "
                + " SET Grade = ? "
                + " WHERE (Studentid, Courseid) "
                + " IN ( SELECT s.Studentid,c.Courseid "
                + " FROM Students As s, Courses As c"
                + " WHERE s.Name= ? AND c.Name = ? )";
        PreparedStatement updatepstmt = null;
        updatepstmt = conn.prepareStatement(sqlUpdate);
        return updatepstmt;
    }

    public PreparedStatement InsertStudentpreparedstatement(Connection conn)
            throws SQLException {
        String sqlInsert = "INSERT INTO Students (name, town , age, sex) VALUES(?,?,?,?)";
        PreparedStatement insertpstmt = null;
        insertpstmt = conn.prepareStatement(sqlInsert);
        return insertpstmt;
    }

    public PreparedStatement InsertCoursepreparedstatement(Connection conn)
            throws SQLException {
        String sqlInsert = "INSERT INTO Courses (Courseid,Name, Teacher, Description, Semester) VALUES(?,?,?,?,?)";
        PreparedStatement insertpstmt = null;
        insertpstmt = conn.prepareStatement(sqlInsert);
        return insertpstmt;
    }

    public PreparedStatement InsertStudypreparedstatement(Connection conn)
            throws SQLException {
        String sqlInsert = "INSERT INTO Study (Studentid, Courseid, Grade) SELECT s.Studentid,c.Courseid, ? " +
                "    FROM Students As s, Courses As c " +
                "        WHERE s.Name= ? AND c.Name = ? ;";
        PreparedStatement insertpstmt = null;
        insertpstmt = conn.prepareStatement(sqlInsert);
        return insertpstmt;
    }


    public static void main(String[] args) {
        CourseJDBC CJ = new CourseJDBC();
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:/Users/liner/Documents/SD2019E/SQLiteDatabases/Enrollment.db";
            conn = CJ.connect(url);
// con.setAutoCommit (false);
            //Select;
            System.out.println("Which student do your wish to find?");
            Scanner scanner = new Scanner(System.in);
            String student = scanner.nextLine();
            System.out.println("Which course do your wish to find?");
            String course = scanner.nextLine();
            System.out.println("Which grade did the student get?");
            int scgrade = scanner.nextInt();
            PreparedStatement pstmt = CJ.Updatepreparedstatement(conn);
            pstmt.setInt(1, scgrade);
            pstmt.setString(2, student);
            pstmt.setString(3, course);
            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Rows affected %d", rowAffected));
            ResultSet res = CJ.plainstatement(student, conn);
            CJ.PresentStudents(res);
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}















