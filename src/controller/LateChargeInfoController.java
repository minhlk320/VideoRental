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
import java.util.Optional;
import java.util.ResourceBundle;

public class LateChargeInfoController implements Initializable {
    @FXML
    private TableView table;
    private Main main;
    private Customer currentCustomer;
    private CustomerDAO customerDAO;
    private LateChargeDAO lateChargeDAO;
    private List<LateCharge> lateChargeList;
    private List<LateCharge> lateChargeChosenList = new ArrayList<>();
    @FXML
    private TableColumn<LateCharge, LateCharge> colNo;
    @FXML
    private TableColumn<LateCharge,String> colReturnDate;
    @FXML
    private TableColumn<LateCharge,String> colDueOn;
    @FXML
    private TableColumn<LateCharge,String> colTitleName;
    @FXML
    private TableColumn<LateCharge,String> colPurchaseDate;
    @FXML
    private TableColumn<LateCharge,String> colTotalAmount;
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
    private JFXTextField tfCustomerID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clear();
        main = Main.getInstance();
        customerDAO = main.getCustomerDAO();
        lateChargeDAO = main.getLateChargeDAO();
        lateChargeList = new ArrayList<>();
        if (currentCustomer != null) {
            tfCustomerID.setText(currentCustomer.getCustomerID());
            loadCustomerInfo();
        } else {
            tfCustomerID.requestFocus();
        }
        btnEnter.setOnAction(e -> {
            findCustomer();
        });
        tfCustomerID.setOnKeyReleased(e->{
            if(e.getCode()== KeyCode.ENTER)
                findCustomer();
        });
        btnCancel.setOnAction(e->{
            if(requestConfirm("Do you want to exit?","Message",null)){
                Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
                stage.close();
            }
        });
        btnPaid.setOnAction(e->{
            if(lateChargeChosenList.isEmpty())
                showMessage("You haven't chosen any late charge to proceed, please choose at least one item to proceed!","Message",null);
           else{
                String message = "Total Payment is $" + this.lateChargeDAO.getTotalLatechargePayment(lateChargeChosenList);
                if(requestConfirm(message,"Message",null)){
                        for(LateCharge lc : lateChargeChosenList){
                            lc.setPurchaseDate(LocalDate.now());
                            this.lateChargeDAO.update(lc);
                        }
                        lateChargeList.removeAll(lateChargeChosenList);
                        loadTable(lateChargeList);
                        lateChargeChosenList.clear();
                }
            }
        });
    }

    private void loadTable(List<LateCharge> list) {
        ObservableList<LateCharge> tkList = FXCollections.observableArrayList(list);
        for(int i = 0; i<tkList.size(); i++) {
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
            colPurchaseDate.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getPurchaseDate()==null?"":celldata.getValue().getPurchaseDate().toString()));
            colTitleName.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItem().getTitleName()));
            colDueOn.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDueOn().toString()));
            colReturnDate.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getReturnDate().toString()));
            colTotalAmount.setCellValueFactory(celldata->new SimpleStringProperty("$" + String.valueOf(celldata.getValue().getTotalAmount())));
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
                    btnCheck.setOnAction(e-> {
                       if(btnCheck.isSelected()){
                           lateChargeChosenList.add(item);
                           loadTotalAmountPayment();
                       }
                       else{
                           lateChargeChosenList.remove(item);
                           loadTotalAmountPayment();
                       }
                    });
                }
            });
        }
        table.setItems(tkList);
    }
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    private void findCustomer(){
        String id = tfCustomerID.getText().trim();
        Customer customer = customerDAO.getById(Customer.class, id);
        if (customer != null) {
            setCurrentCustomer(customer);
            loadCustomerInfo();
            loadLateCharge();
        } else {
            clear();
            showMessage("Customer not found, please try again!","Message",null);
        }
    }
    private void loadCustomerInfo(){
        textCustomerName.setText(currentCustomer.getFirstName() + " " +currentCustomer.getLastName());
        textCustomerAddress.setText(currentCustomer.getAddress());
        textCustomerPhone.setText(currentCustomer.getPhoneNumber());
        textCustomerJoinedDate.setText(currentCustomer.getJoinedDate().toString());
    }
    private void showMessage(String message, String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadLateCharge(){
        if(currentCustomer==null)
            return;
        lateChargeList = this.lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        if(lateChargeList.isEmpty())
            showMessage("Customer does't have any unpaid late charge!","Message",null);
        loadTable(lateChargeList);
    }
    private void loadTotalAmountPayment(){
        double total = this.lateChargeDAO.getTotalLatechargePayment(lateChargeChosenList);
        textTotalCharge.setText("$" + total);
    }
    private boolean requestConfirm(String message, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
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
    private void clear(){
        textCustomerName.setText("");
        textCustomerAddress.setText("");
        textCustomerPhone.setText("");
        textCustomerJoinedDate.setText("");
        textTotalCharge.setText("$0");
    }
}
