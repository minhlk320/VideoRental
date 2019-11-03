package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.CustomerDAO;
import daos.ItemDAO;
import daos.LateChargeDAO;
import daos.RentalDAO;
import entities.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ui.Main;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentalItemController implements Initializable {


    private Main main;
    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private RentalDAO rentalDAO;
    private Customer currentCustomer;
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
    private TableView<Item> tableItemList;
    @FXML
    private TableColumn<Item, String> colItemID;
    @FXML
    private TableColumn<Item, String> colTitle;
    @FXML
    private TableColumn<Item, String> colPrice;
    @FXML
    private TableColumn<Item, Item> colNo;
    @FXML
    private TableColumn<Item, Item> colDeleteButton;
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
    private Button btnResetTable;
    @FXML
    private Button btn_EnterCustomerID;
    @FXML
    private Button btn_EnterItemID;
    private ArrayList<Item> listItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        itemDAO = new ItemDAO();
        customerDAO = new CustomerDAO();
        rentalDAO = new RentalDAO();
        btn_EnterCustomerID.setOnAction(e -> {
            String customerID = tf_CustomerID.getText();
            if (customerID.isEmpty()) {
                tf_CustomerID.requestFocus();
                return;
            }
            Customer customer = findCustomer(customerID);
            if (customer != null) {
                currentCustomer = customer;
            }
            updateCustomerInfo();
        });
        btn_EnterItemID.setOnAction(e -> {
            String itemID = tf_itemID.getText();
            if (itemID.isEmpty()) {
                tf_itemID.requestFocus();
                return;
            }
            Item item = findItem(itemID);
            if (item != null) {
                addToTable(item);
                updateTotal();
            }
        });

        btn_Done.setOnAction(e -> {
            requestConfirm();
        });
        btn_Cancel.setOnAction(e -> {
            //no idea
            main.closeCenter();
        });
        initTable();
    }

    private void updateCustomerInfo() {
        if (currentCustomer != null) {
            text_CustomerName.setText(currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
            text_CustomerPhone.setText(currentCustomer.getPhoneNumber());
            text_CustomerAddress.setText(currentCustomer.getAddress());
            text_CustomerJoinedDate.setText(currentCustomer.getAddress());
        } else {
            text_CustomerName.setText("");
            text_CustomerPhone.setText("");
            text_CustomerAddress.setText("");
            text_CustomerJoinedDate.setText("");
        }

    }

    private void initTable() {
        colNo.setCellValueFactory(
                param -> {
                    // TODO Auto-generated method stub
                    return new ReadOnlyObjectWrapper<>(param.getValue());
                });
        colNo.setCellFactory(new Callback<TableColumn<Item, Item>, TableCell<Item, Item>>() {

            @Override
            public TableCell<Item, Item> call(TableColumn<Item, Item> param) {
                // TODO Auto-generated method stub
                return new TableCell<Item, Item>() {
                    @Override
                    protected void updateItem(Item arg0, boolean arg1) {
                        // TODO Auto-generated method stub
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
        colDeleteButton.setSortable(false);
        colDeleteButton.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colDeleteButton.setCellFactory(param -> new TableCell<Item, Item>() {
            private final Button deleteButton = new Button("X");

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            listItems.remove(item);
                            getTableView().getItems().remove(item);
                        }
                );
            }
        });
        listItems = new ArrayList<>();
        ObservableList<Item> items = FXCollections.observableArrayList(listItems);
        colNo.setSortable(false);
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colTitle.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
        colPrice.setCellValueFactory(celldata -> new SimpleStringProperty(String.format("%2.2f", celldata.getValue().getTitle().getItemClass().getRentalRate())));
        tableItemList.setItems(items);
        btnResetTable.setOnAction(e -> {
            clearForm();
        });

    }

    private void clearForm() {
        tf_CustomerID.clear();
        tf_itemID.clear();
        listItems.clear();
        tableItemList.getItems().clear();
    }

    private void requestConfirm() {
        //Validate
        if (currentCustomer == null) {
            tf_CustomerID.requestFocus();
            return;
        }
        if (listItems.isEmpty()) {
            tf_itemID.requestFocus();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm rental transaction");
        alert.setContentText("CustomerID :" + currentCustomer.getCustomerID());
        //Customer button
        ButtonType payLateCharge = new ButtonType("Pay late charge");
        ButtonType makePayement = new ButtonType("Make payment");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(makePayement, cancel);
        LateChargeDAO lateChargeDAO = new LateChargeDAO();
        List<LateCharge> lateCharge = lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        if (!lateCharge.isEmpty()) {

        }
        alert.getButtonTypes().add(payLateCharge);
        alert.setContentText(String.format("Customer owes fee %s", currentCustomer.getCustomerID()));
        //Show alert
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) {
            //Do nothing
            return;
        }
        if (option.get() == makePayement) {
            saveRentalDetail(currentCustomer, listItems);
            currentCustomer = null;
            clearForm();
            updateCustomerInfo();

        }
        if (option.get() == payLateCharge) {
            System.out.println(currentCustomer);
            main.displayLateCharge(currentCustomer);
            return;
        }
        if (option.get() == cancel) {
            //Do nothing
            return;
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    private double getRentalTotal() {
        double rental_Total = 0;
        for (Item item : listItems) {
            rental_Total += item.getTitle().getItemClass().getRentalRate();
        }
        return rental_Total;
    }

    private double getAmountDue() {
        return getRentalTotal();
    }

    public void updateTotal() {
        text_RentalTotal.setText(String.format("%2.2f", getRentalTotal()));
        text_AmountDue.setText(String.format("%2.2f", getAmountDue()));
    }


    private void addToTable(Item item) {
        //Add Item to TableView
        if (item == null) return;
        if (listItems.contains(item)) {
            return;
        }
        tf_itemID.clear();
        listItems.add(item);
        tableItemList.getItems().setAll(listItems);
        tableItemList.refresh();
    }

    private Item findItem(String itemID) {
        //Find Item from Database
        Item item = itemDAO.getById(Item.class, itemID);
        return item;
    }

    private Customer findCustomer(String customerID) {
        //Find Customer from Database
        Customer customer = customerDAO.getById(Customer.class, customerID);
        return customer;
    }

    public boolean saveRentalDetail(Customer customer, ArrayList<Item> itemList) {
        Rental rental = new Rental(LocalDate.now());
        ArrayList<RentalDetail> rentalDetailList = new ArrayList<>();
        itemList.forEach(x -> {
            x.setStatus(Item.RENTED);
            rentalDetailList.add(new RentalDetail(rental, x, x.getTitle().getItemClass().getRentalRate(), x.getTitle().getItemClass().getRentalPeriod(), x.getTitle().getItemClass().getLateRate()));
        });
        rental.setCustomer(customer);
        rental.setItems(rentalDetailList);
        if (rentalDAO.save(rental))
            return true;
        return false;
    }
}
