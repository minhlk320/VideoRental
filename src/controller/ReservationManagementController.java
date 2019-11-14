package controller;

import daos.ReservationDAO;
import entities.Reservation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ui.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationManagementController implements Initializable {

    @FXML
    private TextField tfCustomerID;

    @FXML
    private Button btnCancelReservation;

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
    private List<Reservation> reservationList;
    private FilteredList<Reservation> filterReservation;
    private SortedList<Reservation> sortedList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        reservationDAO = main.getReservationDAO();
        reservationList = reservationDAO.getAll(Reservation.class);
        initTable(reservationList);
        tfCustomerID.textProperty().addListener((observable, oldValue, newValue) -> {
            filterReservation.setPredicate(reservation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reservation.getCustomer().getCustomerID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        btnCancelReservation.setOnAction(e->{
            Reservation reservation = table.getSelectionModel().getSelectedItem();
            deleteReservation(reservation);
            refreshTable();
        });
    }

    private void deleteReservation(Reservation reservation) {
        if (reservation == null) {
            main.showMessage("Choose a line on table to cancel", "Message", null);
            return;
        }
        boolean confirmDelete = main.requestConfirm("Do you want to cancel the chosen reservation", "Warning", null);
        if (confirmDelete) {
            reservationDAO.delete(reservation);
            if (reservation.getItem() != null) {
                reservationDAO.checkReservation(reservation.getItem());
            }
            main.showMessage("Cancellation successful!", "Message", null);
        }
        refreshTable();
    }

    private void refreshTable() {
        tfCustomerID.clear();
        reservationList = reservationDAO.getAll(Reservation.class);
        table.setItems(FXCollections.observableArrayList(reservationList));
        table.refresh();
    }

    private void initTable(List<Reservation> list) {
        ObservableList<Reservation> tkList = FXCollections.observableArrayList(list);
        colReservationID.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getReservationID()));
        colItemID.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getItem() == null ? "" : celldata.getValue().getItem().getItemID()));
        colTitle.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
        colCustomerID.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getCustomer().getCustomerID()));
        colComment.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getComment()));
        colReservationDate.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getReservationDate().toString()));
        filterReservation = new FilteredList<>(tkList, p -> true);
        sortedList = new SortedList<>(filterReservation);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

}
