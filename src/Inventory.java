import java.util.*;

/**
 * Inventory class to manage the inventory in the vending machine
 *
 * TODO: Allow admin to add more products
 *
 * @author Mehdi Himmiche
 */
public class Inventory {
    // Default products available in the machine - in the future it will allow adding more products
    private String[] defaultProducts = {"Coca-Cola","Pepsi","Sprite","Water","Dr. Pepper", "Coke Zero", "Diet Coke", "Cherry Coke"};
    // Hash map containing the product object and quantity
    private HashMap<Product, Integer> productInventory = new HashMap<>();
    // Starting quantity for the vending machine
    private int quantity = 5;

    /**
     * Construct the inventory class
     * Populate the the hash map
     */
    public Inventory() {
        for (int i = 0; i < defaultProducts.length; i++) {
            productInventory.put(new Product(defaultProducts[i], 1.25), quantity);
        }
    }

    /**
     * Return the quantity available for a given product
     * @param aName name of the product
     * @return quantity remaining
     */
    public int getQuantity(String aName) {
        Product temp = new Product();
        for (Product names : productInventory.keySet()) {
            if (names.getName().equals(aName)) {
                temp = names;
            }
        }
        return productInventory.get(temp);
    }

    /**
     * Allow the admin to update the quantity of a product
     * @param aName name of the product
     * @param aQuantity new quantity
     */
    public void updateProductAvailability(String aName, int aQuantity) {
        for (Map.Entry<Product,Integer> mapIter : productInventory.entrySet()) {
            Product prod = mapIter.getKey();
            if (prod.getName().equals(aName)) {
                mapIter.setValue(aQuantity);
            }
        }
    }

    /**
     * Determine if a product is available to be sold
     * @param aName name of the product
     * @return a boolean determining the availability
     */
    public boolean checkProductAvailability(String aName) {
        Product temp = new Product();
        for (Product names : productInventory.keySet()) {
            if (names.getName().equals(aName)) {
                temp = names;
            }
        }
        return (productInventory.get(temp) > 0);
    }

    /**
     * Store the products available for sale in an arraylist (done this way to allow admin to add more products in the future)
     * @return Arraylist containing the product names
     */
    public ArrayList<String> getProductInventory() {
        ArrayList<String> productNames = new ArrayList<>();
        for (Product names : productInventory.keySet()) {
            productNames.add(names.getName());
        }
        return productNames;
    }

    /**
     * Get the price of a specified product
     * @param prodName product name
     * @return price
     */
    public double getProductPrice(String prodName) {
        double productPrice = 0;
        for (Product names : productInventory.keySet()) {
            if (names.getName().equals(prodName)) {
                productPrice = names.getPrice();
            }
        }
        return productPrice;
    }

    /**
     * Determine how many products are to be sold
     * @return number of products
     */
    public int getProductNumber() {
        return productInventory.size();
    }

    /**
     * Check through the product inventory and determine if a product can be sold
     * @param aProdName product name
     * @return if a product can be sold
     */
    public boolean vendProduct(String aProdName) {
        boolean canVend = false;
        for (Map.Entry<Product,Integer> mapIter : productInventory.entrySet()) {
            Product prod = mapIter.getKey();
            if (prod.getName().equals(aProdName) && mapIter.getValue() > 0) {
                mapIter.setValue(mapIter.getValue() - 1);
                canVend = true;
            }
        }
        return canVend;
    }

}
