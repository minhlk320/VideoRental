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
import javafx.scene.input.KeyCode;
import ui.Main;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnItemController implements Initializable {

    private Main main;

    @FXML
    private JFXTextField tf_ItemID;

    @FXML
    private JFXButton btnEnter;

    private RentalDAO rentalDAO;
    private ItemDAO itemDAO;
    private LateChargeDAO lateChargeDAO;
    private ReservationDAO reservationDAO;
    private boolean late = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        rentalDAO = main.getRentalDAO();
        itemDAO = main.getItemDAO();
        lateChargeDAO = main.getLateChargeDAO();
        reservationDAO = main.getReservationDAO();
        btnEnter.setOnAction(e->{
            returnItem(tf_ItemID.getText());

        });
        tf_ItemID.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                returnItem(tf_ItemID.getText());
            }
        });
    }


    public void returnItem(String id) {
        Item item = itemDAO.getById(Item.class,id);
        if (item == null)
            main.showMessage("Entered ID not found, please check again!", "Message", null);
        else if (!item.getStatus().equalsIgnoreCase(Item.RENTED))
            main.showMessage("Entered ID can not be return due to entered item's status is : " + item.getStatus(), "Message", null);
        else {
            returnItem(item.getItemID());
            if (late)
                main.showMessage("This item has been returned lately (Todo call function 5c to record if chose yes)", "Message", null);
            else
                main.showMessage("Returned : " + item.getItemID(), "Message", null);
        }
        Rental lastestRental = rentalDAO.getLatestRentalByItemID(id);
        Customer customer = lastestRental.getCustomer();
        RentalDetail rentalDetailofItem = null;
        List<RentalDetail> rentalDetailList = lastestRental.getItems();
        for(int i = 0; i<rentalDetailList.size();i++){
            if(rentalDetailList.get(i).getItem().equals(item)){
                rentalDetailofItem = rentalDetailList.get(i);
            }
        }

        LocalDate rentedDate = lastestRental.getDate();
        LocalDate currentDate = LocalDate.now();

        if(currentDate.isAfter(rentedDate.plusDays(rentalDetailofItem.getRentalPeriod()))){
            LocalDate dueOn = rentedDate.plusDays(rentalDetailofItem.getRentalPeriod());
            Duration diff = Duration.between(dueOn.atStartOfDay(), currentDate.atStartOfDay());
            int numOfOverDueDay = (int) diff.toDays();
            double totalAmount = numOfOverDueDay * rentalDetailofItem.getLateRate();
            lateChargeDAO.addLateCharge(item, customer, totalAmount, dueOn);
            late = true;
        }
        Reservation reservation = reservationDAO.checkReservation(item);
        if(reservation!=null)
            main.showMessage("The newly returned Item has been placed ON_HOLD to :  " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName() + "\n"
                    + "Phone: " +reservation.getCustomer().getPhoneNumber(),"Message",null);
    }


}
