package services;

import models.Transaction;
import models.Account;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TransactionService {
    private static Scanner scanner = new Scanner(System.in);

    public void deposit() {
        System.out.println("\n=== DEPOSIT MONEY ===");

        String accountNumber = getInput("Enter Account Number: ");
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);

        if (accountData == null) {
            System.out.println("Account not found!");
            return;
        }

        Account account = Account.fromString(accountData);
        if (!account.getStatus().equals("Active")) {
            System.out.println("Account is not active!");
            return;
        }

        double amount = getAmount("Enter deposit amount: ");
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }

        // Update account balance
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);

        // Update account record
        FileService.updateRecord("accounts.txt", accountData, account.toString());

        // Create transaction record
        Transaction transaction = new Transaction(accountNumber, "Deposit", amount, "Cash Deposit");
        FileService.appendToFile("transactions.txt", transaction.toString());

        System.out.println("\n✓ Deposit successful!");
        transaction.displayReceipt();
        System.out.println("New Balance: PKR " + newBalance);
    }

    public void withdraw() {
        System.out.println("\n=== WITHDRAW MONEY ===");

        String accountNumber = getInput("Enter Account Number: ");
        String accountData = FileService.searchRecord("accounts.txt", accountNumber);

        if (accountData == null) {
            System.out.println("Account not found!");
            return;
        }

        Account account = Account.fromString(accountData);
        if (!account.getStatus().equals("Active")) {
            System.out.println("Account is not active!");
            return;
        }

        double amount = getAmount("Enter withdrawal amount: ");
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }

        // Check minimum balance
        if (account.getBalance() - amount < account.getMinimumBalance()) {
            System.out.println("Insufficient balance! Minimum balance required: " + account.getMinimumBalance());
            return;
        }

        // Check daily withdrawal limit
        double dailyWithdrawal = getDailyWithdrawal(accountNumber);
        if (dailyWithdrawal + amount > 50000) { // Daily limit: 50,000
            System.out.println("Daily withdrawal limit exceeded!");
            System.out.println("Today's withdrawal: " + dailyWithdrawal);
            System.out.println("Remaining limit: " + (50000 - dailyWithdrawal));
            return;
        }

        // Update account balance
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);

        // Update account record
        FileService.updateRecord("accounts.txt", accountData, account.toString());

        // Create transaction record
        Transaction transaction = new Transaction(accountNumber, "Withdrawal", amount, "Cash Withdrawal");
        FileService.appendToFile("transactions.txt", transaction.toString());

        System.out.println("\n✓ Withdrawal successful!");
        transaction.displayReceipt();
        System.out.println("New Balance: PKR " + newBalance);
    }

    public void transfer() {
        System.out.println("\n=== TRANSFER FUNDS ===");

        String fromAccount = getInput("Enter Your Account Number: ");
        String fromAccountData = FileService.searchRecord("accounts.txt", fromAccount);

        if (fromAccountData == null) {
            System.out.println("Your account not found!");
            return;
        }

        Account senderAccount = Account.fromString(fromAccountData);
        if (!senderAccount.getStatus().equals("Active")) {
            System.out.println("Your account is not active!");
            return;
        }

        String toAccount = getInput("Enter Recipient Account Number: ");
        if (fromAccount.equals(toAccount)) {
            System.out.println("Cannot transfer to same account!");
            return;
        }

        String toAccountData = FileService.searchRecord("accounts.txt", toAccount);
        if (toAccountData == null) {
            System.out.println("Recipient account not found!");
            return;
        }

        Account receiverAccount = Account.fromString(toAccountData);
        if (!receiverAccount.getStatus().equals("Active")) {
            System.out.println("Recipient account is not active!");
            return;
        }

        double amount = getAmount("Enter transfer amount: ");
        if (amount <= 0) {
            System.out.println("Amount must be positive!");
            return;
        }

        // Check sender's balance
        if (senderAccount.getBalance() - amount < senderAccount.getMinimumBalance()) {
            System.out.println("Insufficient balance!");
            return;
        }

        // Check transfer limit
        if (amount > 200000) { // Transfer limit: 200,000
            System.out.println("Transfer limit exceeded! Maximum: 200,000");
            return;
        }

        // Update sender's balance
        double senderNewBalance = senderAccount.getBalance() - amount;
        senderAccount.setBalance(senderNewBalance);
        FileService.updateRecord("accounts.txt", fromAccountData, senderAccount.toString());

        // Update receiver's balance
        double receiverNewBalance = receiverAccount.getBalance() + amount;
        receiverAccount.setBalance(receiverNewBalance);
        FileService.updateRecord("accounts.txt", toAccountData, receiverAccount.toString());

        // Create transaction records
        Transaction withdrawal = new Transaction(fromAccount, "Transfer", amount, "Transfer to " + toAccount);
        Transaction deposit = new Transaction(toAccount, "Transfer", amount, "Transfer from " + fromAccount);

        FileService.appendToFile("transactions.txt", withdrawal.toString());
        FileService.appendToFile("transactions.txt", deposit.toString());

        System.out.println("\n✓ Transfer successful!");
        withdrawal.displayReceipt();
        System.out.println("Your New Balance: PKR " + senderNewBalance);
    }

    public void viewTransactionHistory(String accountNumber) {
        System.out.println("\n=== TRANSACTION HISTORY ===");

        List<String> transactions = FileService.searchAllRecords("transactions.txt", accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for account: " + accountNumber);
            return;
        }

        System.out.println("\nDate\t\tType\t\tAmount\t\tDescription\t\tStatus");
        System.out.println("----------------------------------------------------------------------------");

        for (String transaction : transactions) {
            String[] parts = transaction.split("\\|");
            if (parts.length >= 7) {
                System.out.printf("%s\t%s\t%.2f\t\t%s\t\t%s\n",
                        parts[4], parts[2], Double.parseDouble(parts[3]), parts[5], parts[6]);
            }
        }

        System.out.println("\nTotal Transactions: " + transactions.size());
    }

    public void generateDailyReport() {
        System.out.println("\n=== DAILY TRANSACTION REPORT ===");
        System.out.println("Date: " + LocalDate.now());

        List<String> allTransactions = FileService.readFile("transactions.txt");
        List<String> todayTransactions = FileService.searchAllRecords("transactions.txt", LocalDate.now().toString());

        if (todayTransactions.isEmpty()) {
            System.out.println("No transactions today.");
            return;
        }

        double totalDeposits = 0;
        double totalWithdrawals = 0;
        double totalTransfers = 0;

        System.out.println("\nTransaction ID\t\tAccount\t\tType\t\tAmount\t\tTime");
        System.out.println("------------------------------------------------------------------------");

        for (String transaction : todayTransactions) {
            String[] parts = transaction.split("\\|");
            if (parts.length >= 5) {
                String type = parts[2];
                double amount = Double.parseDouble(parts[3]);

                switch (type) {
                    case "Deposit":
                        totalDeposits += amount;
                        break;
                    case "Withdrawal":
                        totalWithdrawals += amount;
                        break;
                    case "Transfer":
                        totalTransfers += amount;
                        break;
                }

                System.out.printf("%s\t%s\t%s\t%.2f\t\t%s\n",
                        parts[0], parts[1], type, amount, parts[4]);
            }
        }

        System.out.println("\n=== DAILY SUMMARY ===");
        System.out.println("Total Transactions: " + todayTransactions.size());
        System.out.println("Total Deposits: PKR " + totalDeposits);
        System.out.println("Total Withdrawals: PKR " + totalWithdrawals);
        System.out.println("Total Transfers: PKR " + totalTransfers);
        System.out.println("Net Change: PKR " + (totalDeposits - totalWithdrawals));
    }

    private double getDailyWithdrawal(String accountNumber) {
        List<String> todayTransactions = FileService.searchAllRecords("transactions.txt", LocalDate.now().toString());
        double total = 0;

        for (String transaction : todayTransactions) {
            String[] parts = transaction.split("\\|");
            if (parts.length >= 5 && parts[1].equals(accountNumber) && parts[2].equals("Withdrawal")) {
                total += Double.parseDouble(parts[3]);
            }
        }
        return total;
    }

    private double getAmount(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount! Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear buffer
        return amount;
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}