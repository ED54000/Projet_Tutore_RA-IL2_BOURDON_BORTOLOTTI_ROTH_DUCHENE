package laby.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewLoading {
    private Stage stage;
    private ProgressBar progressBar;
    private Label progressLabel;

    public ViewLoading(Stage parentStage) {
        stage = new Stage();
        stage.initOwner(parentStage);
        stage.setTitle("Chargement...");

        progressLabel = new Label("Evolution en cours...");
        progressBar = new ProgressBar();
        progressBar.setProgress(-1);  // Mode indéterminé

        VBox layout = new VBox(10, progressLabel, progressBar);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
