package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import daos.*;
import entities.Item;
import entities.Title;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ui.Main;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemManagementController implements Initializable {
    @FXML
    private Button btnReturnItem;
    @FXML
    private Button btnReportItem;
    @FXML
    private TextField txtItemID;

    @FXML
    private ComboBox<Title> cbTitle;


    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private Button btnRefresh;
    @FXML
    private JFXTextField tfFilter;

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
    private TableColumn<Item, String> colCreatedDate;

    @FXML
    private TableColumn<Item, String> colLastModifiedDate;

    private ItemDAO itemDAO;
    private RentalDAO rentalDAO;
    private LateChargeDAO lateChargeDAO;
    private TitleDAO titleDAO;
    private ReservationDAO reservationDAO;
    private List<Item> listItem;
    private FilteredList<Item> filterItem;
    private SortedList<Item> sortedList;
    private Main main;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main = Main.getInstance();
        itemDAO = main.getItemDAO();
        rentalDAO = main.getRentalDAO();
        lateChargeDAO = main.getLateChargeDAO();
        reservationDAO = main.getReservationDAO();
        titleDAO = main.getTitleDAO();
        listItem = itemDAO.getAll(Item.class);
        initTable(listItem);
        showTitleName();
        showStatus();
        if (main.isLogged) {
            btnDelete.setDisable(false);
            btnSave.setDisable(false);
            btnNew.setDisable(false);
        } else {
            btnDelete.setDisable(true);
            btnSave.setDisable(true);
            btnNew.setDisable(true);
        }
        btnRefresh.setOnAction(e -> {
            clearForm();
            refreshTable();
        });
        btnNew.setOnAction(e -> {
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


        });

        btnSave.setOnAction(e -> {
            Item item;
            String id = txtItemID.getText();
            if (itemDAO.getById(Item.class, id) != null) {
                item = itemDAO.getById(Item.class, id);
                item.setLastModifiedDate(LocalDate.now());
                itemDAO.update(getCurrentItem(item));
            } else {
                item = new Item();
                if (getCurrentItem(item) != null) {
                    item = getCurrentItem(item);
                    item.setStatus(Item.ON_SHELF);
                    itemDAO.save(item);
                }
            }
            refreshTable();
        });

        btnDelete.setOnAction(e -> {
            Item item = table.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message !");
            alert.setHeaderText("Are you sure ?");
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            switch (result.get().getText()) {
                case "Yes":
                    boolean x = itemDAO.delete(item);
                    alert.setContentText("Deleted !");
                    clearForm();
                    break;
                default:
                    alert.close();
                    break;
            }

            refreshTable();

        });
        tfFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterItem.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (item.getItemID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        btnReturnItem.setOnAction(e -> {
            //Return Item
            main.displayReturnItem(txtItemID.getText());
        });
        btnReportItem.setOnAction(e -> {
            main.displayStatus(table.getSelectionModel().getSelectedItem());
        });
        table.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 1) {

                    Item item = table.getSelectionModel().getSelectedItem();
                    if (item.getStatus().equals(Item.RENTED)) {
                        btnReturnItem.setDisable(false);
                    } else {
                        btnReturnItem.setDisable(true);
                    }
                    txtItemID.setText(item.getItemID());
                    cbTitle.setValue(item.getTitle());
                    cbStatus.setDisable(false);
                    cbStatus.getSelectionModel().select(getIndexForStatus(item.getStatus()));

                }
            }

            private int getIndexForStatus(String status) {
                switch (status) {
                    case Item.ON_HOLD:
                        return 0;
                    case Item.ON_SHELF:
                        return 1;
                    case Item.RENTED:
                        return 2;
                    case Item.LOST_DAMAGE:
                        return 3;
                }
                return -1;
            }
        });
        table.setRowFactory(tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Item rowData = row.getItem();
                    main.displayStatus(rowData);
                }
            });
            return row;
        });

    }

    private Item getCurrentItem(Item item) {
        Title title = cbTitle.getSelectionModel().getSelectedItem();
        String status = cbStatus.getSelectionModel().getSelectedItem() == null ? (Item.ON_SHELF) : (cbStatus.getSelectionModel().getSelectedItem().toString());

        if (validate()) {
            item.setTitle(title);
            item.setStatus(status);
            return item;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message !");
            alert.setHeaderText(null);
            alert.setContentText("Choose the title for item please !");
            alert.showAndWait();
            return null;
        }
    }

    private void clearForm() {
        txtItemID.clear();
        cbTitle.getSelectionModel().select(-1);
        cbStatus.getSelectionModel().select(-1);
    }
    private void initTable(List<Item> list) {
        ObservableList<Item> tkList = FXCollections.observableArrayList(list);
        colItemID.setSortable(false);
        colItemID.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getItemID()));
        colTitle.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTitle().getTitleName()));
        colStatus.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getStatus()));
        colCreatedDate.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getCreatedDate().toString()));
        colLastModifiedDate.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getLastModifiedDate().toString()));
        filterItem = new FilteredList<>(tkList, p -> true);
        sortedList = new SortedList<>(filterItem);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private void refreshTable() {
        tfFilter.clear();
        listItem = itemDAO.getAll(Item.class);
        table.setItems(FXCollections.observableArrayList(listItem));
        table.refresh();
    }

    private void showTitleName() {
        ObservableList<Title> listTitle = FXCollections.observableArrayList(titleDAO.getAll(Title.class));
        for (int i = 0; i < listTitle.size(); i++) {
            cbTitle.setItems(listTitle);
            cbTitle.getSelectionModel().select(-1);
        }
    }

    private void showStatus() {
        ObservableList listItem = FXCollections.observableArrayList(new String[]{Item.ON_HOLD, Item.ON_SHELF, Item.RENTED, Item.LOST_DAMAGE});
        cbStatus.setItems(listItem);
        cbStatus.getSelectionModel().select(-1);

    }

    private boolean validate() {
        if (cbTitle.getSelectionModel().getSelectedItem() == null) {
            return false;
        }
        return true;
    }


}
