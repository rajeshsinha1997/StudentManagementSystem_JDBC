package com.rajesh.sms;

import com.rajesh.models.Student;
import com.rajesh.util.DBConnectionUtility;
import com.rajesh.util.PropertyUtility;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Runner {

    private static final String DIVIDER = "---------------------------------";
    private static final Logger logger = Logger.getLogger(Runner.class);
    private static final BufferedReader scanner =
            new BufferedReader(new InputStreamReader(System.in));

    private static Student student;

    public static void main(String[] args) {
        performInitializations();
        printWelComeInformation();
        acceptUserInput();
    }

    private static void acceptUserInput() {
        int inputChoice = -1;
        while (inputChoice != 0) {
            logger.info("1. View Student List");
            logger.info("2. Search For Student");
            logger.info("3. Edit A Student's Details");
            logger.info("4. Insert New Student's Details");
            logger.info("0. Exit");
            logger.info("Enter Your Input Below:");
            try {
                inputChoice = Integer.parseInt(scanner.readLine());
                performActionByUserChoice(inputChoice);
            } catch (NumberFormatException e) {
                logger.error("ERROR: Invalid Input - "+inputChoice);
                acceptUserInput();
            } catch (IOException e) {
                logger.error("ERROR: Reading User Input");
                acceptUserInput();
            }
        }
    }

    private static void performActionByUserChoice(int inputChoice) {
        switch (inputChoice) {
            case 1:
                // show student list
                break;
            case 2:
                // search for student
                break;
            case 3:
                // edit student details
                break;
            case 4:
                getStudentInsertionData();
                DBConnectionUtility.insertNewStudentIntoDB(student);
                break;
            case 0:
                logger.info("Good Bye!");
                DBConnectionUtility.closeDBConnection();
                break;
            default:
                logger.info("Invalid Input - "+inputChoice);
                logger.info("Please Provide Valid Input");
                break;
        }
    }

    private static void getStudentInsertionData() {
        student = new Student();
        student.setsFirstName(getStudentFirstName());
        student.setsLastName(getStudentLastName());
        student.setsAge(getStudentAge());
        student.setsSex(getStudentSex());
    }

    private static String getStudentFirstName() {
        logger.info(DIVIDER);
        logger.info("Enter Student First Name: ");
        try {
            String fName = scanner.readLine();
            if (fName != null) return fName;
            logger.error("ERROR: Invalid Student First Name - " + fName);
            return getStudentFirstName();
        }
        catch (IOException exception) {
            logger.error("ERROR: Reading Student First Name");
            return getStudentFirstName();
        }
    }

    private static String getStudentLastName() {
        logger.info(DIVIDER);
        logger.info("Enter Student Last Name: ");
        try {
            String lName = scanner.readLine();
            if (lName != null) return lName;
            logger.error("ERROR: Invalid Student Last Name - " + lName);
            return getStudentLastName();
        }
        catch (IOException exception) {
            logger.error("ERROR: Reading Student Last Name");
            return getStudentLastName();
        }
    }

    private static int getStudentAge() {
        logger.info(DIVIDER);
        logger.info("Enter Student Age: ");
        String age = "0";
        try {
            age = scanner.readLine();
            return Integer.parseInt(age);
        }
        catch (NumberFormatException exception) {
            logger.error("ERROR: Invalid Student Age - "+age);
            return getStudentAge();
        }
        catch (IOException exception) {
            logger.error("ERROR: Reading Student Age");
            return getStudentAge();
        }
    }

    private static char getStudentSex() {
        logger.info(DIVIDER);
        logger.info("Enter Student Sex (M/F): ");
        try {
            char sex = scanner.readLine().toLowerCase().charAt(0);
            if (sex == 'm' || sex == 'f') {
                return sex;
            } else {
                logger.error("ERROR: Invalid Student Sex");
                return getStudentSex();
            }
        }
        catch (IOException exception) {
            logger.error("ERROR: Reading Student Sex");
            return getStudentSex();
        }
    }

    private static void printWelComeInformation() {
        logger.info(DIVIDER);
        logger.info("|      STARTING APPLICATION     |");
        logger.info(DIVIDER);
        logger.info("--- Student Management System ---");
        logger.info("---    Owner: RAJESH SINHA    ---");
        logger.info("---    Language Used: JAVA    ---");
        logger.info("---     DB API Used: JDBC     ---");
        logger.info("---       DB Used: MySQL      ---");
        logger.info(DIVIDER);
    }

    private static void performInitializations() {
        PropertyUtility.initializePropertyUtility();
        DBConnectionUtility.initializeDBConnectionUtility();
        DBConnectionUtility.openDBConnection();
        DBConnectionUtility.checkTableAvailability();
    }
}
