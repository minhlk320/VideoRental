package controller;

import com.jfoenix.controls.JFXTextField;
import daos.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import entities.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class RentalItemController implements Initializable {


    private Main main;
    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private RentalDAO rentalDAO;
    private ReservationDAO reservationDAO;



    private Customer currentCustomer;
    private LateChargeDAO lateChargeDAO;
    @FXML
    private JFXTextField tfCustomerID;
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
    private ArrayList<Reservation> reservationList = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        itemDAO = main.getItemDAO();
        customerDAO = main.getCustomerDAO();
        rentalDAO = main.getRentalDAO();
        lateChargeDAO = main.getLateChargeDAO();
        reservationDAO = main.getReservationDAO();
        tfCustomerID.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                resetForm();
                findCustomer();
            }
        });
        btnEnterCustomerID.setOnAction(e->{
            resetForm();
            findCustomer();
        });
        btnEnterItemID.setOnAction(e -> {
            inputItem();
        });
        tfitemID.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                inputItem();
            }
        });

        btnDone.setOnAction(e -> {
            requestConfirm();
        });
        btnCancel.setOnAction(e -> {
            Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            stage.close();
        });
        initTable();
    }

    private void showMessage(String message, String title, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void inputItem() {
        String itemID = tfitemID.getText();
        if (itemID.isEmpty()) {
            showMessage("Item'ID is empty!", "Alert", "Item ID");
            tfitemID.requestFocus();
            return;
        }
        Item item = itemDAO.getById(Item.class,itemID);
        if (item != null) {
            if(item.getStatus().equalsIgnoreCase(Item.ON_HOLD)){
                    Reservation reservation = reservationDAO.getReservationbyItemID(item.getItemID());
                    if(currentCustomer.equals(reservation.getCustomer())&&item.equals(reservation.getItem())){
                        reservationList.add(reservation);
                        addToTable(item);
                        addToTable(item);
                    }
                    else  showMessage("This Item has been placed ON_HOLD to the customer: " + reservation.getCustomer().getCustomerID()+"!","Message",null);
            }else if(item.getStatus().equalsIgnoreCase(Item.ON_SHELF)){
                addToTable(item);
                addToTable(item);
            }
            else{
                showMessage("This Item is not available to be rented!","Message",null);
            }
            updateTotal();
        }
        else
            showMessage("Entered ID not found, please check again!","Message",null);
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
                    return new ReadOnlyObjectWrapper<>(param.getValue());
                });
        colNo.setCellFactory(new Callback<TableColumn<Item, Item>, TableCell<Item, Item>>() {

            @Override
            public TableCell<Item, Item> call(TableColumn<Item, Item> param) {
                return new TableCell<Item, Item>() {
                    @Override
                    protected void updateItem(Item arg0, boolean arg1) {
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
                deleteButton.setOnAction(e->{
                    listItems.remove(item);
                    getTableView().getItems().remove(item);
                    updateTotal();
                    for(Reservation x : reservationList){
                        if(x.getItem().equals(item))
                            reservationList.remove(x);
                    }
                });
            }
        });
        listItems = new ArrayList<>();
        ObservableList<Item> items = FXCollections.observableArrayList(listItems);
        colNo.setSortable(false);
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colTitle.setCellValueFactory(cdata -> new SimpleStringProperty(cdata.getValue().getTitle().getTitleName()));
        colPrice.setCellValueFactory(cdata -> new SimpleStringProperty(String.format("%2.2f", cdata.getValue().getTitle().getItemClass().getRentalRate())));
        tableItemList.setItems(items);
        btnResetTable.setOnAction(e->clearForm());

    }

    private void clearForm() {
        tfCustomerID.clear();
        textRentalTotal.setText("$0");
        text_AmountDue.setText("$0");
        tfitemID.clear();
        listItems.clear();
        tableItemList.getItems().clear();
        currentCustomer=null;
        clearCustomerInfo();
    }
    private void resetForm() {
        tfitemID.clear();
        textRentalTotal.setText("$0");
        text_AmountDue.setText("$0");
        listItems.clear();
        tableItemList.getItems().clear();
        currentCustomer=null;
        clearCustomerInfo();
    }

    private void requestConfirm() {
        //Validate
        if (currentCustomer == null) {
            showMessage("Customer unavailable!", "Alert", "Customer");
            tfCustomerID.requestFocus();
            return;
        }
        if (listItems.isEmpty()) {
            showMessage("List must not be empty, at least one chosen item", "Alert", "List Item");
            tfitemID.requestFocus();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm rental transaction");
        alert.setContentText("CustomerID :" + currentCustomer.getCustomerID());
        ButtonType payLateCharge = new ButtonType("Pay late charge");
        ButtonType makePayement = new ButtonType("Make payment");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(makePayement, cancel);
        List<LateCharge> lateCharge = lateChargeDAO.getLateChargeByCustomerID(currentCustomer.getCustomerID());
        if (!lateCharge.isEmpty()) {
            //Check Late Charge
            alert.getButtonTypes().add(payLateCharge);
            alert.setContentText(String.format("Customer owes fee %s", currentCustomer.getCustomerID()));
        }
        //Show alert
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == makePayement) {
            for(Reservation r : reservationList)
                reservationDAO.delete(r);
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
        if (item == null) return;
        if (listItems.contains(item)) {
            return;
        }
        tfitemID.clear();
        listItems.add(item);
        tableItemList.getItems().setAll(listItems);
        tableItemList.refresh();
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
    private void findCustomer(){
        String id = tfCustomerID.getText().trim();
        Customer customer = customerDAO.getById(Customer.class, id);
        if (customer != null) {
            setCurrentCustomer(customer);
            loadCustomerInfo();
        } else {
            clearCustomerInfo();
            showMessage("Customer not found, please try again!","Message",null);
        }
    }
    private void loadCustomerInfo(){
        textCustomerName.setText(currentCustomer.getFirstName() + " " +currentCustomer.getLastName());
        textCustomerAddress.setText(currentCustomer.getAddress());
        textCustomerPhone.setText(currentCustomer.getPhoneNumber());
        textCustomerJoinedDate.setText(currentCustomer.getJoinedDate().toString());
    }
    private void clearCustomerInfo(){
        textCustomerName.setText("");
        textCustomerAddress.setText("");
        textCustomerPhone.setText("");
        textCustomerJoinedDate.setText("");
        text_AmountDue.setText("$0");
    }
    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
}
