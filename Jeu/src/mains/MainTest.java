package mains;

import javafx.application.Application;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

public class MainTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.setSimulation(true);
        jeu.creerLabyrinthePour1("Ressources/Labyrinthe3.txt", 1);
        int i = 0;
        int population = 5;

        //boucle pour lancer une partie pour chaque ennemi de la population
        while (i < population) {

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
                System.out.println(jeu.getLogs());
            }).start();
        }
    }
}
