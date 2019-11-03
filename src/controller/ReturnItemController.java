package controller;

import com.jfoenix.controls.JFXTextField;
import daos.ItemDAO;
import entities.Item;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnItemController implements Initializable {
    private ItemDAO itemDAO;
    private Main main;
    private List<Item> listItems;
    @FXML
    private JFXTextField tfItemID;
    @FXML
    private Button btnEnterItemID;
    @FXML
    private TableView tableItemList;
    @FXML
    private TableColumn<Item, Item> colNo;
    @FXML
    private TableColumn<Item, String> colItemID;
    @FXML
    private TableColumn<Item, String> colTitle;
    @FXML
    private TableColumn<Item, String> colPrice;
    @FXML
    private TableColumn<Item, Item> colDeleteButton;
    @FXML
    private Button btnResetTable;
    @FXML
    private Text textTotalDisk;
    @FXML
    private Button btnDone;
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        listItems = new ArrayList<>();
        itemDAO = new ItemDAO();
        btnEnterItemID.setOnAction(e -> {
            String itemID = tfItemID.getText();
            if (itemID.isEmpty()) {
                tfItemID.requestFocus();
                return;
            }

            Item item = findItem(itemID);
            addToTable(item);
            tfItemID.clear();
        });
        initTable();
    }

    private void initTable() {
        colNo.setCellValueFactory(
                param -> {
                    return new ReadOnlyObjectWrapper<>(param.getValue());
                });
        colNo.setCellFactory(new Callback<TableColumn<Item, Item>, TableCell<Item, Item>>() {
            @Override
            public TableCell<Item, Item> call(TableColumn<Item, Item> param) {
                return new TableCell<Item, Item>() {
                    @Override
                    protected void updateItem(Item arg0, boolean arg1) {
                        super.updateItem(arg0, arg1);
                        if (this.getTableRow() != null && arg0 != null) {
                            setText(this.getTableRow().getIndex() + 1 + "");
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        colDeleteButton.setSortable(false);
        colDeleteButton.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        colDeleteButton.setCellFactory(param -> new TableCell<Item, Item>() {
            private final Button deleteButton = new Button("X");

            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            listItems.remove(item);
                            getTableView().getItems().remove(item);
                        }
                );
            }
        });
        listItems = new ArrayList<>();
        ObservableList<Item> items = FXCollections.observableArrayList(listItems);
        colNo.setSortable(false);
        colItemID.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        colTitle.setCellValueFactory(cdata -> new SimpleStringProperty(cdata.getValue().getTitle().getTitleName()));
        colPrice.setCellValueFactory(cdata -> new SimpleStringProperty(String.format("%2.2f", cdata.getValue().getTitle().getItemClass().getRentalRate())));
        tableItemList.setItems(items);
        btnResetTable.setOnAction(e -> {
            //
            listItems.clear();
            tableItemList.getItems().clear();
        });

    }

    public void addToTable(Item item) {
        if (item == null) return;
        if (listItems.contains(item)) {
            return;
        }
        tfItemID.clear();
        listItems.add(item);
        tableItemList.getItems().setAll(listItems);
        tableItemList.refresh();
    }

    private Item findItem(String itemID) {
        //Find Item from Database
        Item item = itemDAO.getById(Item.class, itemID);
        return item;
    }
}
