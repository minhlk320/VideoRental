package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import daos.TitleDAO;
import entities.Title;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ui.Main;

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
	private TableView<Title> table;

	@FXML
	private TableColumn<Title, String> colTitleID;

	@FXML
	private TableColumn<Title, String> colTitle;

	@FXML
	private TableColumn<String, String> colNumOfCopies;

	@FXML
	private TableColumn<Title, String> colDescription;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<Title> listTitles = new TitleDAO().getAll(Title.class);
		lbTitle.setText("");
		lbDescription.setText("");
		loadTable(listTitles);


	}
	void loadTable(List<Title> list) {

		ObservableList<Title> tkList = FXCollections.observableArrayList(list);
		colTitleID.setSortable(false);
		colTitleID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleID()));
		colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleName()));
		colDescription.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDesciption()));
		colNumOfCopies.setCellValueFactory((celldata->new SimpleStringProperty("0")));
		table.setItems(tkList);
		table.setOnMousePressed(e->{
			if(e.isPrimaryButtonDown() && e.getClickCount()==1) {
				txtTitleID.setText(table.getSelectionModel().getSelectedItem().getTitleID());
				txtTitle.setText(table.getSelectionModel().getSelectedItem().getTitleName());
				txtDescription.setText(table.getSelectionModel().getSelectedItem().getDesciption());

				File f = table.getSelectionModel().getSelectedItem().getImange();
				try {
					Image img = new Image(new FileInputStream(f));
				
					imageView.setImage(img);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnBack.setOnAction(e->{
			Main.changelayout("Home", e);
		});
	}

}
