package ui;
import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
public class Main extends Application{
	private static Stage primaryStage;
	private static HashMap<String, String> listUI = new HashMap<>();
	private static Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		initLayout();
		primaryStage.getIcons().add(new Image("/resources/img/icon.png"));
		primaryStage.setTitle("Video Rental Store Application");
		primaryStage.setResizable(true);
		primaryStage.setMaximized(true);
		primaryStage.setOnCloseRequest(e -> {
			System.exit(1);
			Platform.exit();
		});

	}
	public static void initLayout() throws IOException{

		listUI.put("Home", "/resources/fxml/Home.fxml");
		listUI.put("CustomerManagement", "/resources/fxml/CustomerManagement.fxml");
		newWindow("Home", "Main Menus");

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
	
	public static void newWindow(String mapName, String titlename) {
		try {

			Stage stage = new Stage();
			Parent root = loadFXML(listUI.get(mapName)).load();
			stage.getIcons().add(new Image("/resources/img/icon.png"));
			stage.setResizable(true);
			stage.setTitle(titlename);
			stage.setScene(new Scene(root));
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initStyle(StageStyle.DECORATED);
			stage.show();

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}
	public static void closeWindow(final Object btn) {
		final Stage stage = (Stage) ((Node) btn).getScene().getWindow();
		stage.close();
	}
	public static void main(final String[] args) {
		launch(args);
	}
}
