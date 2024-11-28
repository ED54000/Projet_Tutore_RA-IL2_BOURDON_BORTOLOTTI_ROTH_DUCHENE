package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;

public class ControllerStart implements EventHandler<MouseEvent> {
    private ModeleLabyrinth laby;

    public ControllerStart(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //TODO
    }
}
