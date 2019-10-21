package ui;
import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class Main extends Application{
	private static Stage primaryStage;
	public final static String  SCENE_HOME = "Home";
	public final static String SCENE_CUSTOMER_MANAGEMENT = "CustomerManagement";
	public final static String SCENE_TITLE_MANAGEMENT = "TitleManagement";
	public final static String SCENE_LOADING = "Loading";
	public final static String URL_HOME = "/resources/fxml/Home.fxml";
	public final static String URL_CUSTOMER_MANAGEMENT = "/resources/fxml/CustomerManagement.fxml";
	public final static String URL_TITLE_MANAGEMENT = "/resources/fxml/TitleManagement.fxml";
	public final static String URL_LOADING = "/resources/fxml/Loading.fxml";
	public final static String TITLE_LOADING = "Loading";
	public static Image MAIN_ICON = new Image("/resources/img/icon.png");
	private static HashMap<String, String> listUI = new HashMap<>();
	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		initLayout();
		primaryStage.setScene(new Scene(loadFXML(URL_HOME).load()));
		primaryStage.getIcons().add(MAIN_ICON);
		primaryStage.setTitle("Video Rental Store Application");
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(e -> {
			System.exit(1);
			Platform.exit();
		});

	}
	public void initLayout() throws IOException{
		listUI.put(SCENE_HOME, URL_HOME);
		listUI.put(SCENE_CUSTOMER_MANAGEMENT,URL_CUSTOMER_MANAGEMENT);
		listUI.put(SCENE_TITLE_MANAGEMENT, URL_TITLE_MANAGEMENT);
		listUI.put(SCENE_LOADING, URL_LOADING);
		newWindow(SCENE_LOADING, TITLE_LOADING);

	}
	public static FXMLLoader loadFXML(String url) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(url));
		return loader;
	}
	public static void changeLayout(final String value) {
		try {
			Parent root = loadFXML(listUI.get(value)).load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void changelayout(String name, Event event) {
		Stage window;
		Parent fxml;
		try {
			fxml = loadFXML(listUI.get(name)).load();
			Scene scene = new Scene(fxml);
			scene.setFill(Color.TRANSPARENT);
			window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	public static void newWindow(String mapName, String titlename) {
		try {

			Stage stage = new Stage();
			Parent root = loadFXML(listUI.get(mapName)).load();
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			stage.getIcons().add(MAIN_ICON);
			stage.setResizable(true);
			stage.setTitle(titlename);
			stage.setScene(scene);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.DECORATED);
			if(mapName==SCENE_LOADING)
				stage.initStyle(StageStyle.TRANSPARENT);
			stage.show();

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}
	public static void closeWindow(final Object btn) {
		final Stage stage = (Stage) ((Node) btn).getScene().getWindow();
		stage.close();
	}
	public static void enableWindow() {
		primaryStage.show();
	}
	public static void disableWindow(){
		primaryStage.hide();
		
	}
	
	public static void main(final String[] args) {
		launch(args);
	}
}
