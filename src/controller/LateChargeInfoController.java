package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.CustomerDAO;
import daos.LateChargeDAO;
import entities.Customer;
import entities.LateCharge;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LateChargeInfoController implements Initializable {

    @FXML
    private TableView table;
    private Main main;
    private Customer currentCustomer;
    private CustomerDAO customerDAO;
    private LateChargeDAO lateChargeDAO;
    private List<LateCharge> list;
    @FXML
    private TableColumn<LateCharge, LateCharge> colNo;
    @FXML
    private TableColumn colReturnDate;
    @FXML
    private TableColumn colDays;
    @FXML
    private TableColumn colLateCharge;
    @FXML
    private TableColumn colCheck;
    @FXML
    private Text textTotalCharge;
    @FXML
    private JFXButton btnPaid;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Button btnEnter;
    @FXML
    private JFXTextField tfCustomerID;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        customerDAO = main.getCustomerDAO();
        lateChargeDAO = main.getLateChargeDAO();
        list = new ArrayList<>();
        initTable();
        if (currentCustomer != null) {
            tfCustomerID.setText(currentCustomer.getCustomerID());
        } else {
            tfCustomerID.requestFocus();
        }
        btnEnter.setOnAction(e -> {
            String id = tfCustomerID.getText().trim();
            Customer customer = customerDAO.getById(Customer.class, id);
            if (customer != null) {
                setCurrentCustomer(customer);
            } else {
                //Error!!! can't find customer id
            }

        });
    }

    private void initTable() {
        colNo.setCellValueFactory(
                param -> {
                    return new ReadOnlyObjectWrapper<>(param.getValue());
                });
        colNo.setCellFactory(new Callback<TableColumn<LateCharge, LateCharge>, TableCell<LateCharge, LateCharge>>() {

            @Override
            public TableCell<LateCharge, LateCharge> call(TableColumn<LateCharge, LateCharge> param) {
                return new TableCell<LateCharge, LateCharge>() {
                    @Override
                    protected void updateItem(LateCharge arg0, boolean arg1) {
                        super.updateItem(arg0, arg1);
                        if (this.getTableRow() != null && arg0 != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        if (currentCustomer != null) {
            list = lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        } else {

        }
    }
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
}
