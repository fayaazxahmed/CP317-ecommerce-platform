package ui;

import domain.CartItem;
import data.CartRepository;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CartUI {

    /**
     * Show the cart in a dropdown popup menu (JPopupMenu)
     * @param parentButton The button the dropdown should appear under
     */
    public static void showCartDropdown(JButton parentButton) {

        // Load items
        List<CartItem> items = CartRepository.loadCartItems();
        int total = CartRepository.calculateTotal(items);

        // Create dropdown menu
        JPopupMenu menu = new JPopupMenu();

        if (items.isEmpty()) {
            JMenuItem empty = new JMenuItem("Cart is empty");
            empty.setEnabled(false);
            menu.add(empty);
        } else {
            // Add each item as a menu entry
            for (CartItem item : items) {
                JMenuItem entry = new JMenuItem(item.getName() + " - $" + item.getPrice());
                entry.setEnabled(false);
                menu.add(entry);
            }

            // Divider
            menu.addSeparator();

            // Total
            JMenuItem totalLabel = new JMenuItem("Total: $" + total);
            totalLabel.setEnabled(false);
            menu.add(totalLabel);
        }

        // ----- CLEAR CART BUTTON -----
        JMenuItem clearBtn = new JMenuItem("Clear Cart");
        clearBtn.addActionListener((ActionEvent e) -> {
            CartRepository.clearCart();
            JOptionPane.showMessageDialog(parentButton, "Cart cleared!");
        });
        menu.add(clearBtn);

        // Show dropdown below button
        menu.show(parentButton, 0, parentButton.getHeight());
    }
}
