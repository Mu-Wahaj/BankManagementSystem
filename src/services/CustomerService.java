package services;

import models.Customer;
import utils.Validation;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CustomerService {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void registerCustomer() {
        System.out.println("\n=== REGISTER NEW CUSTOMER ===");

        // Generate customer ID
        String customerId = generateCustomerId();

        // Get customer details
        String name = getInput("Enter Full Name: ");

        String cnic;
        while (true) {
            cnic = getInput("Enter CNIC (13 digits): ");
            if (Validation.validateCNIC(cnic)) {
                break;
            } else {
                System.out.println("Invalid CNIC! Must be 13 digits.");
            }
        }

        LocalDate dob;
        while (true) {
            String dobStr = getInput("Enter Date of Birth (YYYY-MM-DD): ");
            try {
                dob = LocalDate.parse(dobStr);
                if (Validation.validateAge(dob)) {
                    break;
                } else {
                    System.out.println("Customer must be at least 18 years old!");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Use YYYY-MM-DD.");
            }
        }

        String address = getInput("Enter Address: ");

        String phone;
        while (true) {
            phone = getInput("Enter Phone Number (03XXXXXXXXX): ");
            if (Validation.validatePhone(phone)) {
                break;
            } else {
                System.out.println("Invalid phone number! Must start with 03 and be 11 digits.");
            }
        }

        String email = getInput("Enter Email: ");

        // Select account type
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Enter choice (1-2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        String accountType = (choice == 1) ? "Savings" : "Current";

        // Create customer object
        Customer customer = new Customer(customerId, name, cnic, dob, address, phone, email, accountType);

        // Save to file
        FileService.appendToFile("customers.txt", customer.toString());

        System.out.println("\n✓ Customer registered successfully!");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Please note this ID for future reference.");

        customer.displayDetails();
    }

    public void viewCustomerDetails(String customerId) {
        String customerData = FileService.searchRecord("customers.txt", customerId);
        if (customerData != null) {
            Customer customer = Customer.fromString(customerData);
            customer.displayDetails();

            // Show customer's accounts
            List<String> customerAccounts = FileService.searchAllRecords("accounts.txt", customerId);
            if (!customerAccounts.isEmpty()) {
                System.out.println("\n=== CUSTOMER ACCOUNTS ===");
                for (String accountData : customerAccounts) {
                    String[] parts = accountData.split("\\|");
                    System.out.println("Account: " + parts[0] + " | Type: " + parts[2] +
                            " | Balance: " + parts[3] + " | Status: " + parts[7]);
                }
            }

            // Show customer's loans
            List<String> customerLoans = FileService.searchAllRecords("loans.txt", customerId);
            if (!customerLoans.isEmpty()) {
                System.out.println("\n=== CUSTOMER LOANS ===");
                for (String loanData : customerLoans) {
                    String[] parts = loanData.split("\\|");
                    System.out.println("Loan ID: " + parts[0] + " | Type: " + parts[2] +
                            " | Amount: " + parts[3] + " | Status: " + parts[10]);
                }
            }
        } else {
            System.out.println("Customer not found!");
        }
    }

    public void updateCustomer(String customerId) {
        String customerData = FileService.searchRecord("customers.txt", customerId);
        if (customerData == null) {
            System.out.println("Customer not found!");
            return;
        }

        Customer customer = Customer.fromString(customerData);
        System.out.println("\n=== UPDATE CUSTOMER INFORMATION ===");
        customer.displayDetails();

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Address");
        System.out.println("2. Phone Number");
        System.out.println("3. Email");
        System.out.println("4. Cancel");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        boolean updated = false;

        switch (choice) {
            case 1:
                String newAddress = getInput("Enter new address: ");
                customer.setAddress(newAddress);
                updated = true;
                break;
            case 2:
                String newPhone;
                while (true) {
                    newPhone = getInput("Enter new phone number: ");
                    if (Validation.validatePhone(newPhone)) {
                        customer.setPhone(newPhone);
                        updated = true;
                        break;
                    } else {
                        System.out.println("Invalid phone number!");
                    }
                }
                break;
            case 3:
                String newEmail = getInput("Enter new email: ");
                customer.setEmail(newEmail);
                updated = true;
                break;
            case 4:
                System.out.println("Update cancelled.");
                return;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        if (updated) {
            FileService.updateRecord("customers.txt", customerData, customer.toString());
            System.out.println("✓ Customer information updated successfully!");
            customer.displayDetails();
        }
    }

    public void generateCustomerReport() {
        System.out.println("\n=== CUSTOMER REPORT ===");

        List<String> customers = FileService.readFile("customers.txt");

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        int totalCustomers = 0;
        int activeCustomers = 0;

        System.out.println("\nCustomer ID\tName\t\tCNIC\t\tPhone\t\tRegistration Date");
        System.out.println("----------------------------------------------------------------");

        for (String customerData : customers) {
            Customer customer = Customer.fromString(customerData);
            if (customer != null) {
                totalCustomers++;
                if (customer.getStatus().equals("Active")) {
                    activeCustomers++;
                }

                System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getCnic(),
                        customer.getPhone(),
                        customer.getRegistrationDate());
            }
        }

        System.out.println("\n=== REPORT SUMMARY ===");
        System.out.println("Total Customers: " + totalCustomers);
        System.out.println("Active Customers: " + activeCustomers);
        System.out.println("Inactive Customers: " + (totalCustomers - activeCustomers));
    }

    private String generateCustomerId() {
        int sequence = FileService.countRecords("customers.txt") + 1;
        return "CUST" + String.format("%06d", sequence);
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}