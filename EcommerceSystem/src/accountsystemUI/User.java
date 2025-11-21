package accountsystemUI;

import ecommerce.ProductCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public String getAddress () {
    	return address;
    }
    
    public void setAddress(String Address) {
    	this.address = Address;
    }

    public List<String> getPreferences() {
        return preferences;
    }
    
    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
    
    public String getPreferencesString() {
        return String.join(";", preferences);
    }
    
    public void setPreferencesFromString(String prefs) {
        preferences.clear();
        if (prefs != null && !prefs.isEmpty()) {
            preferences.addAll(Arrays.asList(prefs.split(";")));
        }
    }

    
}
