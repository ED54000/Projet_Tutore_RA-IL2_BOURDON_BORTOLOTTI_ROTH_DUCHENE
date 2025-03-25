package mains;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import evolution.EvolutionSteering;
import javafx.application.Application;
import javafx.stage.Stage;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainSimuSteering extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialisation avec une liste d'ennemis
        ArrayList<ArrayList<Ennemy>> groupes = new ArrayList<>();
        for (int i = 0; i < 50; i++) { //50 groupes d'un géant
            groupes.add(new ArrayList<>(List.of(new Giant(new Vector2D(0, 0), "Giant"))));
        }

        // Nombre de manches (itération d'évolution)
        for (int i = 0; i < 20; i++) {
            HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
            for (ArrayList<Ennemy> groupe : groupes) {
                stats.put(groupe, 0.0);
            }

            EvolutionSteering evolution = new EvolutionSteering();
            stats = evolution.evaluate(stats);

            if (stats == null) {
                System.out.println("Les ennemies ont gagné la partie");
                break;
            }

            groupes = evolution.evolve(stats);
        }
        System.out.println("Fin bitch");
    }
}
