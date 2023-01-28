package com.epam.tc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesExtractor {

    public static Properties getProperties() {

        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {

            Properties properties = new Properties();
            properties.load(input);
            return properties;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getKey() {
        String apiKey = getProperties().getProperty("key");
        return apiKey;
    }

    public static String getToken() {
        String apiToken = getProperties().getProperty("token");
        return apiToken;
    }

}
