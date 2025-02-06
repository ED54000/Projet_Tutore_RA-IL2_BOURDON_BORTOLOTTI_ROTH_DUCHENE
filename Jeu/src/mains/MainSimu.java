package mains;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import evolution.Evolution;
import javafx.application.Application;
import javafx.stage.Stage;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainSimu extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //initialisation avec une liste d'ennemies
        ArrayList<Ennemy> ennemies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ennemies.add(new Giant(new Vector2D(0, 0), "Giant "+i));
        }

        //Boucle sur le nombre de manche avec une population de 20 ennemies évoluer à chaque fois
        for (int i = 0; i < 20; i++) {
            System.out.println("Manche "+i);
            //création d'une HashMap avec pour cléf l'ennemi et pour valeur son score
            HashMap<Ennemy, Double> stats = new HashMap<>();
            for (Ennemy ennemy : ennemies) {
                System.out.println("Ennemy : "+ennemy.getName());
                System.out.println("Stats : ");
                System.out.println("Vie :"+ennemy.getHealth());
                System.out.println("SurvivalTime :"+ennemy.getSurvivalTime());
                System.out.println("Damage :"+ennemy.getDamages());
                System.out.println("Speed : "+ennemy.getSpeed());
                stats.put(ennemy, 0.0);
            }
            //on évalue
            Evolution evolution = new Evolution();
            ArrayList<Ennemy> newPopulation = evolution.evolve(evolution.evaluate(stats));
            System.out.println("New population : " + newPopulation);
        }
    }
}
