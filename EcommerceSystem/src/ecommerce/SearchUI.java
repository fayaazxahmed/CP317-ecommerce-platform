package ecommerce;

import EcommerceLauncher.*;
import accountsystemUI.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchUI {

    public static void openSearch(ProductStorage storage, boolean isSeller, String username, List<User> users) {
        JFrame searchFrame = new JFrame("Search Products");
        searchFrame.setSize(700, 500);
        searchFrame.setLayout(new BorderLayout(10, 10));

        User currentUser = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");
        JButton filterBtn = new JButton("Filter by Preferences");
        
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBarPanel.add(searchLabel);
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        searchBarPanel.add(filterBtn);
        
        topPanel.add(searchBarPanel, BorderLayout.NORTH);

        JLabel resultsLabel = new JLabel("All Products:");
        topPanel.add(resultsLabel, BorderLayout.SOUTH);

        searchFrame.add(topPanel, BorderLayout.NORTH);

        DefaultListModel<SearchResult> listModel = new DefaultListModel<>();
        JList<SearchResult> resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        resultList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SearchResult) {
                    SearchResult result = (SearchResult) value;
                    setText(String.format("<html><b>%s</b> - $%s<br><i>%s</i> - Views: %d, Sold: %d</html>", 
                        result.product.getName(), 
                        result.product.getPrice(),
                        result.categoryName,
                        result.product.getViews(),
                        result.product.getUnitsSold()));
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultList);
        searchFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton viewButton = new JButton("View Product");
        JButton closeButton = new JButton("Close");
        
        bottomPanel.add(viewButton);
        bottomPanel.add(closeButton);
        
        searchFrame.add(bottomPanel, BorderLayout.SOUTH);

        loadAllProducts(storage, listModel);
        resultsLabel.setText("All Products (" + listModel.getSize() + " found):");

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                loadAllProducts(storage, listModel);
                resultsLabel.setText("All Products (" + listModel.getSize() + " found):");
            } else {
                searchProducts(storage, listModel, query, null);
                resultsLabel.setText("Search Results (" + listModel.getSize() + " found):");
            }
        });

        filterBtn.addActionListener(e -> {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(searchFrame, "User not found.");
                return;
            }
            
            String[] categories = storage.getCategories();
            JCheckBox[] checkboxes = new JCheckBox[categories.length];
            
            JPanel panel = new JPanel(new GridLayout(categories.length + 1, 1, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("Select categories to filter:"));
            
            for (int i = 0; i < categories.length; i++) {
                checkboxes[i] = new JCheckBox(categories[i]);
                if (currentUser.getPreferences().contains(categories[i])) {
                    checkboxes[i].setSelected(true);
                }
                panel.add(checkboxes[i]);
            }
            
            int result = JOptionPane.showConfirmDialog(searchFrame, panel, 
                    "Filter by Category", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                currentUser.getPreferences().clear();
                List<String> selectedCategories = new ArrayList<>();
                for (int i = 0; i < checkboxes.length; i++) {
                    if (checkboxes[i].isSelected()) {
                        currentUser.getPreferences().add(categories[i]);
                        selectedCategories.add(categories[i]);
                    }
                }
                Helpers.saveUsers(users);
                
                if (selectedCategories.isEmpty()) {
                    loadAllProducts(storage, listModel);
                    resultsLabel.setText("All Products (" + listModel.getSize() + " found):");
                } else {
                    String query = searchField.getText().trim().toLowerCase();
                    searchProducts(storage, listModel, query, selectedCategories);
                    resultsLabel.setText("Filtered Products (" + listModel.getSize() + " found):");
                }
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());


        viewButton.addActionListener(e -> {
            SearchResult selected = resultList.getSelectedValue();
            if (selected != null) {
                if (!isSeller) {
                    selected.product.setViews(selected.product.getViews() + 1);
                    storage.saveCategory(selected.categoryIndex);
                    int selectedIndex = resultList.getSelectedIndex();
                    listModel.set(selectedIndex, selected);
                }
                
                showProductDetails(searchFrame, selected, isSeller);
            } else {
                JOptionPane.showMessageDialog(searchFrame, 
                    "Please select a product to view.", 
                    "No Selection", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });


        resultList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewButton.doClick();
                }
            }
        });

        closeButton.addActionListener(e -> searchFrame.dispose());

        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }

    private static void loadAllProducts(ProductStorage storage, DefaultListModel<SearchResult> listModel) {
        listModel.clear();
        String[] categories = storage.getCategories();
        
        for (int i = 0; i < categories.length; i++) {
            List<Product> products = storage.getCategory(i);
            for (Product product : products) {
                listModel.addElement(new SearchResult(product, categories[i], i));
            }
        }
    }

    private static void searchProducts(ProductStorage storage, DefaultListModel<SearchResult> listModel, 
                                      String query, List<String> categoryFilter) {
        listModel.clear();
        String[] categories = storage.getCategories();
        
        for (int i = 0; i < categories.length; i++) {
            if (categoryFilter != null && !categoryFilter.isEmpty() && 
                !categoryFilter.contains(categories[i])) {
                continue;
            }
            
            List<Product> products = storage.getCategory(i);
            for (Product product : products) {
                if (query.isEmpty() || 
                    product.getName().toLowerCase().contains(query) ||
                    product.getDescription().toLowerCase().contains(query) ||
                    categories[i].toLowerCase().contains(query)) {
                    listModel.addElement(new SearchResult(product, categories[i], i));
                }
            }
        }
    }

    private static void showProductDetails(JFrame parent, SearchResult result, boolean isSeller) {
        Product product = result.product;
        
        StringBuilder info = new StringBuilder();
        info.append("Category: ").append(result.categoryName).append("\n");
        info.append("Product: ").append(product.getName()).append("\n");
        info.append("Price: $").append(product.getPrice()).append("\n");
        info.append("Description: ").append(product.getDescription()).append("\n");
        info.append("Views: ").append(product.getViews()).append("\n");
        info.append("Units Sold: ").append(product.getUnitsSold()).append("\n");
        
        if (isSeller) {
            try {
                double revenue = product.getUnitsSold() * Double.parseDouble(product.getPrice());
                info.append("Revenue: $").append(String.format("%.2f", revenue)).append("\n");
            } catch (NumberFormatException ex) {
                info.append("Revenue: N/A\n");
            }
        }
        
        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JOptionPane.showMessageDialog(parent, 
            textArea, 
            product.getName() + " - Details", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static class SearchResult {
        Product product;
        String categoryName;
        int categoryIndex;

        SearchResult(Product product, String categoryName, int categoryIndex) {
            this.product = product;
            this.categoryName = categoryName;
            this.categoryIndex = categoryIndex;
        }

        @Override
        public String toString() {
            return product.getName() + " - $" + product.getPrice() + " (" + categoryName + ")";
        }
    }
}