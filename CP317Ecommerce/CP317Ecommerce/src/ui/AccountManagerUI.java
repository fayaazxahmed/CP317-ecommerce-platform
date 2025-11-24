package ui;

import domain.User;
import util.Helpers;

import javax.swing.*;
import java.util.*;

public class AccountManagerUI {

    /*** CREATE ACCOUNT ***/
    public static List<User> createAccount(List<User> users, String username, String password, String email, String address, String[] categories) {
        if (users == null) users = new ArrayList<>();
        username = username.trim();
        password = password.trim();
        email = email.trim();
        address = address.trim();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(null, "That username is already taken.");
                return users;
            }
        }

        users.add(new User(username, password, email, address));
        Helpers.saveUsers(users, categories);
        JOptionPane.showMessageDialog(null, "Account created for " + username + "!");
        return users;
    }

    /*** LOGIN ***/
    public static boolean login(List<User> users, String username, String password) {
        if (users == null) return false;
        username = username.trim();
        password = password.trim();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /*** DELETE ACCOUNT ***/
    public static List<User> deleteAccount(List<User> users, String username, String password, String[] categories) {
        if (users == null) return new ArrayList<>();
        username = username.trim();
        password = password.trim();

        User found = null;
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                found = user;
                break;
            }
        }

        if (found != null) {
            users.remove(found);
            Helpers.saveUsers(users, categories);
            JOptionPane.showMessageDialog(null, "Account '" + username + "' has been deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Username/password not found. Cannot delete account.");
        }

        return users;
    }
}
