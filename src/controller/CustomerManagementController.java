package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CustomerManagementController implements Initializable{

	@FXML
	private TextField txtCustomerID;

	@FXML
	private TextField txtFName;

	@FXML
	private Text lbFName;

	@FXML
	private TextField txtLName;

	@FXML
	private Text lbLname;

	@FXML
	private TextField txtFone;

	@FXML
	private Text lbFone;

	@FXML
	private TextField txtAddress;

	@FXML
	private Text lbAddress;

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
	private TableColumn<?, ?> colCustomerID;

	@FXML
	private TableColumn<?, ?> colFName;

	@FXML
	private TableColumn<?, ?> colLName;

	@FXML
	private TableColumn<?, ?> colNumber;

	@FXML
	private TableColumn<?, ?> colAddress;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
