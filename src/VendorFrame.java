import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vendor frame to create the GUI of the vending machine.
 *
 * @author Mehdi Himmiche
 */
public class VendorFrame {

    private AdminController adminControl = new AdminController();
    private Inventory inventoryManager = new Inventory();
    private HashMap<String, JComboBox> updateInv = new HashMap<>();
    private int MAX_PRODUCT_NUM = 15;
    private Integer[] productValues = new Integer[MAX_PRODUCT_NUM + 1];
    private double balance = 0;
    private double price = 0;
    private double change = 0;
    private double moneyInMachine = 0;
    private String productSelected;
    private ArrayList<JButton> productButtons;
    private JLabel balanceLabel;
    private JLabel priceLabel;
    private JLabel changeLabel;
    private JLabel prodBoughtLabel;
    private JLabel messageLabel;
    private JButton vendButton;
    private JButton cancelButton;
    private JButton adminButton;
    private JButton centButton = new JButton("$0.01");
    private JButton nickelButton = new JButton("$0.05");
    private JButton dimeButton = new JButton("$0.10");
    private JButton quarterButton = new JButton("$0.25");
    private JTextField username;
    private JPasswordField password;
    private JLabel loginLabel;
    private JFrame loginFrame;
    private JFrame adminOps;
    private JLabel currentMoneyLabel;

    /**
     * Create the initial frame of the vending machine
     * @return vending machine frame
     */
    public JFrame vendingFrame() {
        JFrame vendFrame = new JFrame("Mehdi Himmiche | Vending Maching");
        vendFrame.setLayout(new BorderLayout());

        JLabel infoLabel = new JLabel("<html>Welcome to my vending machine! Please make a selection.<br>Mehdi Himmiche</html>", SwingConstants.CENTER);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        vendFrame.getContentPane().add(infoLabel, BorderLayout.NORTH);
        JPanel products = productVendArea();
        vendFrame.getContentPane().add(products, BorderLayout.CENTER);
        JPanel input = interactionArea();
        vendFrame.getContentPane().add(input, BorderLayout.EAST);
        JPanel message = messageArea();
        vendFrame.getContentPane().add(message, BorderLayout.SOUTH);

        vendFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vendFrame.pack();
        vendFrame.setVisible(true);
        return vendFrame;
    }

    /**
     * Create the area containing the buttons of the options
     * @return JPanel with product buttons
     */
    private JPanel productVendArea() {
        JPanel productsToVend = new JPanel();
        productsToVend.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int size = inventoryManager.getProductNumber();
        size = size % 2 == 0 ? size / 2 : size / 2 + 1;
        productButtons = new ArrayList<>();
        productsToVend.setLayout(new GridLayout(size,size, 10, 10)); // creating a 10x10 area
        ArrayList<String> products = inventoryManager.getProductInventory();
        for (String prod : products) {
            JButton temp = new JButton(prod);
            temp.addActionListener(new productListener());
            productButtons.add(temp);
            productsToVend.add(temp);
        }
        return productsToVend;
    }

    /**
     * Create the interaction area on the side of the vending machine (vend, cancel, add coins)
     * @return interaction buttons
     */
    private JPanel interactionArea() {
        JPanel userInter = new JPanel();
        userInter.setBorder(BorderFactory.createEmptyBorder(10, 10, 25, 15));
        userInter.setLayout(new GridLayout(6,0, 10,10));
        JPanel insertMoney = moneyButtons();
        userInter.add(insertMoney);
        balanceLabel = new JLabel(String.format("<html><b>Balance</b><br>$%.2f</html>", balance));
        userInter.add(balanceLabel);
        priceLabel = new JLabel("<html><b>Price</b><br>$0.00</html>");
        userInter.add(priceLabel);
        vendButton = new JButton("Vend");
        vendButton.addActionListener(new vendListener());
        userInter.add(vendButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new cancelListener());
        userInter.add(cancelButton);
        adminButton = new JButton("Admin");
        adminButton.addActionListener(new adminListener());
        userInter.add(adminButton);
        return userInter;
    }

    /**
     * Create the buttons for each available coin
     * @return coin buttons
     */
    private JPanel moneyButtons() {
        JPanel moneyIn = new JPanel();
        centButton.addActionListener(new moneyListener());
        nickelButton.addActionListener(new moneyListener());
        dimeButton.addActionListener(new moneyListener());
        quarterButton.addActionListener(new moneyListener());
        moneyIn.setLayout(new GridLayout(2,2,2,2));
        moneyIn.add(centButton);
        moneyIn.add(nickelButton);
        moneyIn.add(dimeButton);
        moneyIn.add(quarterButton);
        return moneyIn;
    }

    /**
     * Create the area at the bottom of the vending machine with general information.
     * @return general information tab
     */
    private JPanel messageArea() {
        JPanel message = new JPanel();
        message.setLayout(new GridLayout(0,2,5,5));
        JPanel changePanel = new JPanel();
        changePanel.setBorder(BorderFactory.createEmptyBorder(10,10,25,5));
        changePanel.setLayout(new GridLayout(0,2));
        changeLabel = new JLabel("<html><b>Change</b><br>$0.00</html>");
        changePanel.add(changeLabel);
        prodBoughtLabel = new JLabel("<html><b>Product Bought:</b><br>N/A</html>");
        changePanel.add(prodBoughtLabel);
        message.add(changePanel);
        messageLabel = new JLabel("Insert Coins to Purchase Product",SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),BorderFactory.createLoweredBevelBorder()));
        message.add(messageLabel);
        return message;
    }

    /**
     * add money to available balance
     * @param aBalance coins inserted
     */
    private void addToBalance(double aBalance) {
        balance += aBalance;
        String out = String.format("<html><b>Balance</b><br>$%.2f</html>", balance);
        balanceLabel.setText(out);
    }

    /**
     * Update the label of the balance
     */
    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("<html><b>Balance</b><br>$%.2f</html>", balance));
    }

    /**
     * Update the label of the price section
     * @param aPrice price of a product
     */
    private void updatePriceLabel(double aPrice) {
        price = aPrice;
        String out = String.format("<html><b>Price</b><br>$%.2f</html>", price);
        priceLabel.setText(out);
    }

    /**
     * Create the admin frame that pops up to login
     */
    private void createAdminPanel() {
        username = new JTextField(8);
        password = new JPasswordField(8);
        JPanel loginArea = adminLoginPanels();
        JPanel buttons = adminLoginButtons();
        loginFrame = new JFrame();
        loginFrame.setLayout(new BorderLayout());
        loginFrame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        loginLabel = new JLabel("Please log in as administrator", SwingConstants.CENTER);
        loginFrame.getContentPane().add(loginLabel, BorderLayout.NORTH);
        loginFrame.getContentPane().add(loginArea, BorderLayout.CENTER);
        loginFrame.getContentPane().add(buttons, BorderLayout.SOUTH);
        loginFrame.setVisible(true);
        loginFrame.pack();
    }

    /**
     * Create the login buttons
     * @return panel with login buttons
     */
    private JPanel adminLoginButtons() {
        JPanel loginButtons = new JPanel();
        JButton login = new JButton("Login");
        login.addActionListener(new adminLoginListener());
        loginButtons.add(login);
        loginButtons.add(Box.createHorizontalStrut(15));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new adminLoginListener());
        loginButtons.add(cancel);
        return loginButtons;
    }

    /**
     * text areas to input username and password to login
     * @return JPanel with login inputs
     */
    private JPanel adminLoginPanels() {
        JPanel admin = new JPanel();
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(Box.createHorizontalStrut(15));
        usernamePanel.add(username);
        usernamePanel.add(Box.createHorizontalStrut(15));
        JPanel pwPanel = new JPanel();
        pwPanel.add(new JLabel("Password:"));
        pwPanel.add(Box.createHorizontalStrut(15));
        pwPanel.add(password);
        admin.add(usernamePanel);
        admin.add(pwPanel);
        return admin;
    }

    /**
     * Create the frame that pops up once an admin has successfully logged in
     */
    private void adminOperations() {
        adminOps = new JFrame("Administrator Frame");
        adminOps.getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JTabbedPane adminPane = new JTabbedPane();
        adminPane.addTab("Withdraw $", withdrawMoney());
        adminPane.addTab("Update Inventory", updateInventoryPanel());
        adminOps.getContentPane().add(adminPane);
        adminOps.setVisible(true);
        adminOps.pack();
    }

    /**
     * Create the panel to allow admin to withdraw money from the vending machine
     * @return withdraw panel
     */
    private JPanel withdrawMoney() {
        JPanel withdraw = new JPanel();
        currentMoneyLabel = new JLabel(String.format("<html>Current Available Money in Machine:<b>$%.2f</html>", moneyInMachine));
        withdraw.add(currentMoneyLabel);
        withdraw.add(Box.createHorizontalStrut(15));
        JButton withdrawButton = new JButton("withdraw funds");
        withdrawButton.addActionListener(new withdrawMoneyListen());
        withdraw.add(withdrawButton);
        return withdraw;
    }

    /**
     * create the panel to allow the admin to update the quantity of each product
     * @return product update panel
     */
    private JPanel updateInventoryPanel() {
        for (int i = 0; i < productValues.length; i++) {
            productValues[i] = i;
        }
        populateHashMap(productValues);
        JPanel update = new JPanel();
        update.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        update.setLayout(new BoxLayout(update,BoxLayout.Y_AXIS));
        for (String prod : updateInv.keySet()) {
            JPanel row = new JPanel();
            row.add(new JLabel(prod + ":"));
            row.add(Box.createHorizontalStrut(15));
            row.add(updateInv.get(prod));
            update.add(row);
        }
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        JButton updateInv = new JButton("Update Inventory");
        updateInv.addActionListener(new updateInventoryListener());
        buttonPanel.add(updateInv, BorderLayout.EAST);
        update.add(buttonPanel);
        return update;
    }

    /**
     * Update a hash map used to store the products and a JComboBox for each one
     * @param possibleValues values for quantity
     */
    private void populateHashMap(Integer[] possibleValues) {
        for (JButton button : productButtons) {
            JComboBox prodAvail = new JComboBox(possibleValues);
            prodAvail.setSelectedIndex(inventoryManager.getQuantity(button.getText()));
            updateInv.put(button.getText(), prodAvail);
        }
    }

    /**
     * Turn the button off if the machine runs out of quantity for the products
     */
    private void checkProducts() {
        for (JButton prodButton : productButtons) {
            if (inventoryManager.checkProductAvailability(prodButton.getText())) {
                prodButton.setEnabled(true);
            } else {
                prodButton.setEnabled(false);
            }
        }
    }

    /**
     * Action listener for the withdraw money button
     */
    private class withdrawMoneyListen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            moneyInMachine = 0;
            currentMoneyLabel.setText(String.format("<html>Current Available Money in Machine:<b>$%.2f</html>", moneyInMachine));
        }
    }

    /**
     * Listener for the update inventory button
     */
    private class updateInventoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (String prod : updateInv.keySet()) {
                inventoryManager.updateProductAvailability(prod, (int)updateInv.get(prod).getSelectedItem());
                checkProducts();
            }
        }
    }

    /**
     * Listener for the available coins buttons
     */
    private class moneyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String value = e.getActionCommand();
            switch (value) {
                case "$0.01":
                    addToBalance(0.01);
                    break;
                case "$0.05":
                    addToBalance(0.05);
                    break;
                case "$0.10":
                    addToBalance(0.10);
                    break;
                case "$0.25":
                    addToBalance(0.25);
                    break;
                default:
                    addToBalance(0);
                    break;
            }
        }
    }

    /**
     * Listener for the buttons for the products - it updates the price label
     */
    private class productListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productSelected = e.getActionCommand();
            double price = inventoryManager.getProductPrice(productSelected);
            //balance = price >= balance ? balance - price : balance;
            updatePriceLabel(price);
        }
    }

    /**
     * Listener for the vend button
     * It checks availability of the product, balance, and price, and returns appropriately
     */
    private class vendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (price <= balance && balance > 0.0 && inventoryManager.vendProduct(productSelected)) {
                change = balance - price;
                balance = 0;
                moneyInMachine += price;
                changeLabel.setText(String.format("<html><b>Change</b><br>$%.2f</html>", change));
                updatePriceLabel(0);
                updateBalanceLabel();
                checkProducts();
                prodBoughtLabel.setText(String.format("<html><b>Product Bought:</b><br>%s</html>", productSelected));
                productSelected = "";
                messageLabel.setText("Thank you for using my vending machine!");
            } else {
                changeLabel.setText(String.format("<html><b>Change</b><br>$%.2f</html>", 0.0));
                prodBoughtLabel.setText(String.format("<html><b>Product Bought:</b><br>%s</html>", "N/A"));
                messageLabel.setText("<html>Error vending product. Ensure you have enough funds" +
                        "<br>Press \"Cancel\" to cancel this operation or select a different product.</html>");
            }
        }
    }

    /**
     * Listener for the cancel button
     */
    private class cancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            change = balance;
            balance = 0;
            updateBalanceLabel();
            updatePriceLabel(0);
            changeLabel.setText(String.format("<html><b>Change</b><br>$%.2f</html>", change));
            prodBoughtLabel.setText(String.format("<html><b>Product Bought:</b><br>%s</html>", "N/A"));
            productSelected = "";
            messageLabel.setText("Thank you, come again!");
        }
    }

    /**
     * Listener for the admin button
     */
    private class adminListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            createAdminPanel();
        }
    }

    /**
     * Listener for the login button to determine if user can login
     */
    private class adminLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selection = e.getActionCommand();
            boolean isValid = adminControl.validateCreds(username.getText(), password.getPassword());
            if (selection.equals("Login")) {
                if (username.getText().equals("") || password.getPassword().length == 0 || !isValid) {
                    loginLabel.setText("Error logging in");
                    loginLabel.setForeground(Color.RED);
                } else {
                    loginFrame.dispose();
                    adminOperations();
                }
            } else {
                loginFrame.dispose();
            }
        }
    }
}
