package util;

import java.io.*;
import java.util.*;
import domain.User;

public class Helpers {

    // ------------------- LOAD USERS -------------------
    public static List<User> loadUsers(String[] categories) {
        List<User> users = new ArrayList<>();
        File file = new File("users.csv");
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header
            if (line == null) return users;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5); // username,password,email,address,prefs
                if (parts.length < 4) continue;

                User u = new User(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());

                // load preferences if present
                if (parts.length == 5) {
                    u.setPreferencesFromIndexes(parts[4].trim(), categories);
                }

                users.add(u);
            }

        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }

        return users;
    }

    // ------------------- SAVE USERS -------------------
    public static void saveUsers(List<User> users, String[] categories) {
        File file = new File("users.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("Username,Password,Email,Address,Preferences");
            writer.newLine();

            for (User user : users) {
                String line = String.join(",",
                        user.getUsername(),
                        user.getPassword(),
                        user.getEmail(),
                        user.getAddress(),
                        user.getPreferencesAsIndexes(categories)
                );
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}
