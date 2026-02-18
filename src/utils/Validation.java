package utils;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class Validation {

    // Validate CNIC (13 digits)
    public static boolean validateCNIC(String cnic) {
        return cnic != null && cnic.matches("\\d{13}");
    }

    // Validate phone number (Pakistan format)
    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("03\\d{9}");
    }

    // Validate email
    public static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && Pattern.compile(emailRegex).matcher(email).matches();
    }

    // Validate age (must be 18+)
    public static boolean validateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears() >= 18;
    }

    // Validate amount (positive)
    public static boolean validateAmount(double amount) {
        return amount > 0;
    }

    // Validate account number format
    public static boolean validateAccountNumber(String accountNumber) {
        return accountNumber != null && accountNumber.matches("\\d{10}");
    }

    // Validate password strength
    public static boolean validatePassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Validate name (only letters and spaces)
    public static boolean validateName(String name) {
        return name != null && name.matches("[A-Za-z\\s]+");
    }
}