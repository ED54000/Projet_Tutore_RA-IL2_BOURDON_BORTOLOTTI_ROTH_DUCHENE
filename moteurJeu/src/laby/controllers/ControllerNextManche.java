package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;

public class ControllerNextManche implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerNextManche(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //clear les logs
        VBox parentVBox = (VBox) ((Button) mouseEvent.getSource()).getParent();
        parentVBox.getChildren().clear();
        //TODO : faire la prochaine manche
        laby.setPause(false);
    }
}
