import java.util.*;

public class Inventory {
    private String[] defaultProducts = {"Coca-Cola","Pepsi","Sprite","Water","Dr. Pepper", "Coke Zero", "Diet Coke"};
    private HashMap<Product, Integer> productInventory = new HashMap<>();
    private int quantity = 1;
    public Inventory() {
        for (int i = 0; i < defaultProducts.length; i++) {
            productInventory.put(new Product(defaultProducts[i], 1.25), quantity);
        }
    }

    /*public void addProductQuantity(String aProd) {
        productInventory.add(aProd);
    }*/

    public ArrayList<String> getProductInventory() {
        ArrayList<String> productNames = new ArrayList<>();
        for (Product names : productInventory.keySet()) {
            productNames.add(names.getName());
        }
        return productNames;
    }

    public double getProductPrice(String prodName) {
        double productPrice = 0;
        for (Product names : productInventory.keySet()) {
            if (names.getName().equals(prodName)) {
                productPrice = names.getPrice();
            }
        }
        return productPrice;
    }

    public int getProductNumber() {
        return productInventory.size();
    }

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
