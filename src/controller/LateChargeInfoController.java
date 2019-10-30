package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class LateChargeInfoController implements Initializable {
    @FXML
    private TableView table;
    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colCustomerID;
    @FXML
    private TableColumn colReturnDate;
    @FXML
    private TableColumn colDays;
    @FXML
    private TableColumn colLateCharge;
    @FXML
    private Text text_TotalCharge;
    @FXML
    private JFXButton btnPaid;
    @FXML
    private JFXButton btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
