package ui;

import data.ProductRepository;
import domain.Product;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    public void loadAllProducts() {
        listModel.clear();
        String[] categories = storage.getCategories();
        for (int i = 0; i < categories.length; i++) {
            for (Product product : storage.getCategory(i)) {
                listModel.addElement(new SearchResult(product, categories[i], i));
            }
        }
    }

    public void setProducts(List<Product> filteredProducts) {
        listModel.clear();
        String[] categories = storage.getCategories();

        for (Product product : filteredProducts) {
            int catIndex = -1;
            String catName = "";
            for (int i = 0; i < categories.length; i++) {
                if (storage.getCategory(i).contains(product)) {
                    catIndex = i;
                    catName = categories[i];
                    break;
                }
            }
            listModel.addElement(new SearchResult(product, catName, catIndex));
        }
        resultList.repaint();
    }

    public JList<SearchResult> getResultList() {
        return resultList;
    }

    public SearchResult getSelectedProduct() {
        return resultList.getSelectedValue();
    }

    public void refresh() {
        loadAllProducts();
    }

    public JScrollPane getScrollPane() {
        return new JScrollPane(resultList);
    }

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
}
