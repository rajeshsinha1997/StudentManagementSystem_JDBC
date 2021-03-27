package com.rajesh.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtility {

    private static final Logger logger = Logger.getLogger(PropertyUtility.class);
    private static Map<String, String> propertyMap;

    private PropertyUtility(){}

    public static void initializePropertyUtility() {
        logger.info("Initializing - Property Utility");
        propertyMap = new HashMap<>();

        Properties properties = new Properties();
        try (FileInputStream inputStream =
                     new FileInputStream(System.getProperty("user.dir") +
                "/src/main/resources/configuration.properties")) {

            properties.load(inputStream);
            for (Map.Entry<Object, Object> objectObjectEntry : properties.entrySet()) {
                propertyMap.put(objectObjectEntry.getKey().toString(),
                        objectObjectEntry.getValue().toString());
            }
            logger.info("Initialization Successful - Property Utility");
        }
        catch (IOException e) {
            logger.error("Initialization Failed - Property Utility");
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key) {
        for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
