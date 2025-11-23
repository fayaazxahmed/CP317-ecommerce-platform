package domain;

public class Product {
    String name;
    String price;
    String description;
    int views;
    int unitsSold;
    String imagePath;
    String createdBy;

    public Product(String name, String price, String description, int views, int unitsSold, String imagePath, String createdBy) {
    	this.name = name;
    	this.price = price;
    	this.description = description;
    	this.views = views;
    	this.unitsSold = unitsSold;
    	this.imagePath = (imagePath == null ? "" : imagePath);
    	this.createdBy = createdBy;
    }

    public String toFileLine() {
        return name.replace(",", " ") + "," +
               price + "," +
               description.replace(",", " ") + "," +
               views + "," +
               unitsSold + "," +
               imagePath.replace(",", " ") + "," +
               (createdBy == null ? "" : createdBy.replace(",", " "));
    }



    
    // Getters
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
    
    public String getImagePath() {
        return imagePath;
    }

    public int getUnitsSold() {
        return unitsSold;
    }
    
    public String getCreatedBy() {
    	return createdBy; 
    }

    // Setters
    
	public void setViews(int views) {
		this.views = views;
	}

	public static Product fromFileLine(String line) {
	    String[] parts = line.split(",", 7);
	    if (parts.length == 7) {
	        return new Product(
	            parts[0],
	            parts[1],
	            parts[2],
	            Integer.parseInt(parts[3]),
	            Integer.parseInt(parts[4]),
	            parts[5],
	            parts[6]
	        );
	    }
	    return null;
	}


    @Override
    public String toString() {
	return name + " - $" + price;
    }


}
