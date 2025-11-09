
package EcommerceLauncher;

//Ahmed/Nick - Product data class
public class Product {
    String name;
    String price;
    String description;
    int views;
    int unitsSold;

    public Product(String name, String price, String description, int views, int unitsSold) {
	this.name = name;
	this.price = price;
	this.description = description;
	this.views = views;
	this.unitsSold = unitsSold;
    }

    // Convert product to a line for saving
    public String toFileLine() {
	return name + "," + price + "," + description.replace(",", " ") + "," + views + "," + unitsSold;
    }

    // Create product from a saved line
    public static Product fromFileLine(String line) {
	String[] parts = line.split(",", 5);
	if (parts.length == 5) {
	    return new Product(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
	}
	return null;
    }

    @Override
    public String toString() {
	return name + " - $" + price;
    }
}
