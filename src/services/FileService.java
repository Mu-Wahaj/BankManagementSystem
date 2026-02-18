package services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {

    // Read data from file
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/" + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, return empty list
            System.out.println("Creating new file: " + filename);
        }
        return lines;
    }

    // Write data to file
    public static void writeFile(String filename, List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + filename))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filename);
            e.printStackTrace();
        }
    }

    // Append data to file
    public static void appendToFile(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + filename, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filename);
            e.printStackTrace();
        }
    }

    // Update specific record in file
    public static boolean updateRecord(String filename, String oldData, String newData) {
        List<String> lines = readFile(filename);
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(oldData)) {
                lines.set(i, newData);
                writeFile(filename, lines);
                return true;
            }
        }
        return false;
    }

    // Delete record from file
    public static boolean deleteRecord(String filename, String dataToDelete) {
        List<String> lines = readFile(filename);
        boolean removed = lines.remove(dataToDelete);
        if (removed) {
            writeFile(filename, lines);
        }
        return removed;
    }

    // Search for record in file
    public static String searchRecord(String filename, String searchTerm) {
        List<String> lines = readFile(filename);
        for (String line : lines) {
            if (line.contains(searchTerm)) {
                return line;
            }
        }
        return null;
    }

    // Get all records matching search term
    public static List<String> searchAllRecords(String filename, String searchTerm) {
        List<String> lines = readFile(filename);
        List<String> results = new ArrayList<>();
        for (String line : lines) {
            if (line.contains(searchTerm)) {
                results.add(line);
            }
        }
        return results;
    }

    // Count records in file
    public static int countRecords(String filename) {
        List<String> lines = readFile(filename);
        return lines.size();
    }
}