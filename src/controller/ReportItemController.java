package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.CustomerDAO;
import daos.ItemDAO;
import daos.RentalDAO;
import daos.ReservationDAO;
import entities.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReportItemController implements Initializable {

    @FXML
    private JFXTextField tfItemID;

    @FXML
    private Label lbTitle;

    @FXML
    private Label lbStatus;

    @FXML
    private ImageView imgTitle;

    @FXML
    private Label lbstatusDetail;

    @FXML
    private JFXButton btnEnter;


    @FXML
    private Text customerInfo;
    private Main main;
    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private RentalDAO rentalDAO;
    private ReservationDAO reservationDAO;
    private Item item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearForm();
        main = Main.getInstance();
        //Get necessary DAO for data call
        itemDAO = main.getItemDAO();
        customerDAO = main.getCustomerDAO();
        rentalDAO = main.getRentalDAO();
        reservationDAO = main.getReservationDAO();
        findItem();
        tfItemID.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                clearForm();
                this.item = itemDAO.getById(Item.class, tfItemID.getText());
                findItem();
            }
        });
    }
    void findItem(){
        if (item == null) {
            tfItemID.requestFocus();
            return;
        }
        loadBasicInfo(item);
        if(item.getStatus().equalsIgnoreCase(Item.RENTED)){
            Rental lastestRental = rentalDAO.getLatestRentalByItemID(item.getItemID());
            RentalDetail rentalDetailofItem = null;
            List<RentalDetail> rentalDetailList = lastestRental.getItems();
            for (RentalDetail rentalDetail : rentalDetailList) {
                if (rentalDetail.getItem().equals(item)) {
                    rentalDetailofItem = rentalDetail;
                }
            }
            LocalDate dueDate = lastestRental.getDate().plusDays(rentalDetailofItem.getRentalPeriod());
            lbStatus.setText(Item.RENTED);
            lbstatusDetail.setText("Due On : " + dueDate + "\nto");
            loadCustomerInfo(lastestRental.getCustomer());
        }
        if(item.getStatus().equalsIgnoreCase(Item.ON_HOLD)){
            Reservation reservation = reservationDAO.getReservationbyItemID(item.getItemID());
            if (reservation != null) {
                lbStatus.setText(Item.ON_HOLD);
                lbstatusDetail.setText("for");
                loadCustomerInfo(reservation.getCustomer());
                return;
            }
        }
    }
    private void clearForm(){
        lbTitle.setText("");
        lbStatus.setText("");
        lbstatusDetail.setText("");
        customerInfo.setText("");
    }
    private void loadBasicInfo(Item item){
        tfItemID.setText(item.getItemID());
        lbTitle.setText(item.getTitle().getTitleName());
        lbStatus.setText(item.getStatus());
        imgTitle.setImage(item.getTitle().getImage());
    }

    private void loadCustomerInfo(Customer customer){
        String info ="Customer ID: " + customer.getCustomerID() + "\n" + "Customer Name: " + customer.getFirstName() + " " + customer.getLastName()+"\n"
                + "Address: " + customer.getAddress() + "\n" + "Phone Number: " + customer.getPhoneNumber();
        customerInfo.setText(info);

    }

    public void setItem(Item item) {
        this.item = item;
    }
}