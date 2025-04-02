package mains;

import laby.ModeleLabyrinth;
import moteur.MoteurJeu;
import moteur.TimeManagement;


public class MainLaby {
    public static void main(String[] args)  {
        int pFPS = 100;

        // creation des objets
        ModeleLabyrinth laby = new ModeleLabyrinth();

        // parametrage du moteur de jeu
        TimeManagement.setFPS(pFPS);
        MoteurJeu.setLaby(laby);

        // lancement du jeu
        MoteurJeu.launch(laby);
    }
}
