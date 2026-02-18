package utils;

public class Constants {

    // Bank Information
    public static final String BANK_NAME = "National Bank";
    public static final String BANK_ADDRESS = "123 Main Street, Lahore";
    public static final String BANK_PHONE = "042-111-222-333";

    // Account Types
    public static final String ACCOUNT_SAVINGS = "Savings";
    public static final String ACCOUNT_CURRENT = "Current";
    public static final String ACCOUNT_FIXED_DEPOSIT = "Fixed Deposit";

    // Loan Types
    public static final String LOAN_PERSONAL = "Personal";
    public static final String LOAN_HOME = "Home";
    public static final String LOAN_BUSINESS = "Business";
    public static final String LOAN_AUTO = "Auto";
    public static final String LOAN_EDUCATION = "Education";

    // Transaction Types
    public static final String TRANSACTION_DEPOSIT = "Deposit";
    public static final String TRANSACTION_WITHDRAWAL = "Withdrawal";
    public static final String TRANSACTION_TRANSFER = "Transfer";

    // Status Values
    public static final String STATUS_ACTIVE = "Active";
    public static final String STATUS_INACTIVE = "Inactive";
    public static final String STATUS_CLOSED = "Closed";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_REJECTED = "Rejected";

    // Limits
    public static final double DAILY_WITHDRAWAL_LIMIT = 50000.0;
    public static final double TRANSACTION_LIMIT = 200000.0;
    public static final double MIN_SAVINGS_BALANCE = 1000.0;
    public static final double MIN_CURRENT_BALANCE = 5000.0;
    public static final double MIN_FD_BALANCE = 10000.0;

    // Interest Rates
    public static final double SAVINGS_INTEREST = 5.0;
    public static final double CURRENT_INTEREST = 1.0;
    public static final double FD_INTEREST = 8.0;

    // File Paths
    public static final String CUSTOMERS_FILE = "data/customers.txt";
    public static final String ACCOUNTS_FILE = "data/accounts.txt";
    public static final String TRANSACTIONS_FILE = "data/transactions.txt";
    public static final String LOANS_FILE = "data/loans.txt";
    public static final String EMPLOYEES_FILE = "data/employees.txt";
    public static final String ADMINS_FILE = "data/admins.txt";

    // Bank Logo/ASCII Art
    public static final String BANK_LOGO =
            "╔══════════════════════════════════════════════════════════╗\n" +
                    "║                 NATIONAL BANK OF PAKISTAN               ║\n" +
                    "║            Bank Management System v2.0                  ║\n" +
                    "╚══════════════════════════════════════════════════════════╝\n";
}