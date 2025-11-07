package accountsystem;

import java.util.*;

public class AccountManager {

    public static List<User> createAccount(List<User> users, Scanner scanner) {
        System.out.println("\nCreate Account");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine().trim();

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("That username is already taken.\n");
                return users;
            }
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Enter a email: ");
        String email = scanner.nextLine().trim();

        users.add(new User(username, password, email));
        Helpers.saveUsers(users);
        System.out.println("Account created for " + username + "!\n");
        return users;
    }

    public static boolean login(List<User> users, Scanner scanner) {
        System.out.println("\nLogin");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Welcome back, " + username + "!\n");
                return true;
            }
        }

        System.out.println("Invalid username or password.\n");
        return false;
    }

    public static List<User> deleteAccount(List<User> users, Scanner scanner) {
        System.out.println("\nDelete Account");
        System.out.print("Enter the username to delete: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter the password for verification: ");
        String password = scanner.nextLine().trim();

        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                iterator.remove();
                Helpers.saveUsers(users);
                System.out.println("Account '" + username + "' has been deleted.\n");
                return users;
            }
        }

        System.out.println("Username/password not found. Cannot delete account.\n");
        return users;
    }
}
