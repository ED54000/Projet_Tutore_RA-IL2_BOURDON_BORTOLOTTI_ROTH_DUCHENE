package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;

public class ControllerNextManche implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerNextManche(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //TODO : faire la prochaine manche
        laby.setPause(false);
    }
}
