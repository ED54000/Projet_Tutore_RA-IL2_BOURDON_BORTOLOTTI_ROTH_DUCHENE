package com.example.chemin_interface.laby.views;

import com.example.chemin_interface.laby.ModeleLabyrinth;
import com.example.chemin_interface.laby.Observer;
import com.example.chemin_interface.laby.Subject;

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
