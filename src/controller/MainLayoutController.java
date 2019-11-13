package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button btnLogin;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnRentalItems;
    @FXML
    private Button btnReturnItems;
    @FXML
    private Button btnMakeReservation;
    @FXML
    private Button btnPayLateCharge;
    @FXML
    private Label labelUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        //Active UC01a - Rental Item
        btnRentalItems.setOnAction(event -> {
            main.openWindow(main.SCENE_RENTAL_ITEMS, main.TITLE_RENTAL_ITEM);
        });
        //Active UC01b - Return Item
        btnReturnItems.setOnAction(event -> {
            main.displayInputItem(null);
        });
        //Active UC05c - Record late charge payment
        btnPayLateCharge.setOnAction(event -> {
            main.displayLateCharge(null);
        });
        //Active UC06a - Make reservation
        btnMakeReservation.setOnAction(event -> {
            main.openWindow(main.SCENE_RESERVATION, main.TITLE_RESERVATION);
        });
        //Active UC11 - Login
        btnLogin.setOnAction(event -> {
            if (!main.isLogged) {
                main.doLogin();
                if (main.isLogged) {
                    labelUser.setText(main.USER);
                    btnLogin.setText("Logout");
                } else {
                    btnLogin.setText("Login");
                    labelUser.setText("");
                }
            } else {
                main.doLogout();
                btnLogin.setText("Login");
                labelUser.setText("");
            }

        });
        // Listener for Tab change - content change
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
