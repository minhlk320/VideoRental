package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ui.Main;

public class CustomerManagementController implements Initializable{


    @FXML
    private TextField txtTitleID;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton btnChoose;

    @FXML
    private TextField txtTitle;

    @FXML
    private Text lbTitle;

    @FXML
    private TextField txtDescription;

    @FXML
    private Text lbDescription;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> colTitleID;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colNumOfCopies;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private Label lbSale;

    @FXML
    private Label lbMonthDay;

    @FXML
    private Label lbDaySurfix;

    @FXML
    private Label lbYear;

    @FXML
    private Label lbTime;

    @FXML
    private JFXButton btnLogin;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnBack.setOnAction(e->{
			Main.changeLayout(Main.SCENE_HOME);
		});
		
	}

}
