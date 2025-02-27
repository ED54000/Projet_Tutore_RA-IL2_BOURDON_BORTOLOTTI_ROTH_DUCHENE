package laby.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewLoading {
    private Stage popupStage;
    private ProgressBar progressBar;
    private Label progressLabel;

    public ViewLoading(Stage parentStage, int totalSimulations) {
        popupStage = new Stage();
        popupStage.setAlwaysOnTop(true);  // Garder la popup au premier plan
        popupStage.initOwner(parentStage);
        popupStage.setTitle("Exécution en cours...");

        progressLabel = new Label("Exécution en cours...");
        progressBar = new ProgressBar();
        progressBar.setProgress(-1); // Mode indéterminé (barre animée)

        VBox layout = new VBox(10, progressLabel, progressBar);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 150);
        popupStage.setScene(scene);
    }

    // Afficher la fenêtre de chargement
    public void show() {
        popupStage.show();
    }

    // Fermer la fenêtre de chargement
    public void close() {
        popupStage.close();
    }
}
