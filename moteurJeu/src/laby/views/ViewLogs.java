package laby.views;

import entites.enemies.Ennemy;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
            //System.out.println("Le labbel : "+label);
            //System.out.println("Container : "+logs.getChildren());
            VBox vbox = (VBox) logs.getChildren().get(1);
            //System.out.println("Sous VBOx : "+vbox.getChildren());
            vbox.getChildren().add(label);

            if (laby.isPause() && laby.getLogs().matches("Manche \\d+ terminée")) {
                Button button = new Button("Learn");
                button.setOnMouseClicked(new ControllerLearn(laby)); // a cérer dans le moteur avec les vues ?
                vbox.getChildren().add(button);
            }
            laby.setLogs("");
        }
    }
}
