package bankmanagementsystem;

import services.*;
import utils.Constants;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static CustomerService customerService = new CustomerService();
    private static AccountService accountService = new AccountService();
    private static TransactionService transactionService = new TransactionService();
    private static LoanService loanService = new LoanService();
    private static AuthService authService = new AuthService();

    public static void main(String[] args) {
        System.out.println(Constants.BANK_LOGO);
        System.out.println("=== WELCOME TO BANK MANAGEMENT SYSTEM ===\n");

        boolean exit = false;

        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleCustomerMenu();
                    break;
                case 2:
                    handleAccountMenu();
                    break;
                case 3:
                    handleTransactionMenu();
                    break;
                case 4:
                    handleLoanMenu();
                    break;
                case 5:
                    handleAdminLogin();
                    break;
                case 6:
                    handleEmployeeLogin();
                    break;
                case 7:
                    generateReports();
                    break;
                case 8:
                    System.out.println("\nThank you for using Bank Management System!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Customer Services");
        System.out.println("2. Account Management");
        System.out.println("3. Transaction Services");
        System.out.println("4. Loan Services");
        System.out.println("5. Admin Login");
        System.out.println("6. Employee Login");
        System.out.println("7. Generate Reports");
        System.out.println("8. Exit");
        System.out.println("===============================");
    }

    private static void handleCustomerMenu() {
        System.out.println("\n=== CUSTOMER SERVICES ===");
        System.out.println("1. Register New Customer");
        System.out.println("2. View Customer Details");
        System.out.println("3. Update Customer Information");
        System.out.println("4. Back to Main Menu");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1:
                customerService.registerCustomer();
                break;
            case 2:
                String customerId = getStringInput("Enter Customer ID: ");
                customerService.viewCustomerDetails(customerId);
                break;
            case 3:
                customerId = getStringInput("Enter Customer ID: ");
                customerService.updateCustomer(customerId);
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void handleAccountMenu() {
        System.out.println("\n=== ACCOUNT MANAGEMENT ===");
        System.out.println("1. Open New Account");
        System.out.println("2. View Account Balance");
        System.out.println("3. View Account Statement");
        System.out.println("4. Close Account");
        System.out.println("5. Back to Main Menu");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1:
                accountService.openAccount();
                break;
            case 2:
                String accountNo = getStringInput("Enter Account Number: ");
                accountService.viewBalance(accountNo);
                break;
            case 3:
                accountNo = getStringInput("Enter Account Number: ");
                accountService.viewStatement(accountNo);
                break;
            case 4:
                accountNo = getStringInput("Enter Account Number: ");
                accountService.closeAccount(accountNo);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void handleTransactionMenu() {
        System.out.println("\n=== TRANSACTION SERVICES ===");
        System.out.println("1. Deposit Money");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Transfer Funds");
        System.out.println("4. View Transaction History");
        System.out.println("5. Back to Main Menu");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1:
                transactionService.deposit();
                break;
            case 2:
                transactionService.withdraw();
                break;
            case 3:
                transactionService.transfer();
                break;
            case 4:
                String accountNo = getStringInput("Enter Account Number: ");
                transactionService.viewTransactionHistory(accountNo);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void handleLoanMenu() {
        System.out.println("\n=== LOAN SERVICES ===");
        System.out.println("1. Apply for Loan");
        System.out.println("2. View Loan Status");
        System.out.println("3. Make Loan Payment");
        System.out.println("4. View Loan Details");
        System.out.println("5. Back to Main Menu");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1:
                loanService.applyForLoan();
                break;
            case 2:
                String loanId = getStringInput("Enter Loan ID: ");
                loanService.viewLoanStatus(loanId);
                break;
            case 3:
                loanId = getStringInput("Enter Loan ID: ");
                double amount = getDoubleInput("Enter Payment Amount: ");
                loanService.makePayment(loanId, amount);
                break;
            case 4:
                loanId = getStringInput("Enter Loan ID: ");
                loanService.viewLoanDetails(loanId);
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void handleAdminLogin() {
        System.out.println("=== ADMIN LOGIN ===");
        String username = getStringInput("Username: ");
        String password = getStringInput("Password: ");

        if (username.equals("admin") && password.equals("admin123")) { // Changed password
            System.out.println("Admin login successful!");
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }

    private static void handleEmployeeLogin() {
        System.out.println("\n=== EMPLOYEE LOGIN ===");
        String empId = getStringInput("Employee ID: ");
        String password = getStringInput("Password: ");

        if (empId.equals("emp001") && password.equals("emp123")) {
            System.out.println("employee login   successful!");
        } else {
            System.out.println("Invalid employee credentials!");
        }
    }

    private static void generateReports() {
        System.out.println("\n=== REPORTS ===");
        System.out.println("1. Daily Transaction Report");
        System.out.println("2. Customer List Report");
        System.out.println("3. Loan Portfolio Report");
        System.out.println("4. Account Summary Report");

        int choice = getIntInput("Enter choice: ");
        switch (choice) {
            case 1:
                transactionService.generateDailyReport();
                break;
            case 2:
                customerService.generateCustomerReport();
                break;
            case 3:
                loanService.generateLoanReport();
                break;
            case 4:
                accountService.generateAccountReport();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        return value;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        return value;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}