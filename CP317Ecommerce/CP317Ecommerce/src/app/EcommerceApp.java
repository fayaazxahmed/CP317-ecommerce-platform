package app;

import ui.*;
import data.*;
import domain.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EcommerceApp {

    private List<User> users;
    private JFrame loginFrame;
    private final String[] categories = { "Clothing", "Furniture", "Electronics" };

    public EcommerceApp() {
        users = Helpers.loadUsers(categories);
        createLoginScreen();
    }

    // ---------- LOGIN / CREATE SCREEN ----------
    public void createLoginScreen() {
        if (loginFrame != null) loginFrame.dispose();

        loginFrame = new JFrame("Account System");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(600, 400);
        loginFrame.setLayout(new BorderLayout());

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
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    // ---------- CREATE ACCOUNT ----------
    private void handleCreateAccount() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
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

        JPanel prefPanel = new JPanel(new GridLayout(0, 1));
        prefPanel.setBorder(BorderFactory.createTitledBorder("Select preferred categories"));
        JCheckBox[] prefBoxes = new JCheckBox[categories.length];
        for (int i = 0; i < categories.length; i++) {
            prefBoxes[i] = new JCheckBox(categories[i]);
            prefPanel.add(prefBoxes[i]);
        }

        JPanel wrapper = new JPanel(new BorderLayout(8, 8));
        wrapper.add(panel, BorderLayout.NORTH);
        wrapper.add(prefPanel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(loginFrame, wrapper,
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

            users = AccountManagerUI.createAccount(users, username, password, email, address, categories);

            User created = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
            if (created != null) {
                List<String> chosen = new ArrayList<>();
                for (JCheckBox cb : prefBoxes)
                    if (cb.isSelected()) chosen.add(cb.getText());

                created.setPreferences(chosen);
                Helpers.saveUsers(users, categories);
            }

            boolean success = AccountManagerUI.login(users, username, password);
            if (success) openDashboard(username);
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
            if (success) openDashboard(username);
            else JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.");
        }
    }

    // ---------- DASHBOARD ----------
    private void openDashboard(String username) {
        if (loginFrame != null) loginFrame.dispose();

        ProductRepository storage = new ProductRepository(categories);
        storage.loadAll();

        JFrame dashFrame = new JFrame("Dashboard - " + username);
        dashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashFrame.setSize(900, 700);
        dashFrame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(120, 30));
        logoutBtn.addActionListener(e -> { dashFrame.dispose(); createLoginScreen(); });

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(logoutBtn);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        DashboardProductsUI dashboardProducts = new DashboardProductsUI(storage);

        JButton profileBtn = new JButton("Profile");
        profileBtn.setPreferredSize(new Dimension(120, 30));
        profileBtn.addActionListener(e -> ProfileUI.openProfile(
                dashFrame,
                username,
                users,
                () -> {
                    User currentUser = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
                    if (currentUser != null) dashboardProducts.applyUserPreferences(currentUser);
                    Helpers.saveUsers(users, categories);
                },
                categories,
                () -> {
                    dashFrame.dispose();
                    createLoginScreen();
                }
        ));

        rightPanel.add(profileBtn);
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        dashFrame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Homepage", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        centerPanel.add(title, BorderLayout.NORTH);

        JPanel searchAndListPanel = new JPanel(new BorderLayout());
        JScrollPane productScrollPane = dashboardProducts.getScrollPane();

        User currentUser = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
        dashboardProducts.applyUserPreferences(currentUser);

        JPanel searchPanel = SearchUI.buildSearchPanel(storage, username, users, dashboardProducts, categories);
        searchAndListPanel.add(searchPanel, BorderLayout.NORTH);
        searchAndListPanel.add(productScrollPane, BorderLayout.CENTER);

        centerPanel.add(searchAndListPanel, BorderLayout.CENTER);
        dashFrame.add(centerPanel, BorderLayout.CENTER);

        dashFrame.setLocationRelativeTo(null);

        JButton cartBtn = new JButton("Cart");
        cartBtn.setPreferredSize(new Dimension(120, 30));
        cartBtn.addActionListener(e -> CartUI.showCartDropdown(cartBtn));
        rightPanel.add(cartBtn);

     // ---------- Bottom Panel: Add, Delete, View, Bookmark, and Add to Cart ----------
        JPanel listingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        int categoryIndex = 0;
        List<Product> products = storage.getCategory(categoryIndex);

        // Add Listing Button
        JButton addBtn = new JButton("Add Listing");
        addBtn.setPreferredSize(new Dimension(120, 30));
        addBtn.addActionListener(e -> {
            ListingButtons.addProduct(dashFrame, products, storage, categoryIndex, username);
        });

        // Delete Button
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

        // View Button
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

        // Bookmark Button
        JButton bookmarkBtn = new JButton("Bookmark");
        bookmarkBtn.setPreferredSize(new Dimension(120, 30));
        bookmarkBtn.addActionListener(e -> {
            DashboardProductsUI.SearchResult selectedResult = dashboardProducts.getSelectedProduct();
            if (selectedResult != null) {
                Product selectedProduct = selectedResult.product;
                BookmarkManager.saveBookmark(selectedProduct);
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
                String name = selectedResult.product.getName();
                int price = Integer.parseInt(selectedResult.product.getPrice());
                data.CartRepository.addToCart(new CartItem(name, price));
            } else {
                JOptionPane.showMessageDialog(dashFrame, "Select a product first!", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add all buttons to the panel
        listingPanel.add(cartAddBtn);
        listingPanel.add(addBtn);
        listingPanel.add(deleteBtn);
        listingPanel.add(viewBtn);
        listingPanel.add(bookmarkBtn);

        // Add bottom panel to the dashboard
        dashFrame.add(listingPanel, BorderLayout.SOUTH);

        // Show the frame after everything is setup
        dashFrame.setVisible(true);


        // Add to dashboard
        dashFrame.add(listingPanel, BorderLayout.SOUTH);
        dashFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcommerceApp::new);
    }
}
