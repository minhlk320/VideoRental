package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable{

	public static ProgressBar statProgressBar;
    @FXML
    private ProgressBar progressBar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		statProgressBar = progressBar;
	}
}


