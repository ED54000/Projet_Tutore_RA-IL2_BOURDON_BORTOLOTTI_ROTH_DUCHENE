package evolution;

import entites.enemies.Ennemy;

import java.util.ArrayList;

public class EnnemyEvolutionv2 {

    public double getScore(Ennemy e){
        double score = e.getDistanceToArrival() + 0.2 * (double) e.getSurvivalTime();
        return score;
    }
}
