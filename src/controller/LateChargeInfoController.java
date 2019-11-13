package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.CustomerDAO;
import daos.LateChargeDAO;
import entities.Customer;
import entities.LateCharge;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import ui.Main;

import java.net.URL;
import java.time.LocalDate;
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
    private List<LateCharge> lateChargeList;
    private List<LateCharge> lateChargeChosenList;
    @FXML
    private TableColumn<LateCharge, LateCharge> colNo;
    @FXML
    private TableColumn<LateCharge, String> colReturnDate;
    @FXML
    private TableColumn<LateCharge, String> colDueOn;
    @FXML
    private TableColumn<LateCharge, String> colTitleName;
    @FXML
    private TableColumn<LateCharge, String> colPurchaseDate;
    @FXML
    private TableColumn<LateCharge, String> colTotalAmount;
    @FXML
    private TableColumn<LateCharge, LateCharge> colCheckBox;
    @FXML
    private Text textCustomerName;
    @FXML
    private Text textCustomerAddress;
    @FXML
    private Text textCustomerPhone;
    @FXML
    private Text textCustomerJoinedDate;
    @FXML
    private Text textTotalCharge;
    @FXML
    private JFXButton btnPaid;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnCancelLateCharge;
    @FXML
    private JFXTextField tfCustomerID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();

        customerDAO = main.getCustomerDAO();
        lateChargeDAO = main.getLateChargeDAO();
        lateChargeList = new ArrayList<>();
        lateChargeChosenList = new ArrayList<>();
        initTable(lateChargeList);
        clearForm();
        if (currentCustomer == null) {
            tfCustomerID.requestFocus();
        } else {
            tfCustomerID.setText(currentCustomer.getCustomerID());
            loadCustomer();
        }
        if (main.isLogged) {
            btnCancelLateCharge.setDisable(false);
        } else {
            btnCancelLateCharge.setDisable(true);
        }
        //Button
        btnEnter.setOnAction(e -> {
            loadCustomer();
        });
        //Listener for Enter Key
        tfCustomerID.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loadCustomer();
            }
        });
        btnCancelLateCharge.setOnAction(e->{
            if (table.getSelectionModel().getSelectedItem()==null)
            {
                main.showMessage("Choose a late charge to procced","Message",null);
                return;
            }

            if(main.requestConfirm("Do you want to cancel this Late charge?","Message",null)){
                LateCharge lateCharge = (LateCharge) table.getSelectionModel().getSelectedItem();
                lateChargeDAO.delete(lateCharge);
                main.showMessage("Deleted!","Message",null);
            }
        });
        btnCancel.setOnAction(e -> {
            if (main.requestConfirm("Do you want to exit?", "Message", null)) {
                Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
                stage.close();
            }
        });
        btnPaid.setOnAction(e -> {
            if (lateChargeChosenList.isEmpty()) {
                main.showMessage("You haven't chosen any late charge to proceed, please choose at least one item to proceed!", "Message", null);
                return;
            }
            String message = "Total Payment is $" + lateChargeDAO.getTotalLatechargePayment(lateChargeChosenList);
            if (main.requestConfirm(message, "Message", null)) {
                for (LateCharge lc : lateChargeChosenList) {
                    lc.setPurchaseDate(LocalDate.now());
                    lateChargeDAO.update(lc);
                }
                lateChargeList.removeAll(lateChargeChosenList);
                lateChargeChosenList.clear();
            }
        });
    }

    private void loadCustomer() {
        clearForm();
        String id = tfCustomerID.getText();
        if (id.isEmpty()) {
            tfCustomerID.requestFocus();
            return;
        }
        Customer customer = findCustomer(id);
        if (customer == null) {
            tfCustomerID.clear();
            tfCustomerID.requestFocus();
            main.showMessage("Customer not found, please try again!", "Message", null);
            return;
        }
        loadCustomerInfo(customer);
        reloadTable();
        if (lateChargeList.isEmpty()) {
            main.showMessage(String.format("Customer %s does't have any unpaid late charge!", customer.getCustomerID()), "Message", null);
            tfCustomerID.clear();
        }
    }
    private void loadCustomerInfo(Customer customer) {
        textCustomerName.setText(customer.getFirstName() + " " + customer.getLastName());
        textCustomerAddress.setText(customer.getAddress());
        textCustomerPhone.setText(customer.getPhoneNumber());
        textCustomerJoinedDate.setText(customer.getJoinedDate().toString());
        currentCustomer = customer;
    }


    private void reloadTable() {
        lateChargeList = lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        table.getItems().clear();
        table.getItems().addAll(lateChargeList);
    }
    private void initTable(List<LateCharge> list) {
        ObservableList<LateCharge> tkList = FXCollections.observableArrayList(list);
        colNo.setCellValueFactory(param -> {
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
        colPurchaseDate.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getPurchaseDate() == null ? "" : celldata.getValue().getPurchaseDate().toString()));
        colTitleName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getItem().getTitleName()));
        colDueOn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getDueOn().toString()));
        colReturnDate.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getReturnDate().toString()));
        colTotalAmount.setCellValueFactory(celldata -> new SimpleStringProperty("$" + String.valueOf(celldata.getValue().getTotalAmount())));
        colCheckBox.setSortable(false);
        colCheckBox.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colCheckBox.setCellFactory(param -> new TableCell<LateCharge, LateCharge>() {
            private final CheckBox btnCheck = new CheckBox();

            @Override
            protected void updateItem(LateCharge item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(btnCheck);
                btnCheck.setOnAction(e -> {
                    if (btnCheck.isSelected()) {
                        lateChargeChosenList.add(item);
                        loadTotalAmountPayment();
                    } else {
                        lateChargeChosenList.remove(item);
                        loadTotalAmountPayment();
                    }
                });
            }
        });
        table.setItems(tkList);
    }

    /**
     * call from Main function
     *
     * @param currentCustomer
     */
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    private Customer findCustomer(String id) {
        Customer customer = customerDAO.getById(Customer.class, id);
        return customer;
    }

    private void loadTotalAmountPayment() {
        double total = lateChargeDAO.getTotalLatechargePayment(lateChargeChosenList);
        textTotalCharge.setText("$" + total);
    }
    private void clearForm() {
        textCustomerName.setText("");
        textCustomerAddress.setText("");
        textCustomerPhone.setText("");
        textCustomerJoinedDate.setText("");
        textTotalCharge.setText("$0");
        lateChargeList.clear();
        lateChargeChosenList.clear();
        table.getItems().clear();
    }
}
