package EcommerceLauncher;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// Fayaaz/Sidharth - Main launcher
public class MainLauncher {
    public static void main(String[] args) {
	String[] categories = { "Clothing", "Furniture", "Electronics" };
	ProductStorage storage = new ProductStorage(categories);
	storage.loadAll();

	SwingUtilities.invokeLater(() -> {
	    JFrame roleFrame = new JFrame("E-Commerce");
	    roleFrame.setSize(300, 200);
	    roleFrame.setLayout(new GridLayout(3, 1));

	    JLabel label = new JLabel("Enter as:", SwingConstants.CENTER);
	    JButton buyerButton = new JButton("Buyer");
	    JButton sellerButton = new JButton("Seller");

	    buyerButton.addActionListener(e -> {
		roleFrame.dispose();
		DashboardUI.openMainPage(storage, false);
	    });
	    sellerButton.addActionListener(e -> {
		roleFrame.dispose();
		DashboardUI.openMainPage(storage, true);
	    });

	    roleFrame.add(label);
	    roleFrame.add(buyerButton);
	    roleFrame.add(sellerButton);

	    roleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    roleFrame.setLocationRelativeTo(null);
	    roleFrame.setVisible(true);
	});
    }
}
