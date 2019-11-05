package controller;

import com.jfoenix.controls.JFXButton;
import daos.CustomerDAO;
import entities.Customer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ui.Main;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerManagementController implements Initializable{


    @FXML
    private TextField txtCustomerID;

    @FXML
    private TextField txtFName;

    @FXML
    private TextField txtLName;

    @FXML
    private TextField txtFone;

    @FXML
    private TextField txtAddress;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<Customer> table;

    @FXML
    private TableColumn<Customer, String> colCustomerID;

    @FXML
    private TableColumn<Customer, String> colFName;

    @FXML
    private TableColumn<Customer, String> colLName;

    @FXML
    private TableColumn<Customer, String> colGender;

    @FXML
    private TableColumn<Customer, String> colFone;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private CheckBox cboxMale;

    @FXML
    private JFXButton btnLogin;

    private List<Customer> listCus;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main main = Main.getInstance();
        listCus = new CustomerDAO().getAll(Customer.class);
        loadTable(listCus);

        btnBack.setOnAction(e->{
            main.changeScene(main.SCENE_HOME);
        });

        btnNew.setOnAction(e->{
            txtFName.clear();
            txtLName.clear();
            txtFone.clear();
            txtAddress.clear();
            cboxMale.setSelected(false);
            if (!listCus.isEmpty()) {
                String id_last = listCus.get(listCus.size() - 1).getCustomerID();
                int id = Integer.valueOf((id_last));
                String new_id = String.format("%06d", id + 1);
                txtCustomerID.setText(new_id);
            } else {
                txtCustomerID.setText("000001");
            }

        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Customer customer = null;
                CustomerDAO customerDAO = new CustomerDAO();
                String id = txtCustomerID.getText();
                if(customerDAO.getById(Customer.class,id)!= null){
                    customer = customerDAO.getById(Customer.class,id);
                    customerDAO.update(getCurrentCustomer(customer));
                }else{
                    customer = new Customer();
                    if(getCurrentCustomer(customer)!=null)
                        customerDAO.save(getCurrentCustomer(customer));
                }
                reloadTable();
            }

            private  Customer getCurrentCustomer(Customer customer) {
                String fname = txtFName.getText();
                String lname= txtLName.getText();
                String fone= txtFone.getText();
                String address= txtAddress.getText();
                LocalDate joineddate= LocalDate.now();
                boolean gender = false;
                if (cboxMale.isSelected()){
                    gender = true;
                }else {
                    gender = false;
                }

                if(validate()){
                    customer.setFirstName(fname);
                    customer.setLastName(lname);
                    customer.setPhoneNumber(fone);
                    customer.setAddress(address);
                    customer.setJoinedDate(joineddate);
                    customer.setGender(gender);
                    return customer;
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message !");
                    alert.setHeaderText(null);
                    alert.setContentText("Complete all information please !");
                    alert.showAndWait();
                    return null;
                }
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CustomerDAO customerDAO= new CustomerDAO();
                Customer customer = table.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message !");
                alert.setHeaderText("Are you sure ?");
                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeCancel = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                switch (result.get().getText()) {
                    case "Yes":
                        boolean x = customerDAO.delete(customer);
                        alert.setContentText("Deleted !");
                        break;
                    default:
                        alert.close();
                        break;
                }
                reloadTable();
            }
        });

        table.setOnMousePressed(e->{
            if(table.getSelectionModel().getSelectedItem()==null)
                return;
            if(e.isPrimaryButtonDown() && e.getClickCount()==1) {
                txtCustomerID.setText(table.getSelectionModel().getSelectedItem().getCustomerID());
                txtFName.setText(table.getSelectionModel().getSelectedItem().getFirstName());
                txtLName.setText(table.getSelectionModel().getSelectedItem().getLastName());
                txtFone.setText(table.getSelectionModel().getSelectedItem().getPhoneNumber());
                txtAddress.setText(table.getSelectionModel().getSelectedItem().getAddress());
                cboxMale.setSelected(table.getSelectionModel().getSelectedItem().isGender());
            }
        });

        txtFone.setOnKeyTyped(e->{
            String fone = e.getCharacter();
            try{
                if (Character.isDigit(Integer.parseInt(fone))) {
                    e.consume();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });


    }

    private void loadTable(List<Customer> list) {
        ObservableList<Customer> tkList = FXCollections.observableArrayList(list);
        for(int i = 0;i<tkList.size();i++){
            colCustomerID.setSortable(false);
            colCustomerID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getCustomerID()));
            colFName.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getFirstName()));
            colLName.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getLastName()));
            colFone.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getPhoneNumber()));
            colAddress.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getAddress()));
            colGender.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().isGender()==true?"Male":"Female"));
        }
        table.setItems(tkList);
    }

    private void reloadTable(){
        table.getColumns().clear();
        table.getColumns().addAll(colCustomerID,colFName,colLName,colFone,colAddress,colGender);
        listCus = new CustomerDAO().getAll(Customer.class);
        loadTable(listCus);
    }

    private boolean validate(){
        if(txtFName.getText().trim().equals("")||txtLName.getText().trim().equals("")||txtAddress.getText().trim().equals("")||txtFone.getText().trim().equals(""))
            return false;
        return true;
    }
}
