package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    private Main main;
    @FXML
    private Button btnClose;

    @FXML
    private Button btnRentalItems;
    @FXML
    private Button btnReturnItems;
    @FXML
    private Button btnMakeReservation;
    @FXML
    private Button btnReservationList;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnTitle;
    @FXML
    private Button btnItems;
    @FXML
    private Button btnInventory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        btnRentalItems.setOnAction(e -> {
            main.changeScene(main.SCENE_RENTAL_ITEMS);
            btnClose.setVisible(true);
        });
        btnReturnItems.setOnAction(e -> {
            main.changeScene(main.SCENE_RETURN_ITEM);
            btnClose.setVisible(true);
        });
        btnClose.setOnAction(e -> {
            main.closeCenter();
            btnClose.setVisible(false);
        });
        btnTitle.setOnAction(e -> {
            main.changeScene(main.SCENE_TITLE_MANAGEMENT);
            btnClose.setVisible(true);
        });
        btnCustomer.setOnAction(e -> {
            main.changeScene(main.SCENE_CUSTOMER_MANAGEMENT);
            btnClose.setVisible(true);
        });
        btnItems.setOnAction(e -> {
            main.changeScene(main.SCENE_ITEM_MANAGEMENT);
            btnClose.setVisible(true);
        });
        btnMakeReservation.setOnAction(e->{
            main.changeScene(main.SCENE_RESERVATION);
            btnClose.setVisible(true);
        });
        btnReturnItems.setOnAction(e->{
            main.changeScene(main.SCENE_RETURN_ITEM);
            btnClose.setVisible(true);
        });
        btnReservationList.setOnAction(e->{
            main.changeScene(main.SCENE_RESERVATION_MANAGEMENT);
            btnClose.setVisible(true);
        });

    }

}
