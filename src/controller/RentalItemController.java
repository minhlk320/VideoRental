package controller;

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
    private LateChargeDAO lateChargeDAO;
    @FXML
    private JFXTextField jfxTextField;
    @FXML
    private Text textCustomerName;
    @FXML
    private Text textCustomerPhone;
    @FXML
    private Text textCustomerAddress;
    @FXML
    private Text textCustomerJoinedDate;
    @FXML
    private JFXTextField tfitemID;
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
    private Text textRentalTotal;
    @FXML
    private Text text_AmountDue;
    @FXML
    private Button btnDone;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnResetTable;
    @FXML
    private Button btnEnterCustomerID;
    @FXML
    private Button btnEnterItemID;

    private ArrayList<Item> listItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        itemDAO = main.getItemDAO();
        customerDAO = main.getCustomerDAO();
        rentalDAO = main.getRentalDAO();
        lateChargeDAO = main.getLateChargeDAO();
        btnEnterCustomerID.setOnAction(e -> {
            String customerID = jfxTextField.getText();
            if (customerID.isEmpty()) {
                jfxTextField.requestFocus();
                return;
            }
            Customer customer = findCustomer(customerID);
            if (customer != null) {
                currentCustomer = customer;
            }
            updateCustomerInfo();
        });
        btnEnterItemID.setOnAction(e -> {
            String itemID = tfitemID.getText();
            if (itemID.isEmpty()) {
                tfitemID.requestFocus();
                return;
            }
            Item item = findItem(itemID);
            if (item != null) {
                addToTable(item);
                updateTotal();
            }
        });

        btnDone.setOnAction(e -> {
            requestConfirm();
        });
        btnCancel.setOnAction(e -> {
            //no idea
            main.closeCenter();
        });
        initTable();
    }

    private void updateCustomerInfo() {
        if (currentCustomer != null) {
            textCustomerName.setText(currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
            textCustomerPhone.setText(currentCustomer.getPhoneNumber());
            textCustomerAddress.setText(currentCustomer.getAddress());
            textCustomerJoinedDate.setText(currentCustomer.getAddress());
        } else {
            textCustomerName.setText("");
            textCustomerPhone.setText("");
            textCustomerAddress.setText("");
            textCustomerJoinedDate.setText("");
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
        colTitle.setCellValueFactory(cdata -> new SimpleStringProperty(cdata.getValue().getTitle().getTitleName()));
        colPrice.setCellValueFactory(cdata -> new SimpleStringProperty(String.format("%2.2f", cdata.getValue().getTitle().getItemClass().getRentalRate())));
        tableItemList.setItems(items);
        btnResetTable.setOnAction(e -> {
            clearForm();
        });

    }

    private void clearForm() {
        jfxTextField.clear();
        tfitemID.clear();
        listItems.clear();
        tableItemList.getItems().clear();
    }

    private void requestConfirm() {
        //Validate
        if (currentCustomer == null) {
            jfxTextField.requestFocus();
            return;
        }
        if (listItems.isEmpty()) {
            tfitemID.requestFocus();
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
        List<LateCharge> lateCharge = lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        if (!lateCharge.isEmpty()) {
            //Check Late Charge
        }
        alert.getButtonTypes().add(payLateCharge);
        alert.setContentText(String.format("Customer owes fee %s", currentCustomer.getCustomerID()));
        //Show alert
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == makePayement) {
            saveRentalDetail(currentCustomer, listItems);
            currentCustomer = null;
            clearForm();
            updateCustomerInfo();

        }
        if (option.get() == payLateCharge) {
            main.displayLateCharge(currentCustomer);
        }
        if (option.get() == cancel) {
            //Do nothing
        }
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
        textRentalTotal.setText(String.format("%2.2f", getRentalTotal()));
        text_AmountDue.setText(String.format("%2.2f", getAmountDue()));
    }


    private void addToTable(Item item) {
        //Add Item to TableView
        if (item == null) return;
        if (listItems.contains(item)) {
            return;
        }
        tfitemID.clear();
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
