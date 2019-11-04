package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import daos.RateDAO;
import daos.TitleDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
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
	private Text lbTitle;

	@FXML
	private JFXTextArea txtDescription;

	@FXML
	private Text lbDescription;

	@FXML
	private JFXButton btnNew;

	@FXML
	private JFXButton btnSave;

	@FXML
	private JFXButton btnDelete;

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
	private TableColumn<Title, String> colDescription;
	private TitleDAO titleDAO;
	private List<Title> listTitles;
	private String url_image;
	private File image;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Main main = Main.getInstance();
		titleDAO = main.getTitleDAO();
		listTitles = titleDAO.getAll(Title.class);
		lbTitle.setText("");
		lbDescription.setText("");
		loadTable(listTitles);
		ShowItemClass();

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
				reloadTable();

			}
			Title getCurrentTitle(Title title) {
				String titleName = txtTitle.getText();
				String des = txtDescription.getText();
				Rate rate = cbItemClass.getValue();
				title.setImage(image);
				title.setDescription(des);
				title.setTitleName(titleName);
				title.setItemClass(rate);
				return title;
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
				Title title = table.getSelectionModel().getSelectedItem();
				boolean x = titleDAO.delete(title);
				if(x){
					System.out.println("Deleted !");
				}else{
					System.out.println("Delete Failed ! ");
				}
				reloadTable();
			}
		});

		table.setOnMousePressed(e->{
			if(e.isPrimaryButtonDown() && e.getClickCount()==1) {
				txtTitleID.setText(table.getSelectionModel().getSelectedItem().getTitleID());
				txtTitle.setText(table.getSelectionModel().getSelectedItem().getTitleName());
				txtDescription.setText(table.getSelectionModel().getSelectedItem().getDesciption());
				cbItemClass.setValue(table.getSelectionModel().getSelectedItem().getItemClass());
				imageView.setImage(table.getSelectionModel().getSelectedItem().getImage());
			}
		});
	}


	private void loadTable(List<Title> list) {
		ObservableList<Title> tkList = FXCollections.observableArrayList(list);
		colTitleID.setSortable(false);
		colTitleID.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleID()));
		colTitle.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getTitleName()));
		colDescription.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getDesciption()));
		colNumOfCopies.setCellValueFactory((celldata->new SimpleStringProperty("0")));
		colItemClass.setCellValueFactory(celldata->new SimpleStringProperty(celldata.getValue().getItemClass().getItemClassName()));
		table.setItems(tkList);
	}

	private void reloadTable(){
		table.getColumns().clear();
		table.getColumns().addAll(colTitleID,colTitle,colNumOfCopies,colDescription);
		listTitles = titleDAO.getAll(Title.class);
		loadTable(listTitles);
	}
	private void ShowItemClass(){
		ObservableList<Rate> listRate = FXCollections.observableArrayList(new RateDAO().getAll(Rate.class));
		cbItemClass.setItems(listRate);
		cbItemClass.getSelectionModel().select(-1);
	}

	private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {

		// Set tiêu đề cho DirectoryChooser
		directoryChooser.setTitle("Select your poster");


		// Sét thư mục bắt đầu nhìn thấy khi mở DirectoryChooser
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}

}
