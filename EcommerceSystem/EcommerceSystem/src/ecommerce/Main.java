package ecommerce;

import accountsystemUI.*;

import javax.swing.*;

import EcommerceLauncher.*;

import java.awt.*;
import java.util.List;

public class Main {

    private List<User> users;
    private JFrame loginFrame;

    public Main() {
        users = Helpers.loadUsers();
        createLoginScreen();
    }

    // ---------- LOGIN / CREATE SCREEN ----------
    public void createLoginScreen() {
        if (loginFrame != null) loginFrame.dispose(); // close old frame if exists

        loginFrame = new JFrame("Account System");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(600, 400);
        loginFrame.setLayout(new BorderLayout());

        // Main buttons panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JButton createBtn = new JButton("Create Account");
        createBtn.setPreferredSize(new Dimension(180, 50));
        createBtn.addActionListener(e -> handleCreateAccount());

        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(180, 50));
        loginBtn.addActionListener(e -> handleLogin());

        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(createBtn, gbc);
        gbc.gridx = 1; mainPanel.add(loginBtn, gbc);

        loginFrame.add(mainPanel, BorderLayout.CENTER);

        // Bottom panel: Back button & testing
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(100, 30));
        backBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(loginFrame,
                    "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(backBtn);

        JButton showBtn = new JButton("Show Users");
        showBtn.setPreferredSize(new Dimension(120, 30));
        showBtn.addActionListener(e -> TestingUI.showFileContents());

        JButton deleteAllBtn = new JButton("Delete All");
        deleteAllBtn.setPreferredSize(new Dimension(120, 30));
        deleteAllBtn.addActionListener(e -> users = TestingUI.deleteAll(users));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.add(showBtn);
        rightPanel.add(deleteAllBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        loginFrame.add(bottomPanel, BorderLayout.SOUTH);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

   
    // ---------- CREATE ACCOUNT ----------
    private void handleCreateAccount() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(15);
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(loginFrame, panel,
                "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "All fields are required.");
                return;
            }

            // Create account & update list
            users = AccountManagerUI.createAccount(users, username, password, email);

            // Only proceed to dashboard if account creation succeeded
            boolean success = AccountManagerUI.login(users, username, password);
            if (success) {
                // Ask user which role to enter as (Buyer or Seller)
                String[] options = {"Buyer", "Seller"};
                int choice = JOptionPane.showOptionDialog(loginFrame, "Enter as:", "Select Role",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                boolean isSeller = (choice == 1); // 0 = Buyer, 1 = Seller

                // Open dashboard with selected role
                openDashboard(username, isSeller);
            }
        }
    }


    // ---------- LOGIN ----------
    private void handleLogin() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(loginFrame, panel,
                "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            boolean success = AccountManagerUI.login(users, username, password);
            if (success) {
                // Ask user which role to enter as
                String[] options = {"Buyer", "Seller"};
                int choice = JOptionPane.showOptionDialog(loginFrame, "Enter as:", "Select Role",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                boolean isSeller = (choice == 1); // 0 = Buyer, 1 = Seller
                openDashboard(username, isSeller); // pass role explicitly
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.");
            }

        }
    }

    // ---------- DASHBOARD ----------
    private void openDashboard(String username, boolean isSeller) {
        // Close login frame
        if (loginFrame != null) loginFrame.dispose();

        // Initialize the product system
        String[] categories = { "Clothing", "Furniture", "Electronics" };
        ProductStorage storage = new ProductStorage(categories);
        storage.loadAll();


        // Create the main dashboard frame
        JFrame dashFrame = new JFrame("Dashboard - " + username);
        dashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashFrame.setSize(900, 700);
        dashFrame.setLayout(new BorderLayout());

        // ---------- Top Panel: Back / Delete / Logout / Profile ----------
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 30));
        backBtn.addActionListener(e -> {
            dashFrame.dispose();
            createLoginScreen();
        });

        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setPreferredSize(new Dimension(120, 30));
        deleteBtn.addActionListener(e -> {
            String password = JOptionPane.showInputDialog(dashFrame, "Enter your password:");
            if (password != null) {
                users = AccountManagerUI.deleteAccount(users, username, password);
                dashFrame.dispose();
                createLoginScreen();
            }
        });

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120, 30));
        logoutBtn.addActionListener(e -> {
            dashFrame.dispose();
            createLoginScreen();
        });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(backBtn);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton profileBtn = new JButton("Profile");
        profileBtn.setPreferredSize(new Dimension(120, 30));
        profileBtn.addActionListener(e -> ProfileUI.openProfile(dashFrame, username, users));

        rightPanel.add(profileBtn);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        dashFrame.add(topPanel, BorderLayout.NORTH);

        // ---------- Center Panel: E-Commerce UI ----------
        JPanel ecommercePanel = new JPanel(new BorderLayout());

        // Title
        JLabel title = new JLabel(isSeller ? "Seller Homepage" : "Buyer Homepage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        ecommercePanel.add(title, BorderLayout.NORTH);

        // Categories panel
        JPanel categoryPanel = new JPanel(new FlowLayout());
        for (int i = 0; i < categories.length; i++) {
            int index = i;
            JButton btn = new JButton(categories[i]);
            btn.addActionListener(e -> CategoryUI.open(categories[index], storage.getCategory(index), index, storage,
                    isSeller, () -> {})); // backAction left empty; handled by existing back button
            categoryPanel.add(btn);
        }
        
        JButton searchBtn = new JButton("Search All Products");
        searchBtn.addActionListener(e -> SearchUI.openSearch(storage, isSeller));
        categoryPanel.add(searchBtn);
        
        ecommercePanel.add(categoryPanel, BorderLayout.CENTER);

        dashFrame.add(ecommercePanel, BorderLayout.CENTER);

        // ---------- Bottom Panel: Testing buttons ----------
        JPanel testingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton showBtn = new JButton("Show Users");
        showBtn.setPreferredSize(new Dimension(120, 30));
        showBtn.addActionListener(e -> TestingUI.showFileContents());

        JButton deleteAllBtn = new JButton("Delete All");
        deleteAllBtn.setPreferredSize(new Dimension(120, 30));
        deleteAllBtn.addActionListener(e -> users = TestingUI.deleteAll(users));

        testingPanel.add(showBtn);
        testingPanel.add(deleteAllBtn);
        dashFrame.add(testingPanel, BorderLayout.SOUTH);

        dashFrame.setLocationRelativeTo(null);
        dashFrame.setVisible(true);
        
        // NEW BUTTONS:
        JButton cartBtn = new JButton("Cart");
        cartBtn.setPreferredSize(new Dimension(120, 30));
        cartBtn.addActionListener(e -> TestingUI.showCartItems());
        rightPanel.add(cartBtn);

        // RELOCATED BUTTONS:
        testingPanel.add(deleteBtn);
        testingPanel.add(logoutBtn);
        
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
