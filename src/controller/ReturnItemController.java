package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.ItemDAO;
import daos.LateChargeDAO;
import daos.RentalDAO;
import daos.ReservationDAO;
import entities.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import ui.Main;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReturnItemController implements Initializable {

    private Main main;

    @FXML
    private JFXTextField tfItemID;

    @FXML
    private JFXButton btnEnter;
    private RentalDAO rentalDAO;
    private ItemDAO itemDAO;
    private LateChargeDAO lateChargeDAO;
    private ReservationDAO reservationDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        rentalDAO = main.getRentalDAO();
        itemDAO = main.getItemDAO();
        lateChargeDAO = main.getLateChargeDAO();
        reservationDAO = main.getReservationDAO();
        btnEnter.setOnAction(e -> {
            returnItem(tfItemID.getText());
        });
        tfItemID.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                returnItem(tfItemID.getText());
            }
        });
    }

    /**
     * @param id
     * @UC01b Return Item
     * Return Item by id
     */
    public void returnItem(String id) {
        Item item = itemDAO.getById(Item.class,id);
        if (item == null) {
            main.showMessage("Entered ID not found, please check again!", "Message", null);
            tfItemID.requestFocus();
            return;
        }
        if (!item.getStatus().equalsIgnoreCase(Item.RENTED)) {
            main.showMessage("Entered ID can not be return due to entered item's status is : " + item.getStatus(), "Message", null);
            return;
        }
        Rental lastestRental = rentalDAO.getLatestRentalByItemID(item.getItemID());
        if(confirmRequest("Do you want to return this Item?" + "\n ItemID:" + item.getItemID() + "\n Title: "+ item.getTitle().getTitleName() +  "\n Customer Info:\n" + lastestRental.getCustomer().getFirstName() + " " +lastestRental.getCustomer().getLastName() + "\n Address: " + lastestRental.getCustomer().getAddress() + "\n Phone:" + lastestRental.getCustomer().getPhoneNumber(),"Message")){
            checkReturnItem(item);
            Reservation reservation = reservationDAO.checkReservation(item);
            if (reservation != null) {
                main.showMessage("The newly returned Item has been placed ON_HOLD to :  " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName() + "\n" + "Phone: " + reservation.getCustomer().getPhoneNumber(), "Message", null);
                return;
            }
        }

    }

    /**
     * @param item
     * @UC01b Return item
     * check item for return action/
     */
    private void checkReturnItem(Item item) {
        Rental lastestRental = rentalDAO.getLatestRentalByItemID(item.getItemID());
        LocalDate currentDate = LocalDate.now();
        LocalDate rentedDate = lastestRental.getDate();
        Customer customer = lastestRental.getCustomer();
        RentalDetail rentalDetailofItem = null;
        List<RentalDetail> rentalDetailList = lastestRental.getItems();
        for (RentalDetail rentalDetail : rentalDetailList) {
            if (rentalDetail.getItem().equals(item)) {
                rentalDetailofItem = rentalDetail;
            }
        }
        if (currentDate.isAfter(rentedDate.plusDays(rentalDetailofItem.getRentalPeriod()))) {
            LocalDate dueOn = rentedDate.plusDays(rentalDetailofItem.getRentalPeriod());
            Duration diff = Duration.between(dueOn.atStartOfDay(), currentDate.atStartOfDay());
            int numOfOverDueDay = (int) diff.toDays();
            double totalAmount = numOfOverDueDay * rentalDetailofItem.getLateRate();
            lateChargeDAO.addLateCharge(item, customer, totalAmount, dueOn);
            if(main.requestConfirm("This item has been returned lately! Do you want to make late charge payment now?","Message",null))
                main.displayLateCharge(customer);
        }
        main.showMessage("Returned : " + item.getItemID(), "Message", null);
    }
    private boolean confirmRequest(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
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
}
