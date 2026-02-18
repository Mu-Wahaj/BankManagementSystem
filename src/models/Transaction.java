package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private String transactionId;
    private String accountNumber;
    private String type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    private String status;
    private String referenceNumber;


    public Transaction(String accountNumber, String type, double amount, String description) {
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
        this.status = "Completed";
        this.referenceNumber = generateReference();
    }

    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private String generateReference() {
        return "REF" + timestamp.getYear() +
                String.format("%02d", timestamp.getMonthValue()) +
                String.format("%02d", timestamp.getDayOfMonth()) +
                String.format("%06d", (int)(Math.random() * 1000000));
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    @Override
    public String toString() {
        return transactionId + "|" + accountNumber + "|" + type + "|" +
                amount + "|" + timestamp + "|" + description + "|" +
                status + "|" + referenceNumber;
    }

    public static Transaction fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 8) return null;

        Transaction transaction = new Transaction(
                parts[1], parts[2], Double.parseDouble(parts[3]), parts[5]
        );
        transaction.setTransactionId(parts[0]);
        transaction.setTimestamp(LocalDateTime.parse(parts[4]));
        transaction.setStatus(parts[6]);
        transaction.setReferenceNumber(parts[7]);
        return transaction;
    }

    public void displayReceipt() {
        System.out.println("\n=== TRANSACTION RECEIPT ===");
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Account: " + accountNumber);
        System.out.println("Type: " + type);
        System.out.println("Amount: " + amount);
        System.out.println("Date & Time: " + timestamp);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);
        System.out.println("Reference: " + referenceNumber);
        System.out.println("===========================");
    }
}