package laby.views;

import laby.ModeleLabyrinth;
import laby.Observer;
import laby.Subject;

public class ViewLogs implements Observer {
    private ModeleLabyrinth laby;

    public ViewLogs(ModeleLabyrinth laby) {
        this.laby = laby;
    }

    @Override
    public void update(Subject s) {
        ModeleLabyrinth laby = (ModeleLabyrinth) s;
        System.out.println("Labyrinth updated");
    }
}
