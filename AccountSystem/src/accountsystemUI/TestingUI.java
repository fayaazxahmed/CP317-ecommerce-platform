package accountsystemUI;

import accountsystem.Helpers;
import accountsystem.User;
import javax.swing.*;
import java.awt.Dimension;
import java.io.*;
import java.util.List;

public class TestingUI {

    /** Delete all users */
    public static List<User> deleteAll(List<User> users) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete ALL accounts?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            users.clear();
            Helpers.saveUsers(users);
            JOptionPane.showMessageDialog(null, "All accounts have been deleted!");
        } else {
            JOptionPane.showMessageDialog(null, "Operation cancelled.");
        }

        return users;
    }

    /** Show users in a dialog */
    public static void showFileContents() {
        File file = new File("users.csv"); // or users.txt if still using that
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File does not exist.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // skip the header
                    continue;
                }
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            return;
        }

        JTextArea userArea = new JTextArea(sb.toString());
        userArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "All Users", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Show cart items in a dialogue*/
    public static void showCartItems() {
        File file = new File("cartItems.csv"); // or users.txt if still using that
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File does not exist.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int total = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineContents = line.split(",");
                total += Integer.parseInt(lineContents[1]);
                sb.append(lineContents[0]).append(": $").append(lineContents[1]).append("\n");
            }
            sb.append("Total: $").append(String.valueOf(total)).append("\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
            return;
        }

        JTextArea userArea = new JTextArea(sb.toString());
        userArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userArea);
        scrollPane.setPreferredSize(new Dimension(100, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Items in Cart", JOptionPane.INFORMATION_MESSAGE);
    }
}
