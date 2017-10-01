import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VendorFrame {

    private AdminController adminControl = new AdminController();
    private Inventory inventoryManager = new Inventory();
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

    private JPanel messageArea() {
        JPanel message = new JPanel();
        message.setLayout(new GridLayout(0,2,5,5));
        JPanel changePanel = new JPanel();
        changePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,5));
        changePanel.setLayout(new GridLayout(0,2));
        changeLabel = new JLabel("<html><b>Change</b><br>$0.00</html>");
        changePanel.add(changeLabel);
        prodBoughtLabel = new JLabel("<html><b>Product Bought:</b><br>N/A</html>");
        changePanel.add(prodBoughtLabel);
        message.add(changePanel);
        messageLabel = new JLabel("Insert Coins to Purchase Product",SwingConstants.CENTER);
        message.add(messageLabel);
        return message;
    }

    private void addToBalance(double aBalance) {
        balance += aBalance;
        String out = String.format("<html><b>Balance</b><br>$%.2f</html>", balance);
        balanceLabel.setText(out);
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("<html><b>Balance</b><br>$%.2f</html>", balance));
    }

    private void updatePriceLabel(double aPrice) {
        price = aPrice;
        String out = String.format("<html><b>Price</b><br>$%.2f</html>", price);
        priceLabel.setText(out);
    }

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

    private class productListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productSelected = e.getActionCommand();
            double price = inventoryManager.getProductPrice(productSelected);
            //balance = price >= balance ? balance - price : balance;
            updatePriceLabel(price);
        }
    }

    private class vendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (price <= balance && balance > 0.0 && inventoryManager.vendProduct(productSelected)) {
                change = balance - price;
                balance -= price;
                moneyInMachine += price;
                changeLabel.setText(String.format("<html><b>Change</b><br>$%.2f</html>", change));
                updatePriceLabel(0);
                updateBalanceLabel();
                prodBoughtLabel.setText(String.format("<html><b>Product Bought:</b><br>%s</html>", productSelected));
                productSelected = "";
                messageLabel.setText("Thank you for using my vending machine!");
            } else {
                changeLabel.setText(String.format("<html><b>Change</b><br>$%.2f</html>", 0.0));
                prodBoughtLabel.setText(String.format("<html><b>Product Bought:</b><br>%s</html>", "N/A"));
                messageLabel.setText("<html>Error vending product. If you have enough funds and the error persists, there may not be enough \"" + productSelected + "\" in the machine." +
                        "<br>Press \"Cancel\" to cancel this operation or select a different product.</html>");
            }
        }
    }

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

    private class adminListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            createAdminPanel();
        }
    }

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
                    //System.out.println(adminControl.validateCreds(username.getText(), password.getPassword()));
                }
            } else {
                loginFrame.dispose();
            }
        }
    }
}
