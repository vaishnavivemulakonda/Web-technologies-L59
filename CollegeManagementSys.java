import java.sql.*;
import java.util.Scanner;

public class CollegeManagementSys    {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/collegedb";
    private static final String DB_USER = "System"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "System"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            createCoursesTable(conn);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Choose an operation: 1. Insert 2. Update 3. Delete 4. Display 5. Exit");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        insertCourse(conn, scanner);
                        break;
                    case 2:
                        updateCourse(conn, scanner);
                        break;
                    case 3:
                        deleteCourse(conn, scanner);
                        break;
                    case 4:
                        displayCourses(conn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createCoursesTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (CourseID INT AUTO_INCREMENT PRIMARY KEY, Name VARCHAR(255), Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'Courses' created successfully.");
        }
    }

    private static void insertCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter course name: ");
        scanner.nextLine(); // Consume newline left-over
        String name = scanner.nextLine();
        System.out.print("Enter credits: ");
        int credits = scanner.nextInt();

        String sql = "INSERT INTO Courses (Name, Credits) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter CourseID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new credits: ");
        int credits = scanner.nextInt();

        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Record updated successfully." : "Course not found.");
        }
    }

    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter CourseID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Record deleted successfully." : "Course not found.");
        }
    }

    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Courses Table Records:");
            System.out.println("-----------------------------------------");
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") + " | Name: " + rs.getString("Name") + " | Credits: " + rs.getInt("Credits"));
            }
        }
    }
}