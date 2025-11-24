package ui;

import data.ProductRepository;
import domain.Product;
import domain.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DashboardProductsUI {

    private DefaultListModel<SearchResult> listModel;
    private JList<SearchResult> resultList;
    private ProductRepository storage;

    public DashboardProductsUI(ProductRepository storage) {
        this.storage = storage;
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);

        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        resultList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof SearchResult) {
                    SearchResult r = (SearchResult) value;
                    setText(String.format(
                        "<html><b>%s</b> - $%s<br><i>%s</i> - Views: %d, Sold: %d</html>",
                        r.product.getName(),
                        r.product.getPrice(),
                        r.categoryName,
                        r.product.getViews(),
                        r.product.getUnitsSold()
                    ));
                }

                return this;
            }
        });

        loadAllProducts();
    }


    // Load all products from all categories
    public void loadAllProducts() {
        listModel.clear();
        String[] categories = storage.getCategories();

        for (int i = 0; i < categories.length; i++) {
            for (Product p : storage.getCategory(i)) {
                listModel.addElement(new SearchResult(p, categories[i], i));
            }
        }
    }


    // Apply preference sorting
    public void applyUserPreferences(User user) {
        if (user == null) return;

        List<String> prefs = user.getPreferences();
        if (prefs == null || prefs.isEmpty()) {
            loadAllProducts();
            return;
        }

        List<SearchResult> all = new ArrayList<>();
        String[] categories = storage.getCategories();

        for (int i = 0; i < categories.length; i++) {
            for (Product p : storage.getCategory(i)) {
                all.add(new SearchResult(p, categories[i], i));
            }
        }

        all.sort((a, b) -> {
            boolean aPref = prefs.contains(a.categoryName);
            boolean bPref = prefs.contains(b.categoryName);

            if (aPref && !bPref) return -1;
            if (!aPref && bPref) return 1;
            return 0;
        });

        listModel.clear();
        for (SearchResult r : all) listModel.addElement(r);
    }


    // Called by search UI
    public void setProducts(List<Product> products, String[] categories) {
        listModel.clear();

        for (Product p : products) {
            for (int i = 0; i < categories.length; i++) {
                if (storage.getCategory(i).contains(p)) {
                    listModel.addElement(new SearchResult(p, categories[i], i));
                    break;
                }
            }
        }
    }


    // JList wrapper utilities
    public JList<SearchResult> getResultList() { return resultList; }
    public SearchResult getSelectedProduct() { return resultList.getSelectedValue(); }
    public void refresh() { loadAllProducts(); }

    public static class SearchResult {
        public Product product;
        public String categoryName;
        public int categoryIndex;

        public SearchResult(Product product, String categoryName, int categoryIndex) {
            this.product = product;
            this.categoryName = categoryName;
            this.categoryIndex = categoryIndex;
        }

        @Override
        public String toString() {
            return product.getName() + " - $" + product.getPrice() + " (" + categoryName + ")";
        }
    }

    public JScrollPane getScrollPane() {
        return new JScrollPane(resultList);
    }
}
