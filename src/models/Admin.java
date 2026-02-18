package models;

import java.io.Serializable;

public class Admin implements Serializable {
    private String adminId;
    private String username;
    private String password;
    private String accessLevel;
    private String email;
    private String status;

    public Admin(String adminId, String username, String password,
                 String accessLevel, String email) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.email = email;
        this.status = "Active";
    }


    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAccessLevel() { return accessLevel; }
    public void setAccessLevel(String accessLevel) { this.accessLevel = accessLevel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return adminId + "|" + username + "|" + password + "|" +
                accessLevel + "|" + email + "|" + status;
    }

    public static Admin fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length < 6) return null;

        Admin admin = new Admin(
                parts[0], parts[1], parts[2], parts[3], parts[4]
        );
        admin.setStatus(parts[5]);
        return admin;
    }
}