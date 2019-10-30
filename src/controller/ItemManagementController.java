package controller;

import com.jfoenix.controls.JFXButton;
import daos.ItemDAO;
import daos.RateDAO;
import daos.TitleDAO;
import entities.Item;
import entities.Rate;
import entities.Title;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ItemManagementController implements Initializable{

    @FXML
    private TextField txtItemID;

    @FXML
    private Text lbItemID;

    @FXML
    private ComboBox<Title> cbTitle;


    @FXML
    private ComboBox<Item> cbStatus;

    @FXML
    private ComboBox<Rate> cbItemClass;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnNew;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item, String> colItemID;

    @FXML
    private TableColumn<Item, String> colTitle;

    @FXML
    private TableColumn<Item, String> colStatus;

    @FXML
    private TableColumn<Item, String> colItemClass;

    @FXML
    private Label lbSale;

    @FXML
    private Label lbDate;

    @FXML
    private Label lbTime;

    @FXML
    private JFXButton btnLogin;

    private List<Item> listItem ;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    Main main = Main.getInstance();
	    listItem = new ItemDAO().getAll(Item.class);
	    loadTable(listItem);
	    ShowTitleName();
	    ShowItemClass();
	    ShowStatus();


		btnBack.setOnAction(e->{
			main.changeScene(main.SCENE_HOME);
		});

        btnNew.setOnAction(e->{
            cbTitle.setSelectionModel(null);
            cbStatus.setSelectionModel(null);
            cbItemClass.setSelectionModel(null);
            if (!listItem.isEmpty()) {
                String id_last = listItem.get(listItem.size() - 1).getItemID();
                String prefix = id_last.substring(0, 5);
                int id = Integer.valueOf((id_last.substring(5, id_last.length())));
                String new_id = prefix + String.format("%06d", id + 1);
                txtItemID.setText(new_id);
            } else {
                txtItemID.setText("ITMNo000001");
            }
        });


    }

    private void loadTable(List<Item> list) {
        ObservableList<Item> tkList = FXCollections.observableArrayList(list);
        for(int i = 0; i<tkList.size(); i++){
                colItemID.setSortable(false);
                colItemID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItemID()));
                colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
                colItemClass.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItemClass().getItemClassName()));
                colStatus.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getStatus()));
        }
        table.setItems(tkList);
    }

    private void reloadTable(){
        table.getColumns().clear();
        table.getColumns().addAll(colItemID,colTitle,colStatus,colItemClass);
        listItem = new ItemDAO().getAll(Item.class);
        loadTable(listItem);
    }

    private void ShowTitleName(){
	    ObservableList<Title> listTitle = FXCollections.observableArrayList(new TitleDAO().getALL());
	    for(int i=0; i<listTitle.size(); i++){
	        cbTitle.setItems(listTitle);
            cbTitle.getSelectionModel().select(1);
        }
    }

    private void ShowStatus(){
	    ObservableList listItem = FXCollections.observableArrayList(new String[]{Item.ON_HOLD, Item.ON_SHELF, Item.RENTED,Item.LOST_DAMAGE});
            cbStatus.setItems(listItem);
            cbStatus.getSelectionModel().select(1);

    }

    private void ShowItemClass(){
        ObservableList listRate = FXCollections.observableArrayList(new String[]{Rate.MOVIE, Rate.VIDEOGAME});
        cbItemClass.setItems(listRate);
        cbItemClass.getSelectionModel().select(1);
    }


}
