package com.eoms.app;

import com.eoms.abstract_factory.ui.Dashboard;
import com.eoms.abstract_factory.ui.Menu;
import com.eoms.abstract_factory.ui.UserRole;
import com.eoms.config.UIFactoryRegistry;
import com.eoms.Boundary.AdminReportView;
import com.eoms.Boundary.ProductCatalogView;
import java.util.Scanner;
import com.eoms.util.InputValidator;

public class AdminRoleHandler implements RoleHandler {

    private final ProductCatalogView catalogView;
    private final AdminReportView reportView;

    public AdminRoleHandler(ProductCatalogView catalogView, AdminReportView reportView) {
        this.catalogView = catalogView;
        this.reportView = reportView;
    }

    @Override
    public void handle(Scanner scanner) {
        Dashboard dashboard = UIFactoryRegistry.getUIFactory(UserRole.ADMIN).createDashboard();
        Menu menu = UIFactoryRegistry.getUIFactory(UserRole.ADMIN).createMenu();

        dashboard.showDashboard();
        boolean adminRunning = true;

        while (adminRunning) {
            menu.showMenu();

            int choice;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                InputValidator.validateRange(choice, 0, 3, "Menu choice");
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    catalogView.addProduct();
                    break;

                case 2:
                    catalogView.displayProducts();
                    break;

                case 3:
                    reportView.promptAndGenerate();
                    break;

                case 0:
                    adminRunning = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}