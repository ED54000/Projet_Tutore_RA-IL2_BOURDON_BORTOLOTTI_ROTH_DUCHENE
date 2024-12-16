package com.example.chemin_interface.mains;
import com.example.chemin_interface.laby.ModeleLabyrinth;
import com.example.chemin_interface.moteur.MoteurJeu;

import java.io.IOException;


public class MainLaby {
    public static void main(String[] args) throws IOException {

        int pFPS = 100;

        // creation des objets
        ModeleLabyrinth laby = new ModeleLabyrinth();

        // lancement du jeu
        MoteurJeu.launch(laby, pFPS);
    }
}
