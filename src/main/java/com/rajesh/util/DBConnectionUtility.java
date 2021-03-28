package com.rajesh.util;

import com.rajesh.models.Student;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnectionUtility {

    private static final Logger logger = Logger.getLogger(DBConnectionUtility.class);

    private static String url;
    private static String db;
    private static String username;
    private static String password;
    private static String table;

    private static Connection connection;

    private DBConnectionUtility() {}

    public static void initializeDBConnectionUtility() {
        logger.info("Initializing - DBConnection Utility");
        url = PropertyUtility.getPropertyValue("url");
        db = PropertyUtility.getPropertyValue("db");
        username = PropertyUtility.getPropertyValue("username");
        password = PropertyUtility.getPropertyValue("password");
        table = PropertyUtility.getPropertyValue("table");
        logger.info("DB URL - "+url);
        logger.info("DB Name - "+db);
        logger.info("Initialization Successful - DBConnection Utility");
    }

    public static void openDBConnection() {
        try {
            logger.info("Establishing Connection with DB - "+db);
            connection = DriverManager.getConnection(url+db, username, password);
            logger.info("Established Connection with DB - "+db+" : "+!connection.isClosed());
        } catch (SQLException exception) {
            logger.error("ERROR Establishing Connection with DB - "+db);
            exception.printStackTrace();
        }
    }

    public static void closeDBConnection() {
        try {
            logger.info("Closing Connection with DB - "+db);
            connection.close();
            logger.info("Closed Connection with DB - "+db+" : "+connection.isClosed());
        } catch (SQLException exception) {
            logger.error("ERROR Closing Connection with DB - "+db);
            exception.printStackTrace();
        }
    }

    public static void checkTableAvailability() {
        try {
            logger.info("Checking Table Existence - "+table);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, table, null);
            if (tables.next()) {
                logger.info("Table Found - "+table);
            }
            else {
                logger.info("Table Not Found - "+table);
                createStudentsTable();
            }
        }
        catch (SQLException exception) {
            logger.error("ERROR Finding Table - "+table);
            exception.printStackTrace();
        }
    }

    public static boolean insertNewStudentIntoDB(Student student) {
        String query = "insert into students(sFirstName, sLastName, sAge, sSex) values (?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getsFirstName());
            statement.setString(2, student.getsLastName());
            statement.setString(3, student.getsAge());
            statement.setString(4, student.getsSex());
            logger.info("Inserting New Student Data in DB");
            statement.executeUpdate();
            logger.info("SUCCESS Inserting New Student Data in DB");
            return true;
        } catch (SQLException exception) {
            logger.error("ERROR Inserting New Student Data in DB");
            exception.printStackTrace();
            return false;
        }
    }

    public static List<Student> getAllStudentsData() {
        String query = "select * from students;";
        List<Student> studentList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            logger.info("Fetching Data from Table - "+table);
            ResultSet resultSet = statement.executeQuery(query);
            logger.info("SUCCESS Fetching Data from DB - "+db+", Table - "+table);
            int count = 0;
            while (resultSet.next()) {
                studentList.add(new Student(resultSet.getString(1),
                                            resultSet.getString(2),
                                            resultSet.getString(3),
                                            resultSet.getString(4),
                                            resultSet.getString(5)));
                count++;
            }
            logger.info("Number of Rows Fetched - "+count);
        } catch (SQLException exception) {
            logger.info("ERROR Fetching Data from Table - "+table);
            exception.printStackTrace();
        }
        return studentList;
    }

    public static void deleteStudentRecordById(String studentId) {
        int initialCount = getDataCountFromStudentTable();
        String query = "delete from students where sId="+studentId;
        try (Statement statement = connection.createStatement()) {
            logger.info("Attempting to Delete Student Record With ID - "+studentId);
            statement.executeUpdate(query);
            int currentCount = getDataCountFromStudentTable();
            if (currentCount < initialCount) {
                logger.info("SUCCESS Deleted Student Record With ID - "+studentId);
            }
            else {
                logger.info("FAILED to Delete Student Record With ID - "+studentId);
            }
        } catch (SQLException exception) {
            logger.info("ERROR Deleting Student Record With ID - "+studentId);
        }
    }

    public static List<Student> searchStudents(Student student) {
        String sId = (student.getId().equals("")) ? "%" : student.getId();
        String sFirstName = (student.getsFirstName().equals("")) ? "%" : student.getsFirstName();
        String sLastName = (student.getsLastName().equals("")) ? "%" : student.getsLastName();
        String sAge = (student.getsAge().equals("")) ? "%" : student.getsAge();
        String sSex = (student.getsSex().equals("")) ? "%" : student.getsSex();

        String query = String.format("select * from students where sId like '%s' and " +
                "sFirstName like '%s' and sLastName like '%s' and sAge like '%s' and sSex like '%s'",
                sId, sFirstName, sLastName, sAge, sSex);

        List<Student> studentList = new ArrayList<>();
        int count = 0;

        try (Statement statement = connection.createStatement()) {
            logger.info("Attempting to Fetch All Records Matching Filters");
            ResultSet resultSet = statement.executeQuery(query);
            logger.info("SUCCESS Fetching All Records Matching Filters");
            while (resultSet.next()) {
                studentList.add(new Student(resultSet.getString(1),
                                            resultSet.getString(2),
                                            resultSet.getString(3),
                                            resultSet.getString(4),
                                            resultSet.getString(5)));
                count++;
            }
            logger.info("All Records Matching Filters Count - "+count);
        } catch (SQLException exception) {
            logger.error("ERROR Fetching All Records Matching Filters");
            exception.printStackTrace();
        }
        return studentList;
    }

    public static void updateStudentRecord(Student student) {
        boolean isRecordAvailable = false;
        List<Student> studentList = getAllStudentsData();
        for (Student eachStudent : studentList) {
            if (eachStudent.getId().equals(student.getId())) {
                isRecordAvailable = true;
                break;
            }
        }

        if (isRecordAvailable) {
            String query = String.format("update students set sFirstName='%s',sLastName='%s',sAge='%s',sSex='%s' where sId='%s';",
                    student.getsFirstName(), student.getsLastName(), student.getsAge(), student.getsSex(), student.getId());

            try (Statement statement = connection.createStatement()) {
                logger.info("Attempting to Update Record Having ID - " + student.getId());
                statement.executeUpdate(query);
                logger.info("SUCCESS Updating Record Having ID - " + student.getId());
            } catch (SQLException exception) {
                logger.error("ERROR Updating Record Having ID - " + student.getId());
                exception.printStackTrace();
            }
        }
        else {
            logger.error("ERROR: No Record Found With ID - "+student.getId());
        }
    }

    private static int getDataCountFromStudentTable() {
        return getAllStudentsData().size();
    }

    private static void createStudentsTable() {
        try (Statement statement = connection.createStatement()) {
            logger.info("Creating Table - "+table);
            String query = String.format("create table %s (" +
                    "sId int(100) primary key auto_increment," +
                    "sFirstName varchar(255) not null," +
                    "sLastName varchar(255) not null," +
                    "sAge int(3) not null," +
                    "sSex varchar(1) not null);", table);
            statement.executeUpdate(query);
            logger.info("SUCCESS Table Created - "+table);
        }
        catch (SQLException exception) {
            logger.error("ERROR Creating Table - "+table);
            exception.printStackTrace();
        }
    }
}
