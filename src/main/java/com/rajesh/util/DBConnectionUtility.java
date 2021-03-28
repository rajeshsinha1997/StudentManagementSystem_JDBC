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

    public static boolean insertNewStudentIntoDB(Student student) {
        String query = "insert into students(sFirstName, sLastName, sAge, sSex) values (?,?,?,?);";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getsFirstName());
            statement.setString(2, student.getsLastName());
            statement.setInt(3, student.getsAge());
            statement.setString(4, Character.toString(student.getsSex()));
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
                studentList.add(new Student(resultSet.getInt(1),
                                            resultSet.getString(2),
                                            resultSet.getString(3),
                                            resultSet.getInt(4),
                                            resultSet.getString(5).charAt(0)));
                count++;
            }
            logger.info("Number of Rows Fetched - "+count);
        } catch (SQLException exception) {
            logger.info("ERROR Fetching Data from Table - "+table);
            exception.printStackTrace();
        }
        return studentList;
    }
}
