package data;

import domain.CartItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {

    private static final String CART_FILE = "cartItems.csv";

    /** Load cart items from CSV file */
    public static List<CartItem> loadCartItems() {
        List<CartItem> items = new ArrayList<>();

        File file = new File(CART_FILE);
        if (!file.exists()) {
            return items; // empty cart, no crash
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int price = Integer.parseInt(parts[1]);
                items.add(new CartItem(name, price));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    /** Calculate total value of a cart */
    public static int calculateTotal(List<CartItem> items) {
        int sum = 0;
        for (CartItem item : items) {
            sum += item.getPrice();
        }
        return sum;
    }

    // Add new items to cart
    public static void addToCart(CartItem item) {
        List<CartItem> items = loadCartItems();  // load existing items
        items.add(item);                         // add new one

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))) {
            for (CartItem i : items) {
                writer.write(i.getName() + "," + i.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear cart file
    public static void clearCart() {
        try (FileWriter writer = new FileWriter(CART_FILE, false)) {
            // Write nothing → file becomes empty
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
