package EcommerceLauncher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// Nick - Homepage GUI
public class DashboardUI {

    public static void openMainPage(ProductStorage storage, boolean isSeller) {
	JFrame frame = new JFrame(isSeller ? "Seller Homepage" : "Buyer Homepage");
	frame.setSize(800, 600);
	frame.setLayout(new BorderLayout());

	JLabel title = new JLabel(isSeller ? "Seller Homepage" : "Buyer Homepage", SwingConstants.CENTER);
	title.setFont(new Font("Arial", Font.BOLD, 24));
	frame.add(title, BorderLayout.NORTH);

	// Panel for category buttons
	JPanel categoryPanel = new JPanel(new FlowLayout());
	String[] categories = storage.getCategories();

	for (int i = 0; i < categories.length; i++) {
	    int index = i;
	    JButton btn = new JButton(categories[i]);
	    btn.addActionListener(e -> CategoryUI.open(categories[index], storage.getCategory(index), index, storage,
		    isSeller, () -> openMainPage(storage, isSeller)));
	    categoryPanel.add(btn);
	}
	frame.add(categoryPanel, BorderLayout.CENTER);

	JButton switchRole = new JButton(isSeller ? "Switch to Buyer" : "Switch to Seller");
	switchRole.addActionListener(e -> {
	    frame.dispose();
	    openMainPage(storage, !isSeller);
	});
	frame.add(switchRole, BorderLayout.SOUTH);

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }
}
