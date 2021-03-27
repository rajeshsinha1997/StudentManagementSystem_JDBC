package com.rajesh.util;

import org.apache.log4j.Logger;

import java.sql.*;

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
}
