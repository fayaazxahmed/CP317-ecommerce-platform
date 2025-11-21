package ecommerce;

import EcommerceLauncher.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchUI {

    /**
     * Opens a search window that allows users to search across all product categories
     * @param storage The ProductStorage containing all products
     * @param isSeller Whether the user is in seller mode
     */
    public static void openSearch(ProductStorage storage, boolean isSeller) {
        JFrame searchFrame = new JFrame("Search Products");
        searchFrame.setSize(700, 500);
        searchFrame.setLayout(new BorderLayout(10, 10));

        // Top panel with search bar
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBarPanel.add(searchLabel);
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        
        topPanel.add(searchBarPanel, BorderLayout.NORTH);

        // Results label
        JLabel resultsLabel = new JLabel("All Products:");
        topPanel.add(resultsLabel, BorderLayout.SOUTH);

        searchFrame.add(topPanel, BorderLayout.NORTH);

        // Center panel with product list
        DefaultListModel<SearchResult> listModel = new DefaultListModel<>();
        JList<SearchResult> resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom cell renderer for better display
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

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton viewButton = new JButton("View Product");
        JButton closeButton = new JButton("Close");
        
        bottomPanel.add(viewButton);
        bottomPanel.add(closeButton);
        
        searchFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Load all products initially
        loadAllProducts(storage, listModel);
        resultsLabel.setText("All Products (" + listModel.getSize() + " found):");

        // Search button action
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                // Show all products if search is empty
                loadAllProducts(storage, listModel);
                resultsLabel.setText("All Products (" + listModel.getSize() + " found):");
            } else {
                // Filter products by search query
                searchProducts(storage, listModel, query);
                resultsLabel.setText("Search Results (" + listModel.getSize() + " found):");
            }
        });

        // Allow Enter key to trigger search
        searchField.addActionListener(e -> searchButton.doClick());

       
        // Double-click to view product
        resultList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewButton.doClick();
                }
            }
        });

        // Close button action
        closeButton.addActionListener(e -> searchFrame.dispose());

        searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchFrame.setLocationRelativeTo(null);
        searchFrame.setVisible(true);
    }

    /**
     * Loads all products from all categories into the list
     */
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

    /**
     * Searches for products matching the query across all categories
     */
    private static void searchProducts(ProductStorage storage, DefaultListModel<SearchResult> listModel, String query) {
        listModel.clear();
        String[] categories = storage.getCategories();
        
        for (int i = 0; i < categories.length; i++) {
            List<Product> products = storage.getCategory(i);
            for (Product product : products) {
                // Search in product name, description, and category name
                if (product.getName().toLowerCase().contains(query) ||
                    product.getDescription().toLowerCase().contains(query) ||
                    categories[i].toLowerCase().contains(query)) {
                    listModel.addElement(new SearchResult(product, categories[i], i));
                }
            }
        }
    }

    /**
     * Shows detailed information about a product in a dialog
     */
    private static void showProductDetails(JFrame parent, SearchResult result, boolean isSeller) {
        Product product = result.product;
        
        StringBuilder info = new StringBuilder();
        info.append("Category: ").append(result.categoryName).append("\n");
        info.append("Product: ").append(product.getName()).append("\n");
        info.append("Price: $").append(product.getPrice()).append("\n");
        info.append("Description: ").append(product.getDescription()).append("\n");
        info.append("Views: ").append(product.getViews()).append("\n");
        info.append("Units Sold: ").append(product.getUnitsSold()).append("\n");
        
        // Show revenue for sellers
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

    /**
     * Helper class to store search results with category information
     */
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