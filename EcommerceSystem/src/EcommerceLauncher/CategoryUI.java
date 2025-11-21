package EcommerceLauncher;

import java.awt.BorderLayout;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

	// VIEW PRODUCT WITH IMAGE
	viewButton.addActionListener(e -> {
	    Product selected = productList.getSelectedValue();
	    if (selected != null) {

		if (isSeller) {
		    selected.views++;
		    storage.saveCategory(categoryIndex);
		}

		JPanel panel = new JPanel(new BorderLayout());

		JLabel imgLabel = ProductImageManager.getImageLabel(selected.imagePath, 250, 250);
		panel.add(imgLabel, BorderLayout.CENTER);

		JTextArea text = new JTextArea("Description: " + selected.description + "\n" + "Views: "
			+ selected.views + "\n" + "Units Sold: " + selected.unitsSold);
		text.setEditable(false);
		panel.add(text, BorderLayout.SOUTH);

		JOptionPane.showMessageDialog(categoryWindow, panel, selected.name, JOptionPane.PLAIN_MESSAGE);
	    }
	});

	// ✅ ADD PRODUCT WITH IMAGE CHOOSER
	addButton.addActionListener(e -> {
	    JTextField nameField = new JTextField();
	    JTextField priceField = new JTextField();
	    JTextArea descField = new JTextArea(3, 20);

	    JButton imageBtn = new JButton("Choose Image");
	    JLabel imageStatus = new JLabel("No Image Selected");
	    final String[] imgPath = { "" };

	    imageBtn.addActionListener(x -> {
		imgPath[0] = ProductImageManager.chooseImage();
		if (!imgPath[0].isEmpty())
		    imageStatus.setText(new File(imgPath[0]).getName());
	    });

	    Object[] inputs = { "Name:", nameField, "Price:", priceField, "Description:", descField, imageBtn,
		    imageStatus };

	    int option = JOptionPane.showConfirmDialog(categoryWindow, inputs, "Add Product",
		    JOptionPane.OK_CANCEL_OPTION);

	    if (option == JOptionPane.OK_OPTION) {
		Product newP = new Product(nameField.getText(), priceField.getText(), descField.getText(), 0, 0,
			imgPath[0] // ✅ Saves image path
		);
		products.add(newP);
		productListModel.addElement(newP);
		storage.saveCategory(categoryIndex);
	    }
	});

	deleteButton.addActionListener(e -> {
	    Product selected = productList.getSelectedValue();
	    if (selected != null) {
		products.remove(selected);
		productListModel.removeElement(selected);
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
