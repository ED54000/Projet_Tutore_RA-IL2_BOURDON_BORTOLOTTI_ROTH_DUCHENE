package laby.views;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewGraphicsWindow implements Observer {
    private Stage stage = new Stage();
    private ViewGraphiqueDirect graphique;
    private VBox root = new VBox(10);

    public ViewGraphicsWindow(ViewGraphiqueDirect graphique) {
        // Create a new window
        stage.setTitle("Graphiques");
        this.graphique = graphique;

        // Create and set the scene
        Scene scene = new Scene(root, 1200, 400);
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    @Override
    public void update(Subject s) {
        if (!root.getChildren().contains(graphique.getGraphique())){
            root.getChildren().add(graphique.getGraphique());
        } else {
            root.getChildren().set(0,graphique.getGraphique());
        }
    }
}