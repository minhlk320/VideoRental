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

    private RentalDAO rentalDAO = new RentalDAO();
    private ItemDAO itemDAO = new ItemDAO();
    private LateChargeDAO lateChargeDAO = new LateChargeDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private boolean late = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        btnEnter.setOnAction(e->{
            doReturn();
        });
        tf_ItemID.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                doReturn();
            }
        });
    }
    private void showMessage(String message, String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void doReturn(){
        Item item = itemDAO.getById(Item.class,tf_ItemID.getText());
        if(item==null)
            showMessage("Entered ID not found, please check again!","Message",null);
        else if(!item.getStatus().equalsIgnoreCase(Item.RENTED))
            showMessage("Entered ID can not be return due to entered item'status is : " + item.getStatus(),"Message",null);
        else{
            Return(item.getItemID());
            if(late)
                showMessage("This item has been returned lately (Todo call function 5c to record if chose yes)","Message",null);
            else
                showMessage("Returned : " + item.getItemID(),"Message",null);
        }
    }

    public boolean Return(String id){
        Item item = itemDAO.getById(Item.class,id);
        if(item.equals(null))return false;
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
            double totalAmout = numOfOverDueDay * rentalDetailofItem.getLateRate();
            lateChargeDAO.addLateCharge(item, customer, totalAmout, dueOn);
            late = true;
        }
        reservationDAO.checkReservation(item);
        return true;
    }


}
