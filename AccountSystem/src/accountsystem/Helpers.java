package accountsystem;

import java.io.*;
import java.util.*;

public class Helpers {

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File("users.csv");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine(); // read first line
                if (line == null) return users; // empty file
                // Optionally check header
                if (!line.toLowerCase().startsWith("username")) {
                    // If the first line isn’t a header, treat it as a user
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }

                // Now read the rest of the file
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length == 3) {
                        users.add(new User(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading users file: " + e.getMessage());
            }
        }

        return users;
    }


    public static void saveUsers(List<User> users) {
        File file = new File("users.csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header
            writer.write("Username,Password,Email");
            writer.newLine();

            // Write users
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

}
