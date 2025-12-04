package ui;

import domain.CartItem;
import data.CartRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CartUI {
    public static void showCartDialog() {

        // Create dialog window
        JDialog dialog = new JDialog();
        dialog.setTitle("Shopping Cart");
        dialog.setSize(400, 400);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        // Load cart items
        List<CartItem> items = CartRepository.loadCartItems();
        int total = CartRepository.calculateTotal(items);

        // Build text output
        StringBuilder sb = new StringBuilder();
        if (items.isEmpty()) {
            sb.append("Cart is empty.");
        } else {
            for (CartItem item : items) {
                sb.append(item.getName())
                  .append(": $")
                  .append(item.getPrice())
                  .append("\n");
            }
            sb.append("\nTotal: $").append(total);
        }

        // Text area
        JTextArea text = new JTextArea(sb.toString());
        text.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Clear Cart Button
        JButton clearBtn = new JButton("Clear Cart");
        clearBtn.addActionListener(e -> {
            CartRepository.clearCart();
            JOptionPane.showMessageDialog(dialog, "Cart cleared!");

            // Update UI
            text.setText("Cart is empty.");
        });

        // Close Button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(clearBtn);
        buttonPanel.add(closeBtn);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Show dialog
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
