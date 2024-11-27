package mains;

import laby.Labyrinth;
import moteur.MoteurJeu;

import java.io.IOException;


public class MainLaby {
    public static void main(String[] args) throws IOException {
        double width = 1000;
        double height = 1000;
        int pFPS = 10;

        // creation des objets
        Labyrinth laby = new Labyrinth("Ressources/Labyrinthe2.txt");

        // parametrage du moteur de jeu
        MoteurJeu.setTaille(width, height);
        MoteurJeu.setFPS(pFPS);
        MoteurJeu.setLaby(laby);

        // lancement du jeu
        MoteurJeu.launch(laby);

    }
}
