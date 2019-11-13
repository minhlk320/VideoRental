package ui;


import controller.LoadingController;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyPreloader extends Preloader {
    public final String URL_LOADING = "/resources/fxml/Loading.fxml";
    public Image ICON = new Image("/resources/img/icon.png");
    private Stage preloaderStage;
    private Scene scene;
    public MyPreloader() {

    }

    @Override
    public void init() throws Exception {
        Parent root1 = loadFXML(URL_LOADING).load();
        scene = new Scene(root1);
    }

    @Override
    public void start(Stage primaryStage) {
        this.preloaderStage = primaryStage;
        preloaderStage.getIcons().add(ICON);
        preloaderStage.setScene(scene);
        preloaderStage.initStyle(StageStyle.UNDECORATED);
        preloaderStage.show();


    }

    @Override
    public void handleApplicationNotification(Preloader.PreloaderNotification info) {

        if (info instanceof ProgressNotification) {
            System.out.println("Value@ :" + ((ProgressNotification) info).getProgress());
            LoadingController.statProgressBar.setProgress(((ProgressNotification) info).getProgress());
        }


    }

    @Override
    public void handleStateChangeNotification(Preloader.StateChangeNotification info) {

        StateChangeNotification.Type type = info.getType();
        switch (type) {

            case BEFORE_START:
                System.out.println("BEFORE_START");
                preloaderStage.hide();
                break;
        }

    }

    public FXMLLoader loadFXML(String url) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(url));
        return loader;
    }
}

