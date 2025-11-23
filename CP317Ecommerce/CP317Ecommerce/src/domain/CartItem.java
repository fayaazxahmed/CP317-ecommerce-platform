package domain;

public class CartItem {
    private final String name;
    private final int price;

    public CartItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}
