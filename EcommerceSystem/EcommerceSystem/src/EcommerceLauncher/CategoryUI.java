package EcommerceLauncher;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Jack/Fayaaz/Sidharth - Category page 
public class CategoryUI {

    public static void open(String categoryName, List<Product> products, int categoryIndex, ProductStorage storage,
	    boolean isSeller, Runnable backAction) {
	JFrame categoryWindow = new JFrame(categoryName);
	categoryWindow.setSize(800, 600);

	DefaultListModel<Product> productListModel = new DefaultListModel<>();
	for (Product product : products)
	    productListModel.addElement(product);

	JList<Product> productList = new JList<>(productListModel);
	categoryWindow.add(new JScrollPane(productList), BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	JButton viewButton = new JButton("View");
	JButton addButton = new JButton("Add");
	JButton deleteButton = new JButton("Delete");
	JButton backButton = new JButton("Back");

	buttonPanel.add(viewButton);
	if (isSeller) {
	    buttonPanel.add(addButton);
	    buttonPanel.add(deleteButton);
	}
	buttonPanel.add(backButton);
	categoryWindow.add(buttonPanel, BorderLayout.SOUTH);

	// VIEW BUTTON
	viewButton.addActionListener(e -> {
	    Product selectedProduct = productList.getSelectedValue();
	    if (selectedProduct != null) {
		if (!isSeller) {
		    selectedProduct.views++;
		    storage.saveCategory(categoryIndex);
		}

		String info = "Description: " + selectedProduct.description + "\nViews: " + selectedProduct.views
			+ "\nUnits Sold: " + selectedProduct.unitsSold;

		if (isSeller) {
		    try {
			double revenue = selectedProduct.unitsSold * Double.parseDouble(selectedProduct.price);
			info += "\nRevenue: $" + revenue;
		    } catch (NumberFormatException ex) {
			info += "\nRevenue: N/A";
		    }
		}

		JOptionPane.showMessageDialog(categoryWindow, info, selectedProduct.name,
			JOptionPane.INFORMATION_MESSAGE);
	    }
	});

	// ADD BUTTON
	addButton.addActionListener(e -> {
	    JTextField nameField = new JTextField();
	    JTextField priceField = new JTextField();
	    JTextArea descField = new JTextArea(3, 20);

	    Object[] input = { "Name:", nameField, "Price:", priceField, "Description:", descField };
	    int option = JOptionPane.showConfirmDialog(categoryWindow, input, "Add Product",
		    JOptionPane.OK_CANCEL_OPTION);

	    if (option == JOptionPane.OK_OPTION) {
		Product newProduct = new Product(nameField.getText(), priceField.getText(), descField.getText(), 0, 0);
		products.add(newProduct);
		productListModel.addElement(newProduct);
		storage.saveCategory(categoryIndex);
	    }
	});

	// DELETE BUTTON
	deleteButton.addActionListener(e -> {
	    Product selectedProduct = productList.getSelectedValue();
	    if (selectedProduct != null) {
		products.remove(selectedProduct);
		productListModel.removeElement(selectedProduct);
		storage.saveCategory(categoryIndex);
	    }
	});

	backButton.addActionListener(e -> {
	    categoryWindow.dispose();
	    backAction.run();
	});

	categoryWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	categoryWindow.setLocationRelativeTo(null);
	categoryWindow.setVisible(true);
    }
}
