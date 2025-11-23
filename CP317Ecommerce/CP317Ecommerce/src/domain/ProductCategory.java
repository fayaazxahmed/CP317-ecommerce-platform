package domain;

public enum ProductCategory {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    FURNITURE("Furniture"),
    BOOKS("Books"),
    HOME_GARDEN("Home & Garden"),
    SPORTS_OUTDOORS("Sports & Outdoors"),
    TOYS_GAMES("Toys & Games"),
    FOOD_BEVERAGE("Food & Beverage"),
    HEALTH_BEAUTY("Health & Beauty"),
    AUTOMOTIVE("Automotive"),
    PET_SUPPLIES("Pet Supplies");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // Helper method to convert from string (for CSV loading)
    public static ProductCategory fromString(String text) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.name().equalsIgnoreCase(text) || 
                category.displayName.equalsIgnoreCase(text)) {
                return category;
            }
        }
        return null;
    }
    
    // Helper to match with existing category strings
    public static ProductCategory fromCategoryName(String categoryName) {
        if (categoryName == null) return null;
        
        switch (categoryName.toLowerCase()) {
            case "electronics":
                return ELECTRONICS;
            case "clothing":
                return CLOTHING;
            case "furniture":
                return FURNITURE;
            default:
                return fromString(categoryName);
        }
    }
}