package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HomeController implements Initializable{

    @FXML
    private JFXButton btnCustomers;

    @FXML
    private FontAwesomeIcon btnProcess;

    @FXML
    private FontAwesomeIcon btnRentalItems;

    @FXML
    private FontAwesomeIcon btnSettings;

    @FXML
    private FontAwesomeIcon btnReturn;

    @FXML
    private FontAwesomeIcon btnReport;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> colRentalID;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colDueOn;

    @FXML
    private TableColumn<?, ?> colRentedOn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}

}
