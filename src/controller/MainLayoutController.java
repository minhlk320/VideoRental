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
    private final int TAB_TITLE = 2;
    private final int TAB_CUSTOMER = 3;

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
    private Tab title;
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
            main.displayRental();
        });
        btnReturnItems.setOnAction(e -> {
            main.displayReturn();
        });
        btnCustomer.setOnAction(e -> {
            main.changeScene(main.SCENE_CUSTOMER_MANAGEMENT);
        });
        btnMakeReservation.setOnAction(e->{
            main.changeScene(main.SCENE_RESERVATION);
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
                case TAB_TITLE:
                    main.changeScene(main.SCENE_TITLE_MANAGEMENT);
                    break;
            }
        });
    }

}
