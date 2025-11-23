package ui;

import domain.CartItem;
import data.CartRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CartUI {

    /** Show the cart in a popup dialog */
    public static void showCartDialog() {
        // Get items from repository
        List<CartItem> items = CartRepository.loadCartItems();
        int total = CartRepository.calculateTotal(items);

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

        JTextArea text = new JTextArea(sb.toString());
        text.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setPreferredSize(new Dimension(350, 300));

        JOptionPane.showMessageDialog(
                null,
                scrollPane,
                "Shopping Cart",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
