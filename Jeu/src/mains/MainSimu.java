package mains;

import entites.enemies.*;
import evolution.Evolution;
import javafx.application.Application;
import javafx.stage.Stage;
import steering_astar.Steering.Vector2D;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static laby.ModeleLabyrinth.createEnnemies;

public class MainSimu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Fichier de sortie pour les logs
        String fileName = "Ressources/evolution_stats.csv";

        try (FileWriter writer = new FileWriter(fileName)){
            //écrit les différents ennemis
            writer.write("Manche;Giant;Ninja;Druide;Berserker\n");

            // Initialisation avec une liste d'ennemis
            ArrayList<ArrayList<Ennemy>> groupes = new ArrayList<>();
            for (int i = 0; i < 40; i++) { // 50 groupes
                groupes.add(createEnnemies(20)); // Chaque groupe contient 5 ennemis
            }


            // Boucle sur le nombre de manches avec une population d'ennemis évoluée à chaque fois
            for (int manche = 0; manche < 100; manche++) {
                System.out.println("Manche " + manche);

                // Création d'une HashMap avec pour clé l'ennemi et pour valeur son score
                HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
                for (ArrayList<Ennemy> groupe : groupes) {
                    stats.put(groupe, 0.0);
                }

                // On évolue
                Evolution evolution = new Evolution();
                stats = evolution.evaluate(stats);
                if (stats == null) {
                    System.out.println("Les ennemies ont gagé la partie");
                    writer.write("Les ennemies ont gagné la partie");
                }
                groupes = evolution.evolve(stats);

                //après l'évolution
                System.out.println("Après l'évolution");
                for (ArrayList<Ennemy> groupe : groupes) {
                    System.out.println("Groupe : ");
                    for (Ennemy ennemy : groupe) {
                        System.out.println(ennemy.getName() + " : " + ennemy.getHealth() + " " + ennemy.getDamages() + " " + ennemy.getSpeed());
                    }
                }


                // Écriture des stats après l'évolution
                //TODO : A réadapter pour les groupes
                logStats(writer, manche + 1, groupes.get(0));
            }
            System.out.println("Fin de la simulation. Données enregistrées dans " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logStats(FileWriter writer, int manche, ArrayList<Ennemy> ennemies) throws IOException {
        int nbGiant = 0;
        int nbNinja = 0;
        int nbDruide = 0;
        int nbBersrker = 0;
        for (Ennemy ennemy : ennemies) {
            if (ennemy instanceof Giant) {
                nbGiant++;
            } else if (ennemy instanceof Ninja) {
                nbNinja++;
            } else if (ennemy instanceof Druide) {
                nbDruide++;
            } else if (ennemy instanceof Berserker) {
                nbBersrker++;
            }
        }
        writer.write(manche + ";" + nbGiant + ";" + nbNinja + ";" + nbDruide + ";" + nbBersrker + "\n");
    }
}
