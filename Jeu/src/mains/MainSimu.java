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
        for (int i = 0; i < 5; i++) {
            Giant giant = new Giant(new Vector2D(0, 0), "Giant "+i);
            giant.setSprite(null);
            ennemies.add(giant);
        }

        //Boucle sur le nombre de manche avec une population d'ennemies évoluer à chaque fois
        for (int i = 0; i < 10; i++) {
            System.out.println("Manche "+i);
            for (Ennemy ennemy : ennemies) {
                System.out.println("Ennemy avant évolution: "+ ennemy.getName());
                System.out.println("Vie : "+ennemy.getHealth()+" Dégats : "+ennemy.getDamages()+" Vitesse : "+ennemy.getSpeed());
            }

            //création d'une HashMap avec pour clé l'ennemi et pour valeur son score
            HashMap<Ennemy, Double> stats = new HashMap<>();
            for (Ennemy ennemy : ennemies) {
                stats.put(ennemy, 0.0);
            }

            //on évolue
            Evolution evolution = new Evolution();
            ArrayList<Ennemy> newPopulation = evolution.evolve(evolution.evaluate(stats));
            ennemies = newPopulation;
            System.out.println("Nouvelle population : ");
            for (Ennemy ennemy : ennemies) {
                System.out.println("Ennemy après evolution"+ennemy.getName());
                System.out.println("Vie : "+ennemy.getHealth()+" Dégats : "+ennemy.getDamages()+" Vitesse : "+ennemy.getSpeed());
            }
        }
        System.out.println("Fin de la simulation");
    }
}
