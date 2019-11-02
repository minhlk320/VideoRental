package controller;

import com.jfoenix.controls.JFXTextField;
import entities.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ReturnItemController implements Initializable {
    private Main main;
    @FXML
    private JFXTextField tf_itemID;
    @FXML
    private Button btn_EnterItemID;
    @FXML
    private TableView tableItemList;
    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colItemID;
    @FXML
    private TableColumn colTitle;
    @FXML
    private TableColumn colPrice;
    @FXML
    private TableColumn colDeleteButton;
    @FXML
    private Button btnResetTable;
    @FXML
    private Text text_RentalTotal;
    @FXML
    private Text text_LateCharge;
    @FXML
    private Button btn_Done;
    @FXML
    private Button btn_Cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        btn_EnterItemID.setOnAction(e -> {
            String itemID = tf_itemID.getText();
            if (itemID.isEmpty()) {
                tf_itemID.requestFocus();
                return;
            }
            Item item = findItem(itemID);
            if (item != null) {
                addToTable(item);
            }
        });

    }

    private void addToTable(Item item) {
    }

    private Item findItem(String itemID) {
        return null;
    }
}
