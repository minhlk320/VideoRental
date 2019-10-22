package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
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
	    private Label lbTime;

	    @FXML
	    private JFXButton btnLogin;
	    
	    @FXML
	    private Label lbMonthDay;

	    @FXML
	    private Label lbDaySurfix;

	    @FXML
	    private Label lbYear;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadClockandDate();
		btnTitle.setOnAction(e->{
			Main.changeLayout(Main.SCENE_TITLE_MANAGEMENT);
		});
		btnCustomers.setOnAction(e->{
			Main.changeLayout(Main.SCENE_CUSTOMER_MANAGEMENT);
		});
		btnItem.setOnAction(e->{
			Main.changeLayout(Main.SCENE_ITEM_MANAGEMENT);
		});
		
		
	}
	private void loadClockandDate() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
	        LocalTime currentTime = LocalTime.now();
	        LocalDate currentDate = LocalDate.now();
	        int second = currentTime.getSecond();
	        int hour = currentTime.getHour();
	        int minute = currentTime.getMinute();
	        int day = currentDate.getDayOfMonth();       
	        String month = currentDate.getMonth().toString().toLowerCase();
	        		month = month.substring(0,1).toUpperCase() + month.substring(1,3);
	        int year = currentDate.getYear();
	        String time = hour<10?("0"+hour):(hour)+":";
	        		time += minute<10?("0"+minute):(minute);
	        		time +=":";
	        		time += second<10?("0"+second):(second);
	        lbTime.setText(time);
	        lbMonthDay.setText(month + " " + day);
	        lbDaySurfix.setText(surfixDate(day));
	        lbYear.setText(", "+year+"");
	    }),
	         new KeyFrame(Duration.seconds(1))
	    );
	    clock.setCycleCount(Animation.INDEFINITE);
	    clock.play();
	}
	private String surfixDate(int x) {
			if(x==1||x==21||x==31)
				return "st";
			if(x==2||x==22)
				return "nd";
			if(x==3||x==23)
				return "rd";
			return "th";
	}

}
