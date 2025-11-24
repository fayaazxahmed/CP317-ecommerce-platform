package ui;

import domain.User;
import util.Helpers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProfileUI {

    public static void openProfile(JFrame parentFrame,
                                   String username,
                                   List<User> users,
                                   Runnable onSaveCallback,
                                   String[] categories,
                                   Runnable onDeleteAccountCallback) {

        User currentUser = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (currentUser == null) {
            JOptionPane.showMessageDialog(parentFrame, "User not found.");
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField usernameField = new JTextField(currentUser.getUsername(), 15);
        usernameField.setEditable(false);

        JTextField emailField = new JTextField(currentUser.getEmail(), 15);
        emailField.setEditable(false);

        JPasswordField passwordField = new JPasswordField(currentUser.getPassword(), 15);
        passwordField.setEditable(false);

        JTextField addressField = new JTextField(currentUser.getAddress(), 15);

        gbc.gridx = 0; gbc.gridy = 0; userInfoPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; userInfoPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; userInfoPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; userInfoPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; userInfoPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; userInfoPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; userInfoPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; userInfoPanel.add(addressField, gbc);

        mainPanel.add(userInfoPanel, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Preferred Categories"));
        int rows = (int) Math.ceil(categories.length / 2.0);
        categoryPanel.setLayout(new GridLayout(rows, 2, 10, 6));

        JCheckBox[] prefBoxes = new JCheckBox[categories.length];
        for (int i = 0; i < categories.length; i++) {
            prefBoxes[i] = new JCheckBox(categories[i]);
            if (currentUser.getPreferences() != null && currentUser.getPreferences().contains(categories[i]))
                prefBoxes[i].setSelected(true);
            categoryPanel.add(prefBoxes[i]);
        }

        mainPanel.add(categoryPanel, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Changes");
        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setForeground(Color.RED);

        saveBtn.addActionListener(e -> {
            currentUser.setAddress(addressField.getText().trim());

            List<String> newPrefs = new ArrayList<>();
            for (JCheckBox cb : prefBoxes) if (cb.isSelected()) newPrefs.add(cb.getText());
            currentUser.setPreferences(newPrefs);

            Helpers.saveUsers(users, categories);

            JOptionPane.showMessageDialog(parentFrame, "Profile updated successfully!");
            if (onSaveCallback != null) onSaveCallback.run();
        });

        deleteBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(parentFrame,
                    "Are you sure you want to delete your account?\nThis action cannot be undone.",
                    "Confirm Account Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                users.remove(currentUser);
                Helpers.saveUsers(users, categories);
                JOptionPane.showMessageDialog(parentFrame, "Your account has been deleted.");
                if (onDeleteAccountCallback != null) onDeleteAccountCallback.run();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(saveBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(parentFrame, "Profile", true);
        dialog.setSize(470, 560);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setLayout(new BorderLayout());
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
