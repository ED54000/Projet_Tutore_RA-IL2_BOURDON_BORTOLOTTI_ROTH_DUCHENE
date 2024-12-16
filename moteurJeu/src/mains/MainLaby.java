package mains;

import javafx.stage.Stage;
import laby.ModeleLabyrinth;
import moteur.MoteurJeu;

import java.io.IOException;


public class MainLaby {
    public static void main(String[] args) throws IOException {
        double width = 1500;
        double height = 1000;
        int pFPS = 100;

        // creation des objets
        //ModeleLabyrinth laby = new ModeleLabyrinth("Ressources/Labyrinthe2.txt");
        ModeleLabyrinth laby = new ModeleLabyrinth();

        // parametrage du moteur de jeu
        //MoteurJeu.setTaille(width, height);
        MoteurJeu.setFPS(pFPS);
        MoteurJeu.setLaby(laby);

        // lancement du jeu
        MoteurJeu.launch(laby);

    }
}
