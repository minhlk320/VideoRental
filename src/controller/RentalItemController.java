package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entities.Customer;
import entities.Item;
import entities.Rate;
import entities.Title;
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

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
        btn_EnterCustomerID.setOnAction(e -> {
            String customerID = tf_CustomerID.getText();
            if (customerID.isEmpty()) {
                tf_CustomerID.requestFocus();
                return;
            }
            Customer customer = findCustomer(customerID);
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
            Item item = findItem(itemID);
            if (item != null) {
                addToTable(item);
                updateTotal();
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
        colNo.setCellValueFactory(
                param -> {
                    // TODO Auto-generated method stub
                    return new ReadOnlyObjectWrapper(param.getValue());
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
        colPrice.setCellValueFactory(celldata -> new SimpleStringProperty(String.format("%2.2f", celldata.getValue().getItemClass().getRentalRate())));
        tableItemList.setItems(items);
        btnResetTable.setOnAction(e -> {
            listItems.clear();
            tableItemList.getItems().clear();
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

    private double getRentalTotal() {
        double rental_Total = 0;
        for (Item item : listItems) {
            rental_Total += item.getItemClass().getRentalRate();
        }
        return rental_Total;
    }

    private double getLateCharge() {
        return 0f;
    }

    private double getAmountDue() {
        if (getLateCharge() == 0) {
            return getRentalTotal();
        }
        return getRentalTotal() + getLateCharge();
    }

    public void updateTotal() {
        text_RentalTotal.setText(String.format("%2.2f", getRentalTotal()));
        text_LateCharge.setText(String.format("%2.2f", getLateCharge()));
        text_AmountDue.setText(String.format("%2.2f", getAmountDue()));
    }


    private void addToTable(Item item) {
        //Add Item to TableView
        //System.out.println(item);
        listItems.add(item);
        tableItemList.getItems().setAll(listItems);
        tableItemList.refresh();
    }

    private Item findItem(String itemID) {
        Title title = new Title("WRECK IT RAPTH", "A cartoon movie has been announced in 2018", new File("D:\\XDPM\\misc\\Images\\wreckIt.jpg"));
        Rate rate = new Rate("a", 123, 123, 123);
        Item item = new Item(title, 0, rate);
        item.setItemID("123");
        return item;
    }

    private Customer findCustomer(String customerID) {
        return new Customer("Tran", "Gia Bao", "49 Le Loi", "0123456789", LocalDate.now(), true);
    }
}
