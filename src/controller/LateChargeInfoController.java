package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Customer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class LateChargeInfoController implements Initializable {


    private Main main;
    private Customer currentCustomer;
    @FXML
    private TableView table;
    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colReturnDate;
    @FXML
    private TableColumn colDays;
    @FXML
    private TableColumn colLateCharge;
    @FXML
    private TableColumn colCheck;
    @FXML
    private Text text_TotalCharge;
    @FXML
    private JFXButton btnPaid;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private JFXTextField btnCustomerID;
    @FXML
    private Button btnEnter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        if (currentCustomer != null) {
            System.out.println("Hello " + currentCustomer.getCustomerID());
        }
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
}
