package domain;

import java.util.*;

public class User {
    private String username;
    private String password;
    private String email;
    private String address;
    private List<String> preferences;

    public User(String username, String password, String email, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.preferences = new ArrayList<>();
    }

    // ------------------- GETTERS / SETTERS -------------------
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }

    // ------------------- PREFERENCES HELPER -------------------
    public String getPreferencesAsIndexes(String[] categories) {
        List<String> indexes = new ArrayList<>();
        for (String pref : preferences) {
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(pref)) {
                    indexes.add(String.valueOf(i));
                    break;
                }
            }
        }
        return String.join(" ", indexes);
    }

    // Convert numeric indexes back to category names
    public void setPreferencesFromIndexes(String indexString, String[] categories) {
        preferences.clear();
        if (indexString == null || indexString.isEmpty()) return;
        String[] parts = indexString.split(" ");
        for (String p : parts) {
            try {
                int idx = Integer.parseInt(p);
                if (idx >= 0 && idx < categories.length) {
                    preferences.add(categories[idx]);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    // For debugging / display
    @Override
    public String toString() {
        return username + " (" + email + ") - Prefs: " + preferences.toString();
    }
}
