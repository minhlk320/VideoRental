package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemManagementController implements Initializable{

    @FXML
    private TextField txtItemID;

    @FXML
    private Text lbItemID;

    @FXML
    private ComboBox<?> cbTitle;

    @FXML
    private ComboBox<?> cbType;

    @FXML
    private TextField txtInputDay;

    @FXML
    private Text lbInputDay;

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
    private TableColumn<?, ?> colNumber;

    @FXML
    private TableColumn<?, ?> colitemID;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colInputDay;

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
		btnBack.setOnAction(e->{
			main.changeScene(main.SCENE_HOME);
		});
		
	}

}
