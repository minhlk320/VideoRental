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
        });
    }
}
