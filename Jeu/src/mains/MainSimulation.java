package mains;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

public class MainSimulation extends Application {
    private double FPS = 100;
    private double dureeFPS = 1000 / (FPS + 1);


    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.setSimulation(true);
        jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", 50, 1000, 1200);

        // Lancer la simulation dans un thread séparé
        new Thread(() -> {
            long lastUpdateTime = System.nanoTime();

            while (!jeu.etreFini()) {
                // Calculer le temps écoulé depuis la dernière mise à jour
                long currentTime = System.nanoTime();
                double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

                // Mettre à jour la simulation
                jeu.update(elapsedTimeInSeconds);

                // Mettre à jour le temps de la dernière mise à jour
                lastUpdateTime = currentTime;
            }
            System.out.println(jeu.getLogs());
            System.out.println("Simulation terminée.");
        }).start();
    }


}
