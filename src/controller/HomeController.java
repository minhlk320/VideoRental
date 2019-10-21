package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ui.Main;

public class HomeController implements Initializable{
	
	@FXML
    private Label lbSale;

    @FXML
    private Label lbDate;

    @FXML
    private Label lbTime;

    @FXML
    private JFXButton btnLogin;
	@FXML
    private JFXButton btnCustomers;

    @FXML
    private FontAwesomeIcon btnProcess;

    @FXML
    private JFXButton btnRentalItems;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnReturn;

    @FXML
    private JFXButton btnReport;

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
		btnRentalItems.setOnAction(e->{
			Main.changelayout(Main.SCENE_TITLE_MANAGEMENT, e);
		});
		btnCustomers.setOnAction(e->{
			Main.changelayout(Main.SCENE_CUSTOMER_MANAGEMENT, e);
		});
		
	}

}
