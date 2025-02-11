package mains;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
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

        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("Manche;Nom;Vie;Dégâts;Vitesse\n");

            // Initialisation avec une liste d'ennemis
            //ArrayList<Ennemy> ennemies = new ArrayList<>();
            ArrayList<ArrayList<Ennemy>> groupes = new ArrayList<>();
            for (int i = 0; i < 10; i++) { // 50 groupes
                groupes.add(createEnnemies(10)); // Chaque groupe contient 5 ennemis
            }


            // Boucle sur le nombre de manches avec une population d'ennemis évoluée à chaque fois
            for (int manche = 0; manche < 15; manche++) {
                System.out.println("Manche " + manche);

                // Création d'une HashMap avec pour clé l'ennemi et pour valeur son score
                HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
                for (ArrayList<Ennemy> groupe : groupes) {
                    stats.put(groupe, 0.0);
                }

                // Écriture des stats avant l'évolution
                //logStats(writer, manche, ennemies);
                //Avant l'évolution
                System.out.println("Avant l'évolution");
                for (ArrayList<Ennemy> groupe : groupes) {
                    System.out.println("Groupe : ");
                    for (Ennemy ennemy : groupe) {
                        System.out.println(ennemy.getName() + " : " + ennemy.getHealth() + " " + ennemy.getDamages() + " " + ennemy.getSpeed());
                    }
                }

                // On évolue
                Evolution evolution = new Evolution();
                groupes = evolution.evolve(evolution.evaluate(stats));

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
        for (Ennemy ennemy : ennemies) {
            writer.write(manche + ";" + ennemy.getName() + ";" +
                    String.format("%.2f", ennemy.getHealth()) + ";" +
                    String.format("%.2f", ennemy.getDamages()) + ";" +
                    String.format("%.2f", ennemy.getSpeed()) + "\n");
        }
    }
}
