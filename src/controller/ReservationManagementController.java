package controller;

import daos.ReservationDAO;
import entities.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {

    @FXML
    private Text lbItemID;

    @FXML
    private TextField tf_customerID;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnCancelReservation;

    @FXML
    private Button btnRecall;

    @FXML
    private TableView<Reservation> table;

    @FXML
    private TableColumn<Reservation, String> colReservationID;

    @FXML
    private TableColumn<Reservation, String> colCustomerID;

    @FXML
    private TableColumn<Reservation, String> colReservationDate;

    @FXML
    private TableColumn<Reservation, String> colTitle;

    @FXML
    private TableColumn<Reservation, String> colItemID;

    @FXML
    private TableColumn<Reservation, String> colComment;
    private Main main;
    private ReservationDAO reservationDAO;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        reservationDAO = main.getReservationDAO();
        List<Reservation> reservationList = reservationDAO.getAll(Reservation.class);
        loadTable(reservationList);
        tf_customerID.setOnKeyReleased(e->{
            if (e.getCode() == KeyCode.ENTER) {
                loadTable(reservationDAO.getReservationbyCustomerID(tf_customerID.getText()));
            }
        });
        btnEnter.setOnAction(e->{
            loadTable(reservationDAO.getReservationbyCustomerID(tf_customerID.getText()));
        });
        btnNew.setOnAction(e->{
            main.changeScene(main.SCENE_RESERVATION);
        });
        btnCancelReservation.setOnAction(e->{
            Reservation reservation = table.getSelectionModel().getSelectedItem();
            if(reservation==null)
                showMessage("Choose a line on table to cancel","Message",null);
            else
            {
                if(confirmDialog("Warning","Do you want to cancel the chosen reservation")){
                    table.getItems().remove(reservation);
                    reservationDAO.delete(reservation);
                    if (reservation.getItem()!=null)
                        reservationDAO.checkReservation(reservation.getItem());
                    showMessage("Cancellation successful!","Message",null);
                }
            }
        });
        btnRecall.setOnAction(e->{
            showMessage("Todo, call rental function to make a rental transaction to this customer along with on-hold item","Message",null);
        });

    }
    private boolean confirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
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
    private void showMessage(String message, String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadTable(List<Reservation> list) {
//        if (list.isEmpty()){
//            showMessage("Reservation not found, please try again!", "Message",null);
//            return;
//        }

        ObservableList<Reservation> tkList = FXCollections.observableArrayList(list);
        for(int i = 0; i<tkList.size(); i++){
            colReservationID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getReservationID()));
            colItemID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItem()==null?"":celldata.getValue().getItem().getItemID()));
            colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
            colCustomerID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getCustomer().getCustomerID()));
            colComment.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getComment()));
            colReservationDate.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getReservationDate().toString()));

        }
        table.setItems(tkList);
    }

}
