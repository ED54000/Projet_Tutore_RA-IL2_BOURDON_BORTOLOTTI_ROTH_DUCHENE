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
        System.out.println(this.logs.getChildren());
        if (laby.getLogs()!=""){
            Label label = new Label(laby.getLogs());
            logs.getChildren().add(label);
            //(VBox)(logs.getChildren().get(1).add(label));

            if (laby.isPause()&&laby.getLogs().equals("Manche terminée")){
                Button button = new Button("Learn");
                button.setOnMouseClicked(new ControllerLearn(laby)); // a cérer dans le moteur avec les vues ?
                logs.getChildren().add(button);
            }
            laby.setLogs("");
        }
    }
}
