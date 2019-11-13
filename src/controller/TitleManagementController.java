package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import daos.RateDAO;
import daos.TitleDAO;
import entities.Item;
import entities.Rate;
import entities.Title;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TitleManagementController implements Initializable {


	@FXML
	private TextField txtTitleID;

	@FXML
	private ImageView imageView;

	@FXML
	private JFXButton btnChoose;

	@FXML
	private TextField txtTitle;

	@FXML
	private JFXTextArea txtDescription;

	@FXML
	private JFXButton btnNew;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnDelete;
	@FXML
	private Button btnRefresh;

	@FXML
	private ComboBox<Rate> cbItemClass;

	@FXML
	private TableView<Title> table;

	@FXML
	private TableColumn<Title, String> colItemClass;

	@FXML
	private TableColumn<Title, String> colTitleID;

	@FXML
	private TableColumn<Title, String> colTitle;

	@FXML
	private TableColumn<Title, String> colNumOfCopies;
	@FXML
	private TableColumn<Title, String> colAllCopies;

	@FXML
	private TableColumn<Title, String> colRented;

	@FXML
	private TableColumn<Title, String> colOnHold;

	@FXML
	private TableColumn<Title, String> colOnShelf;

	@FXML
	private TableColumn<Title, String> colLost_Damage;

	@FXML
	private TableColumn<Title, String> colDescription;
	private Main main;
	private List<Title> listTitles;
	private TitleDAO titleDAO;
	private RateDAO rateDAO;
	private String url_image;
	private File image;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		main = Main.getInstance();
		titleDAO = main.getTitleDAO();
		rateDAO = main.getRateDAO();
		listTitles = titleDAO.getAll(Title.class);
		initTable(listTitles);
		showItemClass();
		btnRefresh.setOnAction(event -> {
			refreshTable();
		});
		btnNew.setOnAction(e->{
			txtTitle.clear();
			txtDescription.clear();
			imageView.setImage(null);
			if (!listTitles.isEmpty()) {
				String id_last = listTitles.get(listTitles.size() - 1).getTitleID();
				int id = Integer.valueOf((id_last));
				String new_id = String.format("%06d", id + 1);
				txtTitleID.setText(new_id);
			} else {
				txtTitleID.setText("000001");
			}
			cbItemClass.getSelectionModel().select(-1);
		});


		btnSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Title title;
				String id = txtTitleID.getText();
				if (titleDAO.getById(Title.class, id) != null) {
					title = titleDAO.getById(Title.class, id);
					titleDAO.update(getCurrentTitle(title));
				}else{
					title = new Title();
					titleDAO.save(getCurrentTitle(title));
				}
				refreshTable();

			}
			Title getCurrentTitle(Title title) {
				String titleName = txtTitle.getText();
				String des = txtDescription.getText();
				Rate rate = cbItemClass.getValue();
				if(validate()){
					title.setImage(image);
					title.setDescription(des);
					title.setTitleName(titleName);
					title.setItemClass(rate);
					return title;
				}else {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Message !");
					alert.setHeaderText(null);
					alert.setContentText("Complete all information please !");
					alert.showAndWait();
					return null;
				}
			}
		});

		btnChoose.setOnAction(e->{

			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image files *.jpg, *.png","*.jpg","*.png");
			fileChooser.getExtensionFilters().add(extensionFilter);
			image = fileChooser.showOpenDialog((Stage) ((Node) btnChoose).getScene().getWindow());
			if (image != null) {
				url_image=(image.getAbsolutePath());
				try {
					imageView.setImage(new Image(new FileInputStream(image)));
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				}
			}

		});



		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TitleDAO titleDAO = new TitleDAO();
				Title title = table.getSelectionModel().getSelectedItem();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Message !");
				alert.setHeaderText("Are you sure ?");
				ButtonType buttonTypeYes = new ButtonType("Yes");
				ButtonType buttonTypeCancel = new ButtonType("Cancel");
				alert.getButtonTypes().setAll(buttonTypeYes,buttonTypeCancel);
				Optional<ButtonType> result = alert.showAndWait();
				switch (result.get().getText()) {
					case "Yes":
						boolean x = titleDAO.delete(title);
						alert.setContentText("Deleted !");
						break;
					default:
						alert.close();
						break;
				}
				refreshTable();
			}
		});

		table.setOnMousePressed(e->{
			if(table.getSelectionModel().getSelectedItem()==null)
				return;
			if(e.isPrimaryButtonDown() && e.getClickCount()==1) {
				txtTitleID.setText(table.getSelectionModel().getSelectedItem().getTitleID());
				txtTitle.setText(table.getSelectionModel().getSelectedItem().getTitleName());
				txtDescription.setText(table.getSelectionModel().getSelectedItem().getDesciption());
				cbItemClass.setValue(table.getSelectionModel().getSelectedItem().getItemClass());
				imageView.setImage(table.getSelectionModel().getSelectedItem().getImage());
			}
		});
	}


	private void initTable(List<Title> list) {
		ObservableList<Title> tkList = FXCollections.observableArrayList(list);
			colTitleID.setSortable(false);
			colTitleID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleID()));
			colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleName()));
			colDescription.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDesciption()));
			//colNumOfCopies.setCellValueFactory((celldata->new SimpleStringProperty("0")));
			colAllCopies.setCellValueFactory((celldata->new SimpleStringProperty(titleDAO.getNumberAllcopies(celldata.getValue().getTitleID())+"")));
			colLost_Damage.setCellValueFactory(celldata->new SimpleStringProperty(titleDAO.getNumbercopiesByStatus(celldata.getValue().getTitleID(), Item.LOST_DAMAGE)+""));
			colOnHold.setCellValueFactory(celldata->new SimpleStringProperty(titleDAO.getNumbercopiesByStatus(celldata.getValue().getTitleID(), Item.ON_HOLD)+""));
			colOnShelf.setCellValueFactory(celldata->new SimpleStringProperty(titleDAO.getNumbercopiesByStatus(celldata.getValue().getTitleID(), Item.ON_SHELF)+""));
			colRented.setCellValueFactory(celldata->new SimpleStringProperty(titleDAO.getNumbercopiesByStatus(celldata.getValue().getTitleID(), Item.RENTED)+""));
			colItemClass.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItemClass().getItemClassName()));
		table.setItems(tkList);
	}

	private void refreshTable() {
		listTitles = titleDAO.getAll(Title.class);
		table.getItems().setAll(listTitles);
		table.refresh();
	}

	private void showItemClass() {
		ObservableList<Rate> listRate = FXCollections.observableArrayList(rateDAO.getAll(Rate.class));
		cbItemClass.setItems(listRate);
		cbItemClass.getSelectionModel().select(-1);
	}

	private boolean validate(){
		if(txtTitle.getText().trim().equals("")||cbItemClass.getSelectionModel().getSelectedItem()==null||txtDescription.getText().trim().equals("")||imageView.getImage()==null){
			return false;
		}
		return true;
	}

}
