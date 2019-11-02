package controller;

import entities.Customer;
import javafx.fxml.Initializable;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class LateChargeInfoController implements Initializable {
    private Main main;
    private Customer currentCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        currentCustomer = main.getMainController().getCurrentCustomer();
        if (currentCustomer != null) {
            System.out.println("Hello " + currentCustomer.getCustomerID());
        }
    }
}
