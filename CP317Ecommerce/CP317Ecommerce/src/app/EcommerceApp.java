package app;

import ui.*;
import data.*;
import domain.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;



public class EcommerceApp {

    private List<User> users;
    private JFrame loginFrame;

    public EcommerceApp() {
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

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

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
                openDashboard(username);
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
                openDashboard(username); // pass role explicitly
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.");
            }

        }
    }

    // ---------- DASHBOARD ----------
    private void openDashboard(String username) {
        // Close login frame
        if (loginFrame != null) loginFrame.dispose();

        // Initialize the product system
        String[] categories = { "Clothing", "Furniture", "Electronics" };
        ProductRepository storage = new ProductRepository(categories);
        storage.loadAll();


        // Create the main dashboard frame
        JFrame dashFrame = new JFrame("Dashboard - " + username);
        dashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashFrame.setSize(900, 700);
        dashFrame.setLayout(new BorderLayout());

        // ---------- Top Panel: Logout, Profile, and Cart ----------
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

        // ---------- Center Panel: Title + Search Bar + Product List ----------
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Title
        JLabel title = new JLabel("Homepage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        centerPanel.add(title, BorderLayout.NORTH);

        // Inner panel to hold search bar and product list
        JPanel searchAndListPanel = new JPanel(new BorderLayout());

        // Product List Panel
        DashboardProductsUI dashboardProducts = new DashboardProductsUI(storage);
        JScrollPane productScrollPane = dashboardProducts.getScrollPane();

        // Search Bar Panel (embedded)
        JPanel searchPanel = SearchUI.buildSearchPanel(storage, username, users, dashboardProducts);
        searchAndListPanel.add(searchPanel, BorderLayout.NORTH);
        searchAndListPanel.add(productScrollPane, BorderLayout.CENTER);

        centerPanel.add(searchAndListPanel, BorderLayout.CENTER);
        dashFrame.add(centerPanel, BorderLayout.CENTER);


        dashFrame.setLocationRelativeTo(null);
        dashFrame.setVisible(true);
        
        JButton cartBtn = new JButton("Cart");
        cartBtn.setPreferredSize(new Dimension(120, 30));
        cartBtn.addActionListener(e -> CartUI.showCartDialog());
        rightPanel.add(cartBtn);

        
        // ---------- Bottom Panel: Add, Delete, and View Product Buttons ----------
        JPanel listingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        int categoryIndex = 0;
        List<Product> products = storage.getCategory(categoryIndex);

        // Add button
        JButton addBtn = new JButton("Add Listing");
        addBtn.setPreferredSize(new Dimension(120, 30));
        addBtn.addActionListener(e -> {
            ListingButtons.addProduct(dashFrame, products, storage, categoryIndex, username);
        });

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setPreferredSize(new Dimension(120, 30));
        deleteBtn.addActionListener(e -> {
            DashboardProductsUI.SearchResult selected = dashboardProducts.getSelectedProduct();
            if (selected != null) {
                ListingButtons.deleteProduct(dashFrame, selected.product, products, storage, categoryIndex, username);
            } else {
                JOptionPane.showMessageDialog(dashFrame, "Select a product first!", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });


        // View button
        JButton viewBtn = new JButton("View");
        viewBtn.setPreferredSize(new Dimension(120, 30));
        viewBtn.addActionListener(e -> {
            DashboardProductsUI.SearchResult selected = dashboardProducts.getSelectedProduct();
            if (selected != null) {
                ListingButtons.viewProduct(dashFrame, selected.product, storage, categoryIndex);
            } else {
                JOptionPane.showMessageDialog(dashFrame, "Select a product first!", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Bookmark button
        JButton bookmarkBtn = new JButton("Bookmark");
        bookmarkBtn.setPreferredSize(new Dimension(120, 30));
        bookmarkBtn.addActionListener(e -> {
            DashboardProductsUI.SearchResult selectedResult = dashboardProducts.getSelectedProduct();
            if (selectedResult != null) {
                Product selectedProduct = selectedResult.product;
                BookmarkManager.saveBookmark(selectedProduct); // your method to save bookmarks
                JOptionPane.showMessageDialog(dashFrame, "Product bookmarked!", "Bookmark", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dashFrame, "Select a product first!", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Add to Cart Button
        JButton cartAddBtn = new JButton("Add to Cart");
        cartAddBtn.setPreferredSize(new Dimension(120, 30));
        cartAddBtn.addActionListener(e -> {
            DashboardProductsUI.SearchResult selectedResult = dashboardProducts.getSelectedProduct();
            if (selectedResult != null) {
            } else {
                JOptionPane.showMessageDialog(dashFrame, "Select a product first!", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        listingPanel.add(cartAddBtn);
        listingPanel.add(addBtn);
        listingPanel.add(deleteBtn);
        listingPanel.add(viewBtn);
        listingPanel.add(bookmarkBtn);

        dashFrame.add(listingPanel, BorderLayout.SOUTH);



        dashFrame.setLocationRelativeTo(null);
        dashFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcommerceApp::new);
    }
}
