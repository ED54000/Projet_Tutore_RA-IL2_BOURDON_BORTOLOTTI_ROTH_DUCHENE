package mains;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import evolution.EvolutionSteering;
import javafx.application.Application;
import javafx.stage.Stage;
import steering_astar.Steering.PathfollowingBehavior;
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
            groupes.add(new ArrayList<>(List.of(new Giant(new Vector2D(0, 0), "Giant"+i))));
        }

        // Nombre de manches (itération d'évolution)
        double distanceMin = 1000000;
        for (int i = 0; i < 25; i++) {
            HashMap<ArrayList<Ennemy>, Double> stats = new HashMap<>();
            for (ArrayList<Ennemy> groupe : groupes) {
                stats.put(groupe, 0.0);
            }

            EvolutionSteering evolution = new EvolutionSteering();
            evolution.nbchekpoints = 1;
            stats = evolution.evaluate(stats);

            if (stats == null) {
                System.out.println("Les ennemies ont gagné la partie");
                break;
            }

            groupes = evolution.evolve(stats);

            System.out.println("Après l'évolution");
            //parcours groupes
            for (ArrayList<Ennemy> groupe : groupes) {
                Ennemy ennemy = groupe.get(0);
                //Affiche les waypoints
                System.out.println("Waypoints de " + ennemy.getName() + "(" + stats.get(groupe) + ")");
                for (Vector2D waypoint : ((PathfollowingBehavior) ennemy.getListBehavior().get(0)).getCheckpoints()) {
                    if (ennemy.getDistanceTraveled()<distanceMin && ennemy.getDistanceTraveled() != 0) {
                        distanceMin = ennemy.getDistanceTraveled();
                    }
                }
            }
        }
        System.out.println("Distance minimale parcourue : " + distanceMin);
        System.out.println("Fin bitch");
    }
}
