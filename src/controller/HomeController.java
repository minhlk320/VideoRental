package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
	
	 @FXML
	 private JFXButton btnRentalItems;

	    @FXML
	    private JFXButton btnReturnItem;

	    @FXML
	    private JFXButton btnReportItemStatus;

	    @FXML
	    private JFXButton btnCheckLateCharge;

	    @FXML
	    private JFXButton btnMakeReservation;

	    @FXML
	    private JFXButton btnReservationList;

	    @FXML
	    private JFXButton btnCustomers;

	    @FXML
	    private JFXButton btnTitle;

	    @FXML
	    private JFXButton btnItem;

	    @FXML
	    private JFXButton btnInventory;

	    @FXML
	    private TableView<?> table;

	    @FXML
	    private TableColumn<?, ?> colRentalID;

	    @FXML
	    private TableColumn<?, ?> colTitle;

	    @FXML
	    private TableColumn<?, ?> colStatus;

	    @FXML
	    private TableColumn<?, ?> colRentedOn;

	    @FXML
	    private TableColumn<?, ?> colDueOn;

	    @FXML
	    private Label lbSale;

	    @FXML
	    private Label lbDate;

	    @FXML
	    private Label lbTime;

	    @FXML
	    private JFXButton btnLogin;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main main = Main.getInstance();
		btnTitle.setOnAction(e->{
			main.changeScene(main.SCENE_TITLE_MANAGEMENT);
		});
		btnCustomers.setOnAction(e->{
			main.changeScene(main.SCENE_CUSTOMER_MANAGEMENT);
		});
		btnItem.setOnAction(e->{
			main.changeScene(main.SCENE_ITEM_MANAGEMENT);
		});
		
	}

}
