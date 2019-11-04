package ui;

import com.sun.javafx.application.LauncherImpl;
import daos.MyEntityManagerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
public class Main extends Application{
	public static final String SCENE_LATE_CHARGE_INFO = "LateChargeInfo";
	public static final String URL_LATE_CHARGE_INFO = "/resources/fxml/LateChargeInfo.fxml";
	public static final String TITLE_LATE_CHARGE_INFO = "Late Charge List";
	public static final String MAIN_LAYOUT = "/resources/fxml/MainLayout.fxml";
	private static final double MIN_HEIGHT = 720;
	private static final double MIN_WIDTH = 1280;
	private static final int COUNT_LIMIT = 10;
	public static Image MAIN_ICON = new Image("/resources/img/icon.png");
	private static Stage primaryStage;
	private static HashMap<String, String> listUI = new HashMap<>();
	private static Main mainInstance;
	public final String SCENE_HOME = "Home";
	public final String SCENE_CUSTOMER_MANAGEMENT = "CustomerManagement";
	public final String SCENE_TITLE_MANAGEMENT = "TitleManagement";
	public final String SCENE_LOADING = "Loading";
	public final String SCENE_ITEM_MANAGEMENT = "ItemManagement";
	public final String URL_HOME = "/resources/fxml/Home.fxml";
	public final String URL_CUSTOMER_MANAGEMENT = "/resources/fxml/CustomerManagement.fxml";
	public final String URL_TITLE_MANAGEMENT = "/resources/fxml/TitleManagement.fxml";
	public final String URL_ITEM_MANAGEMENT = "/resources/fxml/ItemManagement.fxml";
    public final String SCENE_RENTAL_ITEMS = "RentalItems";
    public final String URL_RENTAL = "/resources/fxml/RentalItem.fxml";
	@FXML
	public BorderPane root;

	public Main() {
		mainInstance = this;
	}

	public static Main getInstance() {
		return mainInstance;
	}

	public static void main(final String[] args) {
		LauncherImpl.launchApplication(Main.class, MyPreloader.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		root = loadFXML(MAIN_LAYOUT).load();
		primaryStage.setScene(new Scene(root));
		primaryStage.getIcons().add(MAIN_ICON);
		primaryStage.setTitle("Video Rental Store Application");
		primaryStage.setResizable(true);
		//primaryStage.setMaximized(true);
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			System.exit(1);
			Platform.exit();
		});

	}

	@Override
	public void init() throws Exception {
		initLayout();
		MyEntityManagerFactory.getInstance();
		for (int i = 1; i <= COUNT_LIMIT; i++) {
			double progress = (double) i / 10;
			LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
			Thread.sleep(100);
		}
	}

	public void initLayout() {
		listUI.put(SCENE_HOME, URL_HOME);
		listUI.put(SCENE_CUSTOMER_MANAGEMENT,URL_CUSTOMER_MANAGEMENT);
		listUI.put(SCENE_TITLE_MANAGEMENT, URL_TITLE_MANAGEMENT);
		listUI.put(SCENE_ITEM_MANAGEMENT, URL_ITEM_MANAGEMENT);
        listUI.put(SCENE_RENTAL_ITEMS, URL_RENTAL);
		listUI.put(SCENE_LATE_CHARGE_INFO, URL_LATE_CHARGE_INFO);
	}

	public FXMLLoader loadFXML(String url) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(url));
		return loader;
	}

	//	public void changeScene(final String value) {
//		try {
//			Parent root = loadFXML(listUI.get(value)).load();
//			Scene scene = new Scene(root);
//			primaryStage.setScene(scene);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
	public void changeScene(final String value) {
		try {
			Parent part = loadFXML(listUI.get(value)).load();
			root.setCenter(part);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void newWindow(String mapName, String titlename) {
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
			stage.initOwner(primaryStage);
			stage.show();

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public void closeWindow(final Object btn) {
		final Stage stage = (Stage) ((Node) btn).getScene().getWindow();
		stage.close();
	}

	public void enableWindow() {
		primaryStage.show();
	}

	public void disableWindow() {
		primaryStage.hide();

	}

	public void closeCenter() {
		root.setCenter(null);
	}
}
