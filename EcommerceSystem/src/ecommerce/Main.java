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
        
        // Bottom Panel: Testing buttons

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton showBtn = new JButton("Show Users");
        showBtn.setPreferredSize(new Dimension(120, 30));
        showBtn.addActionListener(e -> TestingUI.showFileContents());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.add(showBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        loginFrame.add(bottomPanel, BorderLayout.SOUTH);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

   
    // ---------- CREATE ACCOUNT ----------
    private void handleCreateAccount() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(15);
        panel.add(emailField);
        
        panel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(15);
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(loginFrame, panel,
                "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String email = emailField.getText().trim();
            String address = addressField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "All fields are required.");
                return;
            }

            // Create account & update list
            users = AccountManagerUI.createAccount(users, username, password, email, address);

            // Only proceed to dashboard if account creation succeeded
            boolean success = AccountManagerUI.login(users, username, password);
            if (success) {
                boolean isSeller = true;
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
                boolean isSeller = true;
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

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120, 30));
        logoutBtn.addActionListener(e -> {
            dashFrame.dispose();
            createLoginScreen();
        });


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(logoutBtn);

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
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> SearchUI.openSearch(storage, isSeller, username, users));
        categoryPanel.add(searchBtn);

        
        ecommercePanel.add(categoryPanel, BorderLayout.CENTER);

        dashFrame.add(ecommercePanel, BorderLayout.CENTER);

        dashFrame.setLocationRelativeTo(null);
        dashFrame.setVisible(true);
        
        // NEW BUTTONS:
        JButton cartBtn = new JButton("Cart");
        cartBtn.setPreferredSize(new Dimension(120, 30));
        cartBtn.addActionListener(e -> TestingUI.showCartItems());
        rightPanel.add(cartBtn);

        
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
