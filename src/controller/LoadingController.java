package controller;

import java.net.URL;
import java.util.ResourceBundle;

import daos.MyEntityManagerFactory;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import ui.Main;

public class LoadingController implements Initializable{

    @FXML
    private ProgressBar progressBar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Load();
		
	}
	void Load() {
		progressBar.progressProperty().unbind();
		Task<Void> task = loaddata();
		progressBar.progressProperty().bind(task.progressProperty());
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent event) {
				Main.closeWindow(progressBar);
				Main.newWindow("Home", "Home Sreen");
			}
		});
		new Thread(task).start();

	}
	public Task<Void> loaddata() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				updateProgress(0, 10);
				@SuppressWarnings("unused")
				MyEntityManagerFactory em = MyEntityManagerFactory.getInstance();
				for (int i = 4; i < 10; i++) {
					updateProgress(i, 10);
					Thread.sleep(100);
				}
				Thread.sleep(100);
				updateProgress(10, 10);
				Thread.sleep(50);
				return null;

			}
		};
		return task;
	}
}


