package mains;

import javafx.application.Application;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

public class MainTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.creerLabyrinthePour1("Ressources/Labyrinthe3.txt", 1);
        jeu.setSimulation(false);
        int population = 10;

        //boucle pour lancer une partie pour chaque ennemi de la population
        for (int i = 0; i < population; i++) {
            long lastUpdateTime = System.nanoTime();
            System.out.println("Jeu fini : " + jeu.etreFini());
            while (!jeu.etreFini()) {
                long currentTime = System.nanoTime();
                double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

                jeu.update(elapsedTimeInSeconds);
                lastUpdateTime = currentTime;
            }
            System.out.println("Simulation numéro " + i + " terminée.");
            jeu.setEnd(false);
            jeu.creerLabyrinthePour1("Ressources/Labyrinthe3.txt", 1);

        }


    }
}
