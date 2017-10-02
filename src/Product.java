/**
 * Product class containing information on specific products
 *
 * @author Mehdi Himmiche
 */
public class Product {
    private String name;
    private double price;

    /**
     * Construct a product with a name and price
     * @param aName product name
     * @param aPrice product price
     */
    public Product(String aName, double aPrice) {
        name = aName;
        price = aPrice;
    }

    /**
     * Construct a default product
     */
    public Product() {
        name = "";
        price = 0.0;
    }

    /**
     * get the name of a product
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get the price of a product
     * @return price
     */
    public double getPrice() {
        return price;
    }
}
