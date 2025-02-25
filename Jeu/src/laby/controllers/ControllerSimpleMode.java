package laby.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import laby.ModeleLabyrinth;

public class ControllerSimpleMode implements EventHandler<MouseEvent> {

    ModeleLabyrinth laby;

    public ControllerSimpleMode(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void handle(MouseEvent event) {

    }
}
