package accountsystem;

import java.io.*;
import java.util.*;

public class TestingFunctions {

    private static final String FILE_NAME = "users.csv";

    /** Delete all users after confirmation */
    public static List<User> deleteAll(List<User> users, Scanner scanner) {
        System.out.print("Are you sure you want to delete ALL accounts? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            users.clear();
            saveUsers(users);
            System.out.println("All accounts have been deleted!\n");
        } else {
            System.out.println("Operation cancelled.\n");
        }
        return users;
    }

    /** Show users from CSV, skip header */
    public static void showFileContents() {
        File file = new File(FILE_NAME);
        System.out.println("\nUsers in File:");

        if (!file.exists()) {
            System.out.println("(File does not exist)");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean empty = true;

            // Read header
            line = reader.readLine();
            if (line == null) {
                System.out.println("(File is empty)");
                return;
            }

            // Read rest of file
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    String email = parts[2].trim();
                    System.out.println("Username: " + username + " | Password: " + password + " | Email: " + email);
                    empty = false;
                }
            }

            if (empty) {
                System.out.println("(No users in file)");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /** Save list of users to CSV, including header */
    public static void saveUsers(List<User> users) {
        File file = new File(FILE_NAME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("Username,Password,Email");
            writer.newLine();

            // Write user data
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    /** Load users from CSV, skip header */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }

        return users;
    }
}
