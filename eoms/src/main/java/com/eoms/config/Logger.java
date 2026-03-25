package com.eoms.config;

public class Logger {

    private static Logger instance;

   
    private Logger() {
    }

    // Global access point to the single instance
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Log message
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }

    // info message
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    //error message
    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }
}