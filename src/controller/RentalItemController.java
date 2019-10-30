package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Customer;
import entities.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentalItemController implements Initializable {
    private Main main;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXTextField tf_CustomerID;
    @FXML
    private Text text_CustomerName;
    @FXML
    private Text text_CustomerPhone;
    @FXML
    private Text text_CustomerAddress;
    @FXML
    private Text text_CustomerJoinedDate;
    @FXML
    private JFXTextField tf_itemID;
    @FXML
    private TableView tableItemList;
    @FXML
    private Text text_RentalTotal;
    @FXML
    private Text text_LateCharge;
    @FXML
    private Text text_AmountDue;
    @FXML
    private Button btn_Done;
    @FXML
    private Button btn_Cancel;
    @FXML
    private Button btn_EnterCustomerID;
    @FXML
    private Button btn_EnterItemID;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        btn_EnterCustomerID.setOnAction(e -> {
            String customerID = tf_CustomerID.getText();
            if (customerID.isEmpty()) {
                tf_CustomerID.requestFocus();
                return;
            }
            Customer customer = findCustomer();
            if (customer != null) {
                text_CustomerName.setText(customer.getFirstName() + customer.getLastName());
                text_CustomerPhone.setText(customer.getPhoneNumber());
                text_CustomerAddress.setText(customer.getAddress());
                text_CustomerJoinedDate.setText(customer.getAddress());
            }
        });
        btn_EnterItemID.setOnAction(e -> {
            String itemID = tf_itemID.getText();
            if (itemID.isEmpty()) {
                tf_itemID.requestFocus();
                return;
            }
            Item item = findItem();
            if (item != null) {
                addToTable(item);
                updateAmountDue();
            }
        });
        btn_Done.setOnAction(e -> {
            checkInfo();
            checkLateCharge(tf_CustomerID.getText());
        });
        btn_Cancel.setOnAction(e -> {
            if (requestConfirmExit()) {
                main.changeScene(main.SCENE_HOME);
            }
        });
    }

    private boolean requestConfirmExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to exit ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            return false;
        }
        if (option.get() == ButtonType.OK) {
            return true;
        }
        if (option.get() == ButtonType.CANCEL) {
            return false;
        }
        return false;
    }

    private void checkLateCharge(String customerID) {
        //Call late charge function
        if (customerID.isEmpty()) {
            tf_CustomerID.requestFocus();
            return;
        }
        if (hasLateCharge(customerID)) {
            main.newWindow(main.SCENE_LATE_CHARGE_INFO, main.TITLE_LATE_CHARGE_INFO);
        }
    }

    private boolean hasLateCharge(String customerID) {
        return true;
    }

    private void checkInfo() {
        //Check list Empty or Customer not entered
    }


    private void updateAmountDue() {
        //Update Amount Due
    }

    private void addToTable(Item item) {
    }

    private Item findItem() {
        return null;
    }

    private Customer findCustomer() {
        return null;
    }
}
