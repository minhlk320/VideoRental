package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    private final int TAB_ITEM = 0;
    private final int TAB_RESERVATION = 1;
    private final int TAB_CUSTOMER = 2;
    private Main main;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab reservation;
    @FXML
    private Tab customer;
    @FXML
    private Tab item;
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
        btnReservationList.setOnAction(e->{
            main.changeScene(main.SCENE_RESERVATION_MANAGEMENT);
            btnClose.setVisible(true);
        });
        tabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
            switch ((int) newValue) {
                case TAB_ITEM:
                    main.changeScene(main.SCENE_ITEM_MANAGEMENT);
                    break;
                case TAB_RESERVATION:
                    main.changeScene(main.SCENE_RESERVATION_MANAGEMENT);
                    break;
                case TAB_CUSTOMER:
                    main.changeScene(main.SCENE_CUSTOMER_MANAGEMENT);
                    break;
            }
        });
    }

}
