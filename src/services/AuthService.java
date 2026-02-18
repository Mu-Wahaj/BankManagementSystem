package services;

import models.Employee;
import models.Admin;
import java.util.List;

public class AuthService {

    public boolean authenticateAdmin(String username, String password) {
        List<String> admins = FileService.readFile("admins.txt");

        System.out.println("DEBUG: Looking for admin. Found " + admins.size() + " admins in file.");

        for (String adminData : admins) {
            System.out.println("DEBUG: Raw admin data: " + adminData);
            Admin admin = Admin.fromString(adminData);
            if (admin != null) {
                System.out.println("DEBUG: Parsed admin - Username: " + admin.getUsername() + ", Password: " + admin.getPassword());
                if (admin.authenticate(username, password)) {
                    System.out.println("DEBUG: Authentication SUCCESS!");
                    return true;
                }
            } else {
                System.out.println("DEBUG: Failed to parse admin from: " + adminData);
            }
        }
        System.out.println("DEBUG: Authentication FAILED for " + username);
        return false;
    }

    public boolean authenticateEmployee(String empId, String password) {
        List<String> employees = FileService.readFile("employees.txt");

        for (String empData : employees) {
            Employee employee = Employee.fromString(empData);
            if (employee != null && employee.getEmployeeId().equals(empId) &&
                    employee.authenticate(password)) {
                return true;
            }
        }
        return false;
    }

    // Initialize default admin and employee accounts
    public static void initializeDefaultAccounts() {
        System.out.println("=== Initializing default accounts ===");

        // Check if admin file exists, create default admin if not
        List<String> admins = FileService.readFile("admins.txt");
        System.out.println("Current admins in file: " + admins.size());

        if (admins.isEmpty()) {
            System.out.println("Creating default admin account...");

            // Create default admin
            Admin defaultAdmin = new Admin(
                    "ADM001",           // adminId
                    "admin",            // username
                    "admin123",         // password
                    "Super Admin",      // accessLevel
                    "admin@bank.com"    // email
                    // status will be "Active" by default from constructor
            );

            String adminData = defaultAdmin.toString();
            System.out.println("Admin data to save: " + adminData);

            FileService.appendToFile("admins.txt", adminData);
            System.out.println("✓ Default admin created: Username: 'admin', Password: 'admin123'");
        } else {
            System.out.println("Admin file already exists. First admin record: " + admins.get(0));
        }

        // Check if employee file exists, create default employee if not
        List<String> employees = FileService.readFile("employees.txt");
        System.out.println("Current employees in file: " + employees.size());

        if (employees.isEmpty()) {
            System.out.println("Creating default employee account...");
            Employee defaultEmployee = new Employee(
                    "EMP001",
                    "John Doe",
                    "Teller",
                    "Operations",
                    50000,
                    "Teller",
                    "emp123"
            );

            FileService.appendToFile("employees.txt", defaultEmployee.toString());
            System.out.println("✓ Default employee created");
        }

        System.out.println("=== Account initialization complete ===");
    }
}