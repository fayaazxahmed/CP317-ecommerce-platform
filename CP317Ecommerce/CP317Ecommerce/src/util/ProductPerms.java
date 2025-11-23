package util;

import domain.Product;

public class ProductPerms {

    /**
     * Determines if the logged-in user is allowed to delete this product.
     */
    public static boolean canDelete(Product product, String username) {
        if (product == null || username == null)
            return false;

        String creator = product.getCreatedBy();
        if (creator == null)
            return false;

        return creator.equals(username);
    }
}
