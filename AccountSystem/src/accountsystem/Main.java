package accountsystem;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> users = Helpers.loadUsers();

        final String BOLD = "\033[1m";
        final String RESET = "\033[0m";

        while (true) {
            System.out.println("=== Account Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Delete Account");
            System.out.println("4. " + BOLD + "Delete ALL Accounts (testing)" + RESET);
            System.out.println("5. " + BOLD + "Show All Users (testing)" + RESET);
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    users = AccountManager.createAccount(users, scanner);
                    break;
                case "2":
                    boolean loggedIn = AccountManager.login(users, scanner);
                    if (loggedIn) {
                        System.out.println("Here's your personalized dashboard (placeholder)\n");
                    }
                    break;
                case "3":
                    users = AccountManager.deleteAccount(users, scanner);
                    break;
                case "4":
                    users = TestingFunctions.deleteAll(users, scanner);
                    break;
                case "5":
                    TestingFunctions.showFileContents();
                    break;
                case "6":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.\n");
            }
        }
    }
}
