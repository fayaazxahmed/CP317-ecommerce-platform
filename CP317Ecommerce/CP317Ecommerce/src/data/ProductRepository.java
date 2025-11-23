package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.Product;

// Ahmed - Product storage with persistent saving
public class ProductRepository {
    private String[] categories;
    private ArrayList<List<Product>> items;

    public ProductRepository(String[] categories) {
	this.categories = categories;
	items = new ArrayList<>();
	for (int i = 0; i < categories.length; i++) {
	    items.add(new ArrayList<>());
	}
    }

    // Load all categories from files
    public void loadAll() {
	for (int i = 0; i < categories.length; i++) {
	    File file = new File(categories[i] + ".txt");
	    if (file.exists()) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
			Product p = Product.fromFileLine(line);
			if (p != null)
			    items.get(i).add(p);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    // Save one category to file
    public void saveCategory(int index) {
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(categories[index] + ".txt"))) {
	    for (Product p : items.get(index))
		bw.write(p.toFileLine() + "\n");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    // Get products for a category
    public List<Product> getCategory(int index) {
	return items.get(index);
    }

    public String[] getCategories() {
	return categories;
    }
}
