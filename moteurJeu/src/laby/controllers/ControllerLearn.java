package laby.controllers;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;

public class ControllerLearn implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerLearn(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //clear les logs
        VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
        parentVBox.getChildren().clear();

        //TODO : faire l'apprentissage
        laby.enemies.add(new Giant(10, 10, "NewGiant"));
        laby.setLogs("Learned");

        Button nextManche = new Button("Next Manche");
        nextManche.setOnMouseClicked(new ControllerNextManche(laby));
        parentVBox.getChildren().add(nextManche);

        //laby.setPause(false); //???
    }
}
