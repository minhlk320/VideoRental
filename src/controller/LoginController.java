package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private JFXPasswordField tfPassword;
    @FXML
    private JFXTextField tfUsername;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnLogout;

    private Main main;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();

        btnLogin.setOnAction(event -> {
            String username = tfUsername.getText();
            String password = tfPassword.getText();
            if (checkForm(username, password)) {
                if (main.checkLogin(username, password)) {
                    main.showMessage("", "Success", "Login Successfully!");
                    closeWindow(event);
                } else {
                    main.showMessage("", "Fail", "username or password are incorrect!");
                }
            }
        });
    }

    private void closeWindow(ActionEvent event) {
        main.refreshContent();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private boolean checkForm(String username, String password) {

        if (username.isEmpty()) {
            tfUsername.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            tfPassword.requestFocus();
            return false;
        }
        return true;
    }
}
