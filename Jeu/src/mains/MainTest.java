package mains;

import entites.enemies.Giant;
import evolution.EnnemyEvolutionv2;
import javafx.application.Application;
import javafx.stage.Stage;
import laby.ModeleLabyrinth;

import java.util.HashMap;

public class MainTest extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.creerLabyrinthePour1("Ressources/Labyrinthe3.txt", 1);
        jeu.setSimulation(false);
        int population = 10;

        //boucle sur le nombre d'itérations d'évolution
        for (int y = 0; y < 5; y++) {
            //boucle sur le nombre de simulations
            for (int i = 0; i < population; i++) {
                jeu.setStartTime();
                System.out.println("Start Time : "+jeu.getStartTime());

                long lastUpdateTime = System.nanoTime();
                //System.out.println("Jeu fini : " + jeu.etreFini());
                while (!jeu.etreFini()) {
                    long currentTime = System.nanoTime();
                    double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

                    jeu.update(elapsedTimeInSeconds);
                    lastUpdateTime = currentTime;
                }


                System.out.println("Simulation numéro " + i + " terminée.");
                System.out.println("Map Score :"+jeu.getScore()+"\n");
                System.out.println("Défenses en fin de manche: "+jeu.getDefenseEndOfManche());
                jeu.setDefenses(jeu.getDefenseEndOfManche());
                jeu.refresh(i);

                jeu.setEnd(false);

            }
            System.out.println("Toutes les simulations sont terminées.");
            System.out.println(jeu.getScore());

            // On évolue les ennemis
            EnnemyEvolutionv2 evolution = new EnnemyEvolutionv2();
            jeu.setEnnemiesEvolved(evolution.evolve(jeu.getScore()));
            jeu.refresh(y);
            jeu.refreshEnnemiesScore();
        }
    }
}
