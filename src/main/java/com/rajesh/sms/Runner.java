package com.rajesh.sms;

import com.rajesh.util.DBConnectionUtility;
import com.rajesh.util.PropertyUtility;

public class Runner {

    public static void main(String[] args) {
        performInitializations();
    }

    private static void performInitializations() {
        PropertyUtility.initializePropertyUtility();
        DBConnectionUtility.initializeDBConnectionUtility();
        DBConnectionUtility.openDBConnection();
        DBConnectionUtility.checkTableAvailability();
    }
}
