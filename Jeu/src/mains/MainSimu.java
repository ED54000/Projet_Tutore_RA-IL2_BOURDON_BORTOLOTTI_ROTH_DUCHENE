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

public class MainSimu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Fichier de sortie pour les logs
        String fileName = "Ressources/evolution_stats.csv";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Manche;Nom;Vie;Dégâts;Vitesse\n");

            // Initialisation avec une liste d'ennemis
            ArrayList<Ennemy> ennemies = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                Giant giant = new Giant(new Vector2D(0, 0), "Giant " + i);
                giant.setSprite(null);
                ennemies.add(giant);
            }

            // Boucle sur le nombre de manches avec une population d'ennemis évoluée à chaque fois
            for (int manche = 0; manche < 100; manche++) {
                System.out.println("Manche " + manche);

                // Création d'une HashMap avec pour clé l'ennemi et pour valeur son score
                HashMap<Ennemy, Double> stats = new HashMap<>();
                for (Ennemy ennemy : ennemies) {
                    stats.put(ennemy, 0.0);
                }

                // Écriture des stats avant l'évolution
                //logStats(writer, manche, ennemies);

                // On évolue
                Evolution evolution = new Evolution();
                ennemies = evolution.evolve(evolution.evaluate(stats));

                // Écriture des stats après l'évolution
                logStats(writer, manche + 1, ennemies);
            }

            System.out.println("Fin de la simulation. Données enregistrées dans " + fileName);
        } catch (IOException e) {
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
