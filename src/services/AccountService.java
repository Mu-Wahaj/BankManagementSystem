package services;

import models.Account;
import utils.Validation;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AccountService {
    private static Scanner scanner = new Scanner(System.in);

    public void openAccount() {
        System.out.println("\n=== OPEN NEW ACCOUNT ===");

        // Get customer ID
        String customerId = getInput("Enter Customer ID: ");

        // Check if customer exists
        String customerData = FileService.searchRecord("customers.txt", customerId);
        if (customerData == null) {
            System.out.println("Customer not found! Please register customer first.");
            return;
        }

        // Select account type
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Fixed Deposit Account");
        System.out.print("Enter choice (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        String accountType = "";
        switch (choice) {
            case 1:
                accountType = "Savings";
                break;
            case 2:
                accountType = "Current";
                break;
            case 3:
                accountType = "Fixed Deposit";
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        // Get initial deposit
        double initialDeposit = 0;
        while (true) {
            System.out.print("Enter Initial Deposit: ");
            initialDeposit = scanner.nextDouble();
            scanner.nextLine();

            // Validate minimum deposit
            double minDeposit = 0;
            switch (accountType) {
                case "Savings":
                    minDeposit = 1000;
                    break;
                case "Current":
                    minDeposit = 5000;
                    break;
                case "Fixed Deposit":
                    minDeposit = 10000;
                    break;
            }

            if (initialDeposit >= minDeposit) {
                break;
            } else {
                System.out.println("Minimum deposit for " + accountType + " account is: " + minDeposit);
            }
        }

        // Generate account number
        String accountNumber = generateAccountNumber();

        // Create account object
        Account account = new Account(accountNumber, customerId, accountType, initialDeposit);

        // Save to file
        FileService.appendToFile("accounts.txt", account.toString());

        System.out.println("\n✓ Account created successfully!");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Initial Balance: " + initialDeposit);

        // Create initial transaction record
        String transactionData = "SYSTEM|" + accountNumber + "|Deposit|" + initialDeposit +
                "|" + LocalDate.now() + "|Initial Deposit|Completed|INIT" + accountNumber;
        FileService.appendToFile("transactions.txt", transactionData);
    }

    public void viewBalance(String accountNumber) {
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);
        if (accountData != null) {
            Account account = Account.fromString(accountData);
            if (account != null) {
                System.out.println("\n=== ACCOUNT BALANCE ===");
                System.out.println("Account Number: " + accountNumber);
                System.out.println("Current Balance: PKR " + account.getBalance());
                System.out.println("Account Status: " + account.getStatus());
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    public void viewStatement(String accountNumber) {
        System.out.println("\n=== ACCOUNT STATEMENT ===");

        // Get account details
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);
        if (accountData == null) {
            System.out.println("Account not found!");
            return;
        }

        Account account = Account.fromString(accountData);
        account.displayDetails();

        // Get transactions for this account
        List<String> transactions = FileService.searchAllRecords("transactions.txt", accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
        } else {
            System.out.println("\n=== TRANSACTION HISTORY ===");
            System.out.println("Date\t\tType\t\tAmount\t\tDescription");
            System.out.println("------------------------------------------------");

            double totalDeposits = 0;
            double totalWithdrawals = 0;

            for (String transaction : transactions) {
                String[] parts = transaction.split("\\|");
                if (parts.length >= 5) {
                    System.out.printf("%s\t%s\t%.2f\t\t%s\n",
                            parts[4], parts[2], Double.parseDouble(parts[3]), parts[5]);

                    if (parts[2].equals("Deposit")) {
                        totalDeposits += Double.parseDouble(parts[3]);
                    } else if (parts[2].equals("Withdrawal")) {
                        totalWithdrawals += Double.parseDouble(parts[3]);
                    }
                }
            }

            System.out.println("\n=== SUMMARY ===");
            System.out.println("Total Deposits: PKR " + totalDeposits);
            System.out.println("Total Withdrawals: PKR " + totalWithdrawals);
            System.out.println("Current Balance: PKR " + account.getBalance());
        }
    }

    public void closeAccount(String accountNumber) {
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);
        if (accountData != null) {
            Account account = Account.fromString(accountData);

            if (account.getStatus().equals("Closed")) {
                System.out.println("Account is already closed!");
                return;
            }

            if (account.getBalance() > 0) {
                System.out.println("Account has balance of PKR " + account.getBalance());
                System.out.print("Do you want to withdraw remaining balance? (yes/no): ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("yes")) {
                    System.out.println("Please visit branch to withdraw remaining balance.");
                    return;
                }
            }

            // Confirm closure
            System.out.print("Are you sure you want to close this account? (yes/no): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {
                account.setStatus("Closed");
                FileService.updateRecord("accounts.txt", accountData, account.toString());
                System.out.println("Account closed successfully!");
            } else {
                System.out.println("Account closure cancelled.");
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    public void generateAccountReport() {
        System.out.println("\n=== ACCOUNT SUMMARY REPORT ===");
        List<String> accounts = FileService.readFile("accounts.txt");

        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        int totalAccounts = 0;
        int activeAccounts = 0;
        int closedAccounts = 0;
        double totalBalance = 0;

        System.out.println("\nAccount No\t\tCustomer ID\tType\t\tBalance\t\tStatus");
        System.out.println("----------------------------------------------------------------");

        for (String accountData : accounts) {
            Account account = Account.fromString(accountData);
            if (account != null) {
                totalAccounts++;
                totalBalance += account.getBalance();

                if (account.getStatus().equals("Active")) {
                    activeAccounts++;
                } else if (account.getStatus().equals("Closed")) {
                    closedAccounts++;
                }

                System.out.printf("%s\t%s\t%s\t%.2f\t\t%s\n",
                        account.getAccountNumber(),
                        account.getCustomerId(),
                        account.getAccountType(),
                        account.getBalance(),
                        account.getStatus());
            }
        }

        System.out.println("\n=== REPORT SUMMARY ===");
        System.out.println("Total Accounts: " + totalAccounts);
        System.out.println("Active Accounts: " + activeAccounts);
        System.out.println("Closed Accounts: " + closedAccounts);
        System.out.println("Total Balance: PKR " + totalBalance);
    }

    private String generateAccountNumber() {
        // Generate account number: BranchCode + Year + Sequence
        String branchCode = "001"; // Default branch
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        int sequence = FileService.countRecords("accounts.txt") + 1;
        return branchCode + year + String.format("%06d", sequence);
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}