package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Customer implements Serializable {
    private String customerId;
    private String name;
    private String cnic;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private String accountType;
    private LocalDate registrationDate;
    private String status;

    public Customer(String customerId, String name, String cnic, LocalDate dateOfBirth,
                    String address, String phone, String email, String accountType) {
        this.customerId = customerId;
        this.name = name;
        this.cnic = cnic;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.accountType = accountType;
        this.registrationDate = LocalDate.now();
        this.status = "Active";
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCnic() { return cnic; }
    public void setCnic(String cnic) { this.cnic = cnic; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return customerId + "|" + name + "|" + cnic + "|" + dateOfBirth + "|" +
                address + "|" + phone + "|" + email + "|" + accountType + "|" +
                registrationDate + "|" + status;
    }

    public static Customer fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 10) return null;

        Customer customer = new Customer(
                parts[0], parts[1], parts[2],
                LocalDate.parse(parts[3]), parts[4], parts[5], parts[6], parts[7]
        );
        customer.setRegistrationDate(LocalDate.parse(parts[8]));
        customer.setStatus(parts[9]);
        return customer;
    }

    public void displayDetails() {
        System.out.println("\n=== CUSTOMER DETAILS ===");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("CNIC: " + cnic);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Account Type: " + accountType);
        System.out.println("Registration Date: " + registrationDate);
        System.out.println("Status: " + status);
    }
}