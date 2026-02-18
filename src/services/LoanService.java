package services;

import models.Loan;
import models.Account;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LoanService {
    private static Scanner scanner = new Scanner(System.in);

    public void applyForLoan() {
        System.out.println("\n=== APPLY FOR LOAN ===");

        String customerId = getInput("Enter Customer ID: ");
        String customerData = FileService.searchRecord("customers.txt", customerId);

        if (customerData == null) {
            System.out.println("Customer not found!");
            return;
        }

        // Display loan types
        System.out.println("\nSelect Loan Type:");
        System.out.println("1. Personal Loan (15% interest)");
        System.out.println("2. Home Loan (10% interest)");
        System.out.println("3. Business Loan (12% interest)");
        System.out.println("4. Auto Loan (13% interest)");
        System.out.println("5. Education Loan (8% interest)");
        System.out.print("Enter choice (1-5): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String loanType = "";
        switch (choice) {
            case 1: loanType = "Personal"; break;
            case 2: loanType = "Home"; break;
            case 3: loanType = "Business"; break;
            case 4: loanType = "Auto"; break;
            case 5: loanType = "Education"; break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        double loanAmount = getAmount("Enter Loan Amount: ");
        if (loanAmount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }

        // Loan amount limits
        double maxAmount = 0;
        switch (loanType) {
            case "Personal": maxAmount = 500000; break;
            case "Home": maxAmount = 10000000; break;
            case "Business": maxAmount = 5000000; break;
            case "Auto": maxAmount = 3000000; break;
            case "Education": maxAmount = 2000000; break;
        }

        if (loanAmount > maxAmount) {
            System.out.println("Maximum loan amount for " + loanType + " is: " + maxAmount);
            return;
        }

        int tenureMonths = getTenure("Enter Loan Tenure (in months, 6-360): ");
        if (tenureMonths < 6 || tenureMonths > 360) {
            System.out.println("Tenure must be between 6 and 360 months!");
            return;
        }

        // Create loan object
        Loan loan = new Loan(customerId, loanType, loanAmount, tenureMonths);

        // Save to file
        FileService.appendToFile("loans.txt", loan.toString());

        System.out.println("\n✓ Loan application submitted successfully!");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Application will be reviewed by loan officer.");

        loan.displayDetails();
    }

    public void viewLoanStatus(String loanId) {
        String loanData = FileService.searchRecord("loans.txt", loanId);
        if (loanData != null) {
            Loan loan = Loan.fromString(loanData);
            loan.displayDetails();
        } else {
            System.out.println("Loan not found!");
        }
    }

    public void makePayment(String loanId, double amount) {
        String loanData = FileService.searchRecord("loans.txt", loanId);
        if (loanData == null) {
            System.out.println("Loan not found!");
            return;
        }

        Loan loan = Loan.fromString(loanData);
        if (!loan.getStatus().equals("Disbursed")) {
            System.out.println("Loan is not disbursed yet!");
            return;
        }

        if (amount <= 0) {
            System.out.println("Payment amount must be positive!");
            return;
        }

        // Check if payment is at least EMI amount
        if (amount < loan.getEmiAmount()) {
            System.out.println("Minimum payment amount is EMI: " + loan.getEmiAmount());
            System.out.print("Do you want to pay EMI amount? (yes/no): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                amount = loan.getEmiAmount();
            } else {
                return;
            }
        }

        // Get account for payment
        String accountNumber = getInput("Enter Account Number for payment: ");
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);

        if (accountData == null) {
            System.out.println("Account not found!");
            return;
        }

        Account account = Account.fromString(accountData);
        if (account.getBalance() < amount) {
            System.out.println("Insufficient balance in account!");
            return;
        }

        // Deduct from account
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        FileService.updateRecord("accounts.txt", accountData, account.toString());

        // Update loan
        double oldRemaining = loan.getRemainingAmount();
        loan.makePayment(amount);

        // Update loan record
        FileService.updateRecord("loans.txt", loanData, loan.toString());

        // Record transaction
        String transactionData = "LOANPAY|" + accountNumber + "|Loan Payment|" + amount +
                "|" + LocalDate.now() + "|Loan Payment for " + loanId +
                "|Completed|LNPAY" + loanId;
        FileService.appendToFile("transactions.txt", transactionData);

        System.out.println("\n✓ Loan payment successful!");
        System.out.println("Payment Amount: " + amount);
        System.out.println("Previous Loan Balance: " + oldRemaining);
        System.out.println("New Loan Balance: " + loan.getRemainingAmount());
        System.out.println("Account Balance: " + newBalance);

        if (loan.getStatus().equals("Closed")) {
            System.out.println("Congratulations! Loan fully paid.");
        }
    }

    public void viewLoanDetails(String loanId) {
        viewLoanStatus(loanId); // Reuse same method
    }

    public void generateLoanReport() {
        System.out.println("\n=== LOAN PORTFOLIO REPORT ===");

        List<String> loans = FileService.readFile("loans.txt");

        if (loans.isEmpty()) {
            System.out.println("No loans found.");
            return;
        }

        int totalLoans = 0;
        int approvedLoans = 0;
        int disbursedLoans = 0;
        int closedLoans = 0;
        double totalLoanAmount = 0;
        double totalRemaining = 0;

        System.out.println("\nLoan ID\t\tCustomer ID\tType\t\tAmount\t\tRemaining\tStatus");
        System.out.println("-----------------------------------------------------------------------------");

        for (String loanData : loans) {
            Loan loan = Loan.fromString(loanData);
            if (loan != null) {
                totalLoans++;
                totalLoanAmount += loan.getLoanAmount();
                totalRemaining += loan.getRemainingAmount();

                switch (loan.getStatus()) {
                    case "Approved": approvedLoans++; break;
                    case "Disbursed": disbursedLoans++; break;
                    case "Closed": closedLoans++; break;
                }

                System.out.printf("%s\t%s\t%s\t%.2f\t%.2f\t\t%s\n",
                        loan.getLoanId(),
                        loan.getCustomerId(),
                        loan.getLoanType(),
                        loan.getLoanAmount(),
                        loan.getRemainingAmount(),
                        loan.getStatus());
            }
        }

        System.out.println("\n=== REPORT SUMMARY ===");
        System.out.println("Total Loans: " + totalLoans);
        System.out.println("Approved Loans: " + approvedLoans);
        System.out.println("Disbursed Loans: " + disbursedLoans);
        System.out.println("Closed Loans: " + closedLoans);
        System.out.println("Total Loan Amount: PKR " + totalLoanAmount);
        System.out.println("Total Remaining Amount: PKR " + totalRemaining);
        System.out.println("Amount Recovered: PKR " + (totalLoanAmount - totalRemaining));
    }

    private double getAmount(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount! Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();
        return amount;
    }

    private int getTenure(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        int tenure = scanner.nextInt();
        scanner.nextLine();
        return tenure;
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}