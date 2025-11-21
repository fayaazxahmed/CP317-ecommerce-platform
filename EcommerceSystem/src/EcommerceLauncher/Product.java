package EcommerceLauncher;

public class Product {
    String name;
    String price;
    String description;
    int views;
    int unitsSold;
    String imagePath;

    public Product(String name, String price, String description, int views, int unitsSold, String imagePath) {
    	this.name = name;
    	this.price = price;
    	this.description = description;
    	this.views = views;
    	this.unitsSold = unitsSold;
    	this.imagePath = (imagePath == null ? "" : imagePath);
    }

    public String toFileLine() {
	return name.replace(",", " ") + "," + price + "," + description.replace(",", " ") + "," + views + ","
		+ unitsSold + "," + imagePath.replace(",", " ");
    }

    
    // ADDED GETTER METHODS
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }
    
	public void setViews(int views) {
		this.views = views;
	}

    public int getUnitsSold() {
        return unitsSold;
    }

    public static Product fromFileLine(String line) {
	String[] parts = line.split(",", 6);
	if (parts.length == 6) {
	    return new Product(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
		    parts[5]);
	}
	return null;
    }

    @Override
    public String toString() {
	return name + " - $" + price;
    }


}
