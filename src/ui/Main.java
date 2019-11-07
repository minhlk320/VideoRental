package ui;

import com.sun.javafx.application.LauncherImpl;
import controller.LateChargeInfoController;
import daos.*;
import entities.Customer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

/**
 *
 */
public class Main extends Application{
	private static Stage primaryStage;
	private static HashMap<String, String> listUI = new HashMap<>();
	private static Main mainInstance;
	public final String SCENE_LOGIN = "Login";
	public final String URL_LOGIN = "/resources/fxml/Login.fxml";
	public final String TITLE_LOGIN = "Login";
	public final String TITLE_RETURN_ITEM = "returnItem Item";
	public final String TITLE_LATE_CHARGE_INFO = "Late Charge List";
	public final String TITLE_RESERVATION = "Make Reservation";
	public final String SCENE_HOME = "Home";
	public final String SCENE_CUSTOMER_MANAGEMENT = "CustomerManagement";
	public final String SCENE_TITLE_MANAGEMENT = "TitleManagement";
	public final String SCENE_LOADING = "Loading";
	public final String SCENE_ITEM_MANAGEMENT = "ItemManagement";
	public final String SCENE_RENTAL_ITEMS = "RentalItems";
	public final String SCENE_RESERVATION = "Reservation";
	public final String SCENE_RESERVATION_MANAGEMENT = "ReservationManagement";
	public final String SCENE_RETURN_ITEM = "ReturnItem";
	public final String SCENE_LATE_CHARGE_INFO = "LateChargeInfo";
	public final String URL_LATE_CHARGE_INFO = "/resources/fxml/LateChargeInfo.fxml";
	public final String URL_MAIN_LAYOUT = "/resources/fxml/MainLayout.fxml";
	public final String URL_DEFAULT_POSTER = "src/resources/img/default_poster.jpg";
	public final String URL_HOME = "/resources/fxml/Home.fxml";
	public final String URL_CUSTOMER_MANAGEMENT = "/resources/fxml/CustomerManagement.fxml";
	public final String URL_TITLE_MANAGEMENT = "/resources/fxml/TitleManagement.fxml";
	public final String URL_ITEM_MANAGEMENT = "/resources/fxml/ItemManagement.fxml";
	public final String URL_RENTAL = "/resources/fxml/RentalItem.fxml";
	public final String URL_RESERVATION = "/resources/fxml/Reservation.fxml";
	public final String URL_RESERVATION_MANAGEMENT = "/resources/fxml/ReservationList.fxml";
	public final String URL_RETURN_ITEM = "/resources/fxml/ReturnItem.fxml";
	public final String TITLE_RENTAL_ITEM = "Rental Item";
	public final double MIN_HEIGHT = 720;
	public final double MIN_WIDTH = 1280;
	private final String USER = "admin";
	private final String PASSWORD = "admin";
	private final int TOTAL_PROGRESS = 3;
	public Image MAIN_ICON = new Image("/resources/img/icon.png");
	@FXML
	public BorderPane root;
	public boolean isLogged = false;
	private ItemDAO itemDAO;
	private CustomerDAO customerDAO;
	private RentalDAO rentalDAO;
	private TitleDAO titleDAO;
	private LateChargeDAO lateChargeDAO;
	private ReservationDAO reservationDAO;
	private RateDAO rateDAO;

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
		root = loadFXML(URL_MAIN_LAYOUT).load();
		root.setCenter(loadFXML(URL_ITEM_MANAGEMENT).load());
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
	public void init() {
		try {
			int step = 1;
			MyEntityManagerFactory.getInstance();
			updateProgress(step);
			step++;
			initDAO();
			updateProgress(step);
			step++;
			initLayout();
			updateProgress(step);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return root
	 */
	public BorderPane getRoot() {
		return root;
	}

	/**
	 *
	 * @param step count for initial data
	 * @throws Exception
	 *
	 */
	private void updateProgress(int step) throws Exception {
		double progress = (double) (TOTAL_PROGRESS - (TOTAL_PROGRESS - step)) / TOTAL_PROGRESS;
		LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
		Thread.sleep(500);
	}

	/**
	 * Initial DAO for recall
	 */
	public void initDAO() {
		itemDAO = new ItemDAO();
		customerDAO = new CustomerDAO();
		rentalDAO = new RentalDAO();
		titleDAO = new TitleDAO();
		lateChargeDAO = new LateChargeDAO();
		reservationDAO = new ReservationDAO();
		rateDAO = new RateDAO();
	}

	/**
	 * Initial Layout ( put URL to list and call for use )
	 *
	 * @throws IOException
	 */
	public void initLayout() throws IOException {
		listUI.put(SCENE_CUSTOMER_MANAGEMENT, URL_CUSTOMER_MANAGEMENT);
		listUI.put(SCENE_RENTAL_ITEMS, URL_RENTAL);
		listUI.put(SCENE_RETURN_ITEM, URL_RETURN_ITEM);
		listUI.put(SCENE_TITLE_MANAGEMENT, URL_TITLE_MANAGEMENT);
		listUI.put(SCENE_ITEM_MANAGEMENT, URL_ITEM_MANAGEMENT);
		listUI.put(SCENE_LATE_CHARGE_INFO, URL_LATE_CHARGE_INFO);
		listUI.put(SCENE_RESERVATION, URL_RESERVATION);
		listUI.put(SCENE_RESERVATION_MANAGEMENT, URL_RESERVATION_MANAGEMENT);
	}

	/**
	 *
	 * @return ReservationDAO
	 */
	public ReservationDAO getReservationDAO() {
		return reservationDAO;
	}

	/**
	 *
	 * @return CustomerDAO
	 */
	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	/**
	 *
	 * @return ItemDAO
	 */
	public ItemDAO getItemDAO() {
		return itemDAO;
	}

	/**
	 *
	 * @return RentalDAO
	 */
	public RentalDAO getRentalDAO() {
		return rentalDAO;
	}

	/**
	 *
	 * @return TitleDAO
	 */
	public TitleDAO getTitleDAO() {
		return titleDAO;
	}

	/**
	 *
	 * @return LateChargeDAO
	 */
	public LateChargeDAO getLateChargeDAO() {
		return lateChargeDAO;
	}

	/**
	 * @return RateDAO
	 */
	public RateDAO getRateDAO() {
		return rateDAO;
	}

	/**
	 *	load FXML from URL to FXMLoader
	 * @param url
	 * @return FXMLoader
	 */
	public FXMLLoader loadFXML(String url) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource(url));
		return loader;
	}

	/**
	 * Change content of main scene
	 * @param value
	 */
	public void changeScene(final String value) {
		try {
			Parent part = loadFXML(listUI.get(value)).load();
			root.setCenter(part);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * Check login
	 *
	 * @param username
	 * @param password
	 * @return true if username and password matched
	 */
	public boolean checkLogin(String username, String password) {
		if ((username.equals(USER)) && (password.equals(PASSWORD))) {
			isLogged = true;
			return true;
		}
		return false;
	}

	/**
	 * Open window form for login
	 */
	public void doLogin() {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = loadFXML(URL_LOGIN);
			Parent root = loader.load();
			root.getStylesheets().setAll(this.root.getStylesheets());
			root.getStyleClass().setAll(this.root.getStyleClass());
			Scene scene = new Scene(root);
			stage.setTitle(TITLE_LOGIN);
			scene.setFill(Color.TRANSPARENT);
			stage.setResizable(true);
			stage.getIcons().add(MAIN_ICON);
			stage.setScene(scene);
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Logout
	 *
	 * @return
	 */
	public boolean doLogout() {
		if (isLogged) {
			isLogged = false;
			return true;
		}
		return false;
	}

	/**
	 * Show message
	 * @param message
	 * @param title
	 * @param header
	 */
	public void showMessage(String message, String title, String header) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Display late charge scene ( popup window)
	 * @param customer
	 */
	public void displayLateCharge(Customer customer) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = loadFXML(URL_LATE_CHARGE_INFO);
			if (customer != null) {
				loader.setControllerFactory((Class<?> controllerType) -> {
					if (controllerType == LateChargeInfoController.class) {
						LateChargeInfoController controller = new LateChargeInfoController();
						controller.setCurrentCustomer(customer);
						return controller;
					} else {
						try {
							return controllerType.newInstance();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setTitle(TITLE_LATE_CHARGE_INFO);
			scene.setFill(Color.TRANSPARENT);
			stage.setResizable(true);
			stage.getIcons().add(MAIN_ICON);
			stage.setScene(scene);
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();

		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Open new window (scene contain in list (name = key) )
	 * @param name
	 * @param title
	 */
	public void openWindow(String name, String title) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = loadFXML(listUI.get(name));
			Parent root = loader.load();
			root.getStylesheets().setAll(this.root.getStylesheets());
			root.getStyleClass().setAll(this.root.getStyleClass());
			Scene scene = new Scene(root);
			stage.setTitle(title);
			scene.setFill(Color.TRANSPARENT);
			stage.setResizable(true);
			stage.getIcons().add(MAIN_ICON);
			stage.setScene(scene);
			stage.initStyle(StageStyle.DECORATED);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(primaryStage);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close window
	 */
	public void closeWindow(final Object btn) {
		final Stage stage = (Stage) ((Node) btn).getScene().getWindow();
		stage.close();
		stage.setScene(null);
	}

	/**
	 * Clear center content
	 */
	public void closeCenter() {
		root.setCenter(null);
	}
}
