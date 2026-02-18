package models;

import java.io.Serializable;

public class Employee implements Serializable {
    private String employeeId;
    private String name;
    private String designation;
    private String department;
    private double salary;
    private String role;
    private String password;
    private String status;
    public Employee(String employeeId, String name, String designation,
                    String department, double salary, String role, String password) {
        this.employeeId = employeeId;
        this.name = name;
        this.designation = designation;
        this.department = department;
        this.salary = salary;
        this.role = role;
        this.password = password;
        this.status = "Active";
    }


    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Business Methods
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public double getTransactionLimit() {
        switch (role) {
            case "Teller":
                return 50000.0;
            case "Manager":
                return 500000.0;
            case "Loan Officer":
                return 100000.0;
            case "Customer Service":
                return 10000.0;
            default:
                return 0.0;
        }
    }

    @Override
    public String toString() {
        return employeeId + "|" + name + "|" + designation + "|" + department + "|" +
                salary + "|" + role + "|" + password + "|" + status;
    }

    public static Employee fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 8) return null;

        Employee employee = new Employee(
                parts[0], parts[1], parts[2], parts[3],
                Double.parseDouble(parts[4]), parts[5], parts[6]
        );
        employee.setStatus(parts[7]);
        return employee;
    }
}