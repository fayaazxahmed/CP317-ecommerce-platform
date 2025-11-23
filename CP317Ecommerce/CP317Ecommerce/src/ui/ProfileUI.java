package ui;

import app.EcommerceApp;
import domain.User;
import util.Helpers;

import javax.swing.*;

import java.awt.*;
import java.util.List;

public class ProfileUI {

	public static void openProfile(JFrame parentFrame, String username, List<User> users) {
	    // Find the user
	    final User currentUser = users.stream()
	            .filter(u -> u.getUsername().equals(username))
	            .findFirst()
	            .orElse(null);

	    if (currentUser == null) {
	        JOptionPane.showMessageDialog(parentFrame, "User not found.");
	        return;
	    }

	    JFrame profileFrame = new JFrame("Profile - " + username);
	    profileFrame.setSize(400, 350);
	    profileFrame.setLayout(new BorderLayout());

	    JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
	    infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	    // Masked password
	    String maskedPassword = "*".repeat(currentUser.getPassword().length());

	    infoPanel.add(new JLabel("Username:"));
	    infoPanel.add(new JLabel(currentUser.getUsername()));
	    infoPanel.add(new JLabel("Email:"));
	    infoPanel.add(new JLabel(currentUser.getEmail()));
	    infoPanel.add(new JLabel("Password:"));
	    infoPanel.add(new JLabel(maskedPassword));

	    profileFrame.add(infoPanel, BorderLayout.CENTER);

	    // Buttons
	    JButton deleteBtn = new JButton("Delete Account");
	    JButton closeBtn = new JButton("Close");

	    deleteBtn.addActionListener(e -> {
	        int confirm = JOptionPane.showConfirmDialog(profileFrame,
	                "Are you sure you want to delete your account? This action cannot be undone.",
	                "Confirm Delete", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            deleteAccount(currentUser, users);
	            JOptionPane.showMessageDialog(profileFrame, "Account deleted successfully.");
	            profileFrame.dispose();
	            parentFrame.dispose();
	            new EcommerceApp().createLoginScreen();
	        }
	    });

	    closeBtn.addActionListener(e -> profileFrame.dispose());

	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    buttonPanel.add(deleteBtn);
	    buttonPanel.add(closeBtn);
	    profileFrame.add(buttonPanel, BorderLayout.SOUTH);

	    profileFrame.setLocationRelativeTo(parentFrame);
	    profileFrame.setVisible(true);
	}

    // --- Delete account logic ---
    private static void deleteAccount(User user, List<User> users) {
        users.remove(user);
        Helpers.saveUsers(users); // Assuming you have this method in Helpers
    }
}
