package com.eoms;

import com.eoms.app.EomsApplication;
import java.util.Scanner;

/**
 * Application entry point.  Responsibility is limited to creating the application
 * object and starting it.  All prior business logic has been moved elsewhere to
 * improve maintainability and keep Main SOLID-compliant.
 */

public class Main {



public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    new EomsApplication(scanner).run();
    scanner.close();
}

}