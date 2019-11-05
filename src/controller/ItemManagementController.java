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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ui.Main;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemManagementController implements Initializable{

    @FXML
    private TextField txtItemID;

    @FXML
    private ComboBox<Title> cbTitle;


    @FXML
    private ComboBox<String> cbStatus;

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
    private JFXButton btnLogin;

    private List<Item> listItem ;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main main = Main.getInstance();
        listItem = new ItemDAO().getAll(Item.class);
        loadTable(listItem);
        ShowTitleName();
        ShowStatus();

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cbStatus.setDisable(true);
                if (!listItem.isEmpty()) {
                    String id_last = listItem.get(listItem.size() - 1).getItemID();
                    int id = Integer.valueOf((id_last));
                    String new_id = String.format("%06d", id + 1);
                    txtItemID.setText(new_id);
                } else
                    txtItemID.setText("000001");
                cbTitle.getSelectionModel().select(-1);
                cbStatus.getSelectionModel().select(-1);

            }


        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Item item;
                ItemDAO itemDAO = new ItemDAO();
                String id = txtItemID.getText();
                if(itemDAO.getById(Item.class,id)!= null){
                    item = itemDAO.getById(Item.class,id);
                    itemDAO.update(getCurrentItem(item));
                }else{
                    item = new Item();
                    if(getCurrentItem(item)!=null){
                        item = getCurrentItem(item);
                        item.setStatus(Item.ON_SHELF);
                        itemDAO.save(item);
                    }
                }
                reloadTable();
            }

            Item getCurrentItem(Item item) {
                Title title = cbTitle.getSelectionModel().getSelectedItem();
                String status = cbStatus.getSelectionModel().getSelectedItem()==null?(Item.ON_SHELF):(cbStatus.getSelectionModel().getSelectedItem().toString());

                if(validate()){
                    item.setTitle(title);
                    item.setStatus(status);
                    return item;
                }else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message !");
                    alert.setHeaderText(null);
                    alert.setContentText("Choose the title for item please !");
                    alert.showAndWait();
                    return null;
                }
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemDAO itemDAO =new ItemDAO();
                Item item = table.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message !");
                alert.setHeaderText("Are you sure ?");
                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeCancel = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeCancel);
                Optional<ButtonType> result = alert.showAndWait();
                switch (result.get().getText()) {
                    case "Yes":
                        boolean x = itemDAO.delete(item);
                        alert.setContentText("Deleted !");
                        break;
                    default:
                        alert.close();
                        break;
                }
                reloadTable();
            }
        });

        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(e.isPrimaryButtonDown() && e.getClickCount()==1) {

                    Item item = table.getSelectionModel().getSelectedItem();
                    txtItemID.setText(item.getItemID());
                    cbTitle.setValue(item.getTitle());
                    cbStatus.setDisable(false);
                    cbStatus.getSelectionModel().select(getIndexForStatus(item.getStatus()));

                }
            }
            private int getIndexForStatus(String s){
                switch (s){
                    case Item.ON_HOLD : return 0;
                    case Item.ON_SHELF : return 1;
                    case Item.RENTED : return 2;
                    case Item.LOST_DAMAGE : return 3;
                }
                return -1;
            }
        });
    }

    private void loadTable(List<Item> list) {
        ObservableList<Item> tkList = FXCollections.observableArrayList(list);
        for(int i = 0; i<tkList.size(); i++){
            colItemID.setSortable(false);
            colItemID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItemID()));
            colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
            colStatus.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getStatus()));
        }
        table.setItems(tkList);
    }

    private void reloadTable(){
        table.getColumns().clear();
        table.getColumns().addAll(colItemID,colTitle,colStatus);
        listItem = new ItemDAO().getAll(Item.class);
        loadTable(listItem);
    }

    private void ShowTitleName(){
        ObservableList<Title> listTitle = FXCollections.observableArrayList(new TitleDAO().getAll(Title.class));
        for(int i=0; i<listTitle.size(); i++){
            cbTitle.setItems(listTitle);
            cbTitle.getSelectionModel().select(-1);
        }
    }

    private void ShowStatus(){
        ObservableList listItem = FXCollections.observableArrayList(new String[]{Item.ON_HOLD, Item.ON_SHELF, Item.RENTED,Item.LOST_DAMAGE});
        cbStatus.setItems(listItem);
        cbStatus.getSelectionModel().select(-1);

    }

    private boolean validate(){
        if(cbTitle.getSelectionModel().getSelectedItem()==null){
            return false;
        }
        return true;
    }





}
