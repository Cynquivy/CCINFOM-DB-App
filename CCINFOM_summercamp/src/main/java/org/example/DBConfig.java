package org.example;

import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBConfig.class.getResourceAsStream("/db.properties")) {
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db.properties from resources", e);
        }
    }

    public static String getUrl() { return props.getProperty("jdbc.url"); }
    public static String getUser() { return props.getProperty("jdbc.user"); }
    public static String getPassword() { return props.getProperty("jdbc.password"); }
}
