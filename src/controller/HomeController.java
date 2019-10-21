package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ui.Main;

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
		btnTitle.setOnAction(e->{
			Main.changelayout(Main.SCENE_TITLE_MANAGEMENT, e);
		});
		btnCustomers.setOnAction(e->{
			Main.changelayout(Main.SCENE_CUSTOMER_MANAGEMENT, e);
		});
		btnItem.setOnAction(e->{
			Main.changelayout(Main.SCENE_ITEM_MANAGEMENT, e);
		});
		
	}

}
