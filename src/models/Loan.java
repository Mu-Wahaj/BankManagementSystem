package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable {
    private String loanId;
    private String customerId;
    private String loanType;
    private double loanAmount;
    private double interestRate;
    private int tenureMonths;
    private LocalDate applicationDate;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private double remainingAmount;
    private String status;
    private double emiAmount;


    public Loan(String customerId, String loanType, double loanAmount, int tenureMonths) {
        this.loanId = generateLoanId();
        this.customerId = customerId;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.tenureMonths = tenureMonths;
        this.applicationDate = LocalDate.now();
        setInterestRate();
        calculateEMI();
        this.remainingAmount = loanAmount;
        this.status = "Applied";
    }

    private String generateLoanId() {
        return "LOAN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private void setInterestRate() {
        switch (loanType) {
            case "Personal":
                interestRate = 15.0;
                break;
            case "Home":
                interestRate = 10.0;
                break;
            case "Business":
                interestRate = 12.0;
                break;
            case "Auto":
                interestRate = 13.0;
                break;
            case "Education":
                interestRate = 8.0;
                break;
            default:
                interestRate = 12.0;
        }
    }

    public void calculateEMI() {
        double monthlyRate = interestRate / 12 / 100;
        emiAmount = (loanAmount * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) /
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);
    }

    public String getLoanId() { return loanId; }
    public void setLoanId(String loanId) { this.loanId = loanId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
        setInterestRate();
        calculateEMI();
    }

    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
        calculateEMI();
    }

    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
        calculateEMI();
    }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
        calculateEMI();
    }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }

    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }

    public double getRemainingAmount() { return remainingAmount; }
    public void setRemainingAmount(double remainingAmount) { this.remainingAmount = remainingAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getEmiAmount() { return emiAmount; }

    public void approveLoan() {
        this.status = "Approved";
        this.approvalDate = LocalDate.now();
    }

    public void disburseLoan() {
        this.status = "Disbursed";
        this.disbursementDate = LocalDate.now();
    }

    public boolean makePayment(double amount) {
        if (amount <= 0 || amount > remainingAmount) {
            return false;
        }
        remainingAmount -= amount;
        if (remainingAmount <= 0) {
            status = "Closed";
        }
        return true;
    }

    @Override
    public String toString() {
        return loanId + "|" + customerId + "|" + loanType + "|" + loanAmount + "|" +
                interestRate + "|" + tenureMonths + "|" + applicationDate + "|" +
                approvalDate + "|" + disbursementDate + "|" + remainingAmount + "|" +
                status + "|" + emiAmount;
    }

    public static Loan fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 12) return null;

        Loan loan = new Loan(
                parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[5])
        );
        loan.setLoanId(parts[0]);
        loan.setInterestRate(Double.parseDouble(parts[4]));
        loan.setApplicationDate(LocalDate.parse(parts[6]));
        loan.setApprovalDate(parts[7].equals("null") ? null : LocalDate.parse(parts[7]));
        loan.setDisbursementDate(parts[8].equals("null") ? null : LocalDate.parse(parts[8]));
        loan.setRemainingAmount(Double.parseDouble(parts[9]));
        loan.setStatus(parts[10]);
        return loan;
    }

    public void displayDetails() {
        System.out.println("\n=== LOAN DETAILS ===");
        System.out.println("Loan ID: " + loanId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Loan Type: " + loanType);
        System.out.println("Loan Amount: " + loanAmount);
        System.out.println("Interest Rate: " + interestRate + "%");
        System.out.println("Tenure: " + tenureMonths + " months");
        System.out.println("EMI Amount: " + String.format("%.2f", emiAmount));
        System.out.println("Application Date: " + applicationDate);
        System.out.println("Approval Date: " + (approvalDate != null ? approvalDate : "Pending"));
        System.out.println("Disbursement Date: " + (disbursementDate != null ? disbursementDate : "Not Disbursed"));
        System.out.println("Remaining Amount: " + remainingAmount);
        System.out.println("Status: " + status);
    }
}