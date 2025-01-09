package evolution;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnnemyEvolutionv2 {

    public double getScore(Ennemy e){
        //Ajoute 20 si l'ennemi est en vie et enleve 20 si l'ennemi est mort
        int bonus = e.isDead() ? -20 : 20;
        System.out.println("Distance to Arrival : "+e.getDistanceToArrival());
        System.out.println("Bonus : "+bonus);

        double score = e.getDistanceToArrival() + bonus;
        return score;
    }

    public ArrayList<Giant> evolve(HashMap<Giant, Double> giants) {
        // 1. Trier les géants par score décroissant
        ArrayList<Map.Entry<Giant, Double>> giantsTries = new ArrayList<>(giants.entrySet());
        giantsTries.sort((g1, g2) -> Double.compare(g2.getValue(), g1.getValue()));

        // 2. Sélectionner la moitié des meilleurs géants
        int moitié = giantsTries.size() / 2;
        ArrayList<Giant> meilleurs = new ArrayList<>();
        for (int i = 0; i < moitié; i++) {
            meilleurs.add(giantsTries.get(i).getKey());
        }

        // 3. Générer les enfants via le croisement
        ArrayList<Giant> enfants = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < moitié; i++) {
            // Sélectionner deux parents aléatoires parmi les meilleurs
            Giant parent1 = meilleurs.get(random.nextInt(meilleurs.size()));
            Giant parent2 = meilleurs.get(random.nextInt(meilleurs.size()));

            // Créer un enfant en croisant les parents
            Giant enfant = croiser(parent1, parent2);
            enfants.add(enfant);
        }

        // 4. Combiner les meilleurs géants et les enfants
        ArrayList<Giant> nouvellePopulation = new ArrayList<>();
        nouvellePopulation.addAll(meilleurs);
        nouvellePopulation.addAll(enfants);

        return nouvellePopulation;
    }

    /**
     * Méthode pour croiser deux géants et générer un enfant.
     * À personnaliser en fonction des propriétés des géants.
     */
    private Giant croiser(Giant parent1, Giant parent2) {
        // Exemple de croisement (à adapter selon la structure de Giant)
        Giant giant = new Giant(new Vector2D(0, 0), "Giant");

        giant.setHealth(randomChoice(parent1.getHealth(), parent2.getHealth()));
        giant.setSpeed(randomChoice(parent1.getSpeed(), parent2.getSpeed()));
        giant.setDamages(randomChoice(parent1.getDamages(), parent2.getDamages()));
        giant.setAttackSpeed(randomChoice(parent1.getAttackSpeed(), parent2.getAttackSpeed()));
        // Ajouter ici les propriétés spécifiques à croiser
        return giant;
    }

    /**
     * Méthode pour choisir aléatoirement une propriété entre deux options.
     */
    private <T> T randomChoice(T option1, T option2) {
        return new Random().nextBoolean() ? option1 : option2;
    }
}
