package ui;

import data.ProductRepository;
import domain.Product;
import domain.User;
import util.Helpers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchUI {

    /**
     * Returns a JPanel containing search bar + filter button
     * Tied directly to the dashboardProducts instance
     */
    public static JPanel buildSearchPanel(ProductRepository storage, String username, List<User> users, DashboardProductsUI dashboardProducts, String[] categories) {
        User currentUser = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton filterBtn = new JButton("Filter by Preferences");

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(filterBtn);

        // --- Search action ---
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            List<Product> results = new ArrayList<>();

            for (int i = 0; i < categories.length; i++) {
                for (Product p : storage.getCategory(i)) {
                    if (p.getName().toLowerCase().contains(query) ||
                        p.getDescription().toLowerCase().contains(query)) {
                        results.add(p);
                    }
                }
            }

            dashboardProducts.setProducts(results, categories);
        });

        // --- Filter preferences ---
        filterBtn.addActionListener(e -> {
            if (currentUser == null) return;

            JCheckBox[] boxes = new JCheckBox[categories.length];
            JPanel filterPanel = new JPanel(new GridLayout(categories.length + 1, 1, 5, 5));
            filterPanel.add(new JLabel("Select categories:"));

            for (int i = 0; i < categories.length; i++) {
                boxes[i] = new JCheckBox(categories[i]);
                if (currentUser.getPreferences().contains(categories[i])) {
                    boxes[i].setSelected(true);
                }
                filterPanel.add(boxes[i]);
            }

            int result = JOptionPane.showConfirmDialog(null, filterPanel, "Filter Preferences",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                List<String> selectedCategories = new ArrayList<>();
                currentUser.getPreferences().clear();

                for (int i = 0; i < boxes.length; i++) {
                    if (boxes[i].isSelected()) {
                        currentUser.getPreferences().add(categories[i]);
                        selectedCategories.add(categories[i]);
                    }
                }

                Helpers.saveUsers(users, categories);

                List<Product> filteredProducts = new ArrayList<>();
                if (selectedCategories.isEmpty()) {
                    for (int i = 0; i < categories.length; i++)
                        filteredProducts.addAll(storage.getCategory(i));
                } else {
                    for (int i = 0; i < categories.length; i++)
                        if (selectedCategories.contains(categories[i]))
                            filteredProducts.addAll(storage.getCategory(i));
                }

                dashboardProducts.setProducts(filteredProducts, categories);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());

        return panel;
    }
}
