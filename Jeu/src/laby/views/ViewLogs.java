package laby.views;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;
import laby.controllers.ControllerLearn;

public class ViewLogs implements Observer {
    private ModeleLabyrinth laby;
    private VBox logs;

    public ViewLogs(ModeleLabyrinth laby, VBox logs) {
        this.laby = laby;
        //this.logs = (VBox)(logs.getChildren().get(1));
        this.logs = logs;
    }

    @Override
    public void update(Subject s) {
        if (!laby.getLogs().isEmpty()) {
            Label label = new Label(laby.getLogs());
            ScrollPane scrollPane = (ScrollPane) logs.getChildren().get(1);

            VBox vbox = (VBox) scrollPane.getContent();
            vbox.getChildren().add(label);

            if (laby.getPause() && laby.getLogs().matches("Manche \\d+ terminée")) {
                System.out.println("Simulation ? : " + laby.estSimulation());
                Button button = new Button("Learn");
                button.setOnMouseClicked(new ControllerLearn(laby)); // a cérer dans le moteur avec les vues ?
                vbox.getChildren().add(button);
            }
            laby.setLogs("");
        }
    }
}
