package accountsystem;

public class User {
    private String username;
    private String password;
    private String email;
    private String shippingAddress;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.shippingAddress = ""; // Default to blank
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
    
    public String getShippingAddress () {
    	return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
    	this.shippingAddress = shippingAddress;
    }
    
}
