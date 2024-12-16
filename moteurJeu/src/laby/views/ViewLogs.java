package laby.views;

import javafx.scene.layout.VBox;
import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewLogs implements Observer {
    private ModeleLabyrinth laby;

    public ViewLogs(ModeleLabyrinth laby, VBox logs) {
        this.laby = laby;
    }

    @Override
    public void update(Subject s) {

        System.out.println("Labyrinth updated");
    }
}
