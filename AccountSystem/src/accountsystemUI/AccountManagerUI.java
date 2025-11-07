package accountsystemUI;

import accountsystem.Helpers;
import accountsystem.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AccountManagerUI {

    /*** CREATE ACCOUNT ***/
    public static List<User> createAccount(List<User> users, String username, String password, String email) {
        if (users == null) users = new ArrayList<>();
        username = username.trim();
        password = password.trim();
        email = email.trim();

        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(null, "That username is already taken.");
                return users; // Return unchanged list
            }
        }

        // Add new user
        users.add(new User(username, password, email));
        Helpers.saveUsers(users);
        JOptionPane.showMessageDialog(null, "Account created for " + username + "!");
        return users; // Return updated list
    }

    /*** LOGIN ***/
    public static boolean login(List<User> users, String username, String password) {
        if (users == null) return false;
        username = username.trim();
        password = password.trim();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Do not show dialog here; let UI handle it to avoid double messages
                return true;
            }
        }
        return false; // failed login
    }

    /*** DELETE ACCOUNT ***/
    public static List<User> deleteAccount(List<User> users, String username, String password) {
        if (users == null) return new ArrayList<>();
        username = username.trim();
        password = password.trim();

        // Iterate to safely remove user
        User found = null;
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                found = user;
                break;
            }
        }

        if (found != null) {
            users.remove(found);
            Helpers.saveUsers(users);
            JOptionPane.showMessageDialog(null, "Account '" + username + "' has been deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Username/password not found. Cannot delete account.");
        }

        return users;
    }
}
