package util;

import domain.Product;
import data.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ListingButtons {

    // ----------------- VIEW PRODUCT -----------------
    public static void viewProduct(JFrame parent, Product selected, ProductRepository storage, int categoryIndex) {
        if (selected == null)
            return;

        // Increment views and save
        selected.setViews(selected.getViews() + 1);
        storage.saveCategory(categoryIndex);

        // Create panel with image and description
        JPanel panel = new JPanel(new BorderLayout());

        JLabel imgLabel = ProductImageManager.getImageLabel(selected.getImagePath(), 250, 250);
        panel.add(imgLabel, BorderLayout.CENTER);

        JTextArea text = new JTextArea(
                "Description: " + selected.getDescription() + "\n" +
                        "Views: " + selected.getViews() + "\n" +
                        "Units Sold: " + selected.getUnitsSold()
        );
        text.setEditable(false);
        panel.add(text, BorderLayout.SOUTH);

        // Show popup dialog
        JOptionPane.showMessageDialog(parent, panel, selected.getName(), JOptionPane.PLAIN_MESSAGE);
    }

    // ----------------- ADD PRODUCT -----------------
    public static void addProduct(JFrame parent, List<Product> products, ProductRepository storage,
                                  int categoryIndex, String username) {

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextArea descField = new JTextArea(3, 20);

        JButton imageBtn = new JButton("Choose Image");
        JLabel imageStatus = new JLabel("No Image Selected");
        final String[] imgPath = { "" };

        imageBtn.addActionListener(x -> {
            imgPath[0] = ProductImageManager.chooseImage();
            if (!imgPath[0].isEmpty())
                imageStatus.setText(new File(imgPath[0]).getName());
        });

        Object[] inputs = {
                "Name:", nameField,
                "Price:", priceField,
                "Description:", descField,
                imageBtn, imageStatus
        };

        int option = JOptionPane.showConfirmDialog(parent, inputs, "Add Product", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            Product newP = new Product(
                    nameField.getText(),
                    priceField.getText(),
                    descField.getText(),
                    0, 0,
                    imgPath[0],
                    username
            );

            products.add(newP);
            storage.saveCategory(categoryIndex);
        }
    }

    // ----------------- DELETE PRODUCT -----------------
    public static void deleteProduct(JFrame parent, Product selected, List<Product> products,
                                     ProductRepository storage, int categoryIndex, String username) {

        if (selected == null)
            return;

        if (!selected.getCreatedBy().equals(username)) {
            JOptionPane.showMessageDialog(parent,
                    "You can only delete products you created.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        products.remove(selected);
        storage.saveCategory(categoryIndex);
    }
}
