package evolution;

import entites.enemies.Ennemy;
import javafx.application.Platform;
import laby.ModeleLabyrinth;
import mains.MainSimu;
import mains.MainSimuSteering;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.*;

public class EvolutionSteering implements Evolve {

    public static final Map<Ennemy, double[]> startStats = Collections.synchronizedMap(new HashMap<>());


    @Override
    public HashMap<ArrayList<Ennemy>, Double> evaluate(HashMap<ArrayList<Ennemy>, Double> stats) throws IOException {
        // Créer une nouvelle map pour stocker les résultats
        HashMap<ArrayList<Ennemy>, Double> newStats = new HashMap<>();

        ArrayList<Ennemy> groupe = stats.keySet().iterator().next();
        Ennemy ennemy = groupe.get(0);
        ModeleLabyrinth jeu = new ModeleLabyrinth();
        jeu.nbEnnemiesToWin = 2;
        //refreshEnnemies(ennemy, jeu);
        ArrayList<Ennemy> copieGroupe = new ArrayList<>(List.of(ennemy));
        jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", copieGroupe, 1000, jeu.nbEnnemiesToWin);

        //Créer au hasard 3 checkpoints
        ArrayList<Vector2D> checkpoints = new ArrayList<>();
        for (int j = 0; j < 3; j++) {
            //ajoute un checkpoint dans la limite du labyrinthe
            int hauteur = jeu.getCases().length;
            int largeur = jeu.getCases()[0].length;
            checkpoints.add(new Vector2D((int) (Math.random() * largeur), (int) (Math.random() * hauteur)));
        }
        //ajoute l'arrivé
        checkpoints.add(jeu.getArrival());
        System.out.println("Le départ est : " + ennemy.getPositionReel()+" / "+jeu.getStart());
        System.out.println("Les checkpoints sont : ");
        for (Vector2D v : checkpoints) {
            System.out.println(v);
        }

        //créer PathFpllowingBehavior
        PathfollowingBehavior pathfollowingBehavior = new PathfollowingBehavior(checkpoints);
        ennemy.setBehavior(pathfollowingBehavior);

        double score = simulate(jeu);
        refreshEnnemies(ennemy, jeu);
        newStats.put(groupe, score);
        stats.putAll(newStats);
        return stats;
    }

    @Override
    public double simulate(ModeleLabyrinth jeu) {
        System.out.println("Simulation");

        //Platform.runLater(() -> MainSimuSteering.launchUI(jeu, jeu.enemies));

        saveStartStats(jeu.enemies);

        long lastUpdateTime = System.nanoTime();
        while (!jeu.getPause() && !jeu.getPauseManche()) {
            long currentTime = System.nanoTime();
            double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

            jeu.update(elapsedTimeInSeconds);
            lastUpdateTime = currentTime;
        }

        System.out.println("Fin de la simulation");
        return getScore(jeu.getEnnemyEndOfManche());
    }




    @Override
    public double getScore(ArrayList<Ennemy> ennemies) {
        //Ajoute 20 si l'ennemi est en vie et enleve 20 si l'ennemi est mort
        Ennemy e = ennemies.get(0);

        //int bonus = e.getIsDead() ? -1000 : 1000;
        System.out.println("Survival time : "+(double)e.getSurvivalTime());
        System.out.println("Distance to arrival: "+e.getDistanceToArrival());
        //TODO : Rajouter le nombre de hit que l'ennemi a pris
        //System.out.println("Bonus : "+bonus);

        return - ((double) e.getSurvivalTime()) - e.getDistanceToArrival()*10;
    }

    @Override
    public ArrayList<ArrayList<Ennemy>> evolve(HashMap<ArrayList<Ennemy>, Double> ennemies) {
        return null;
    }

    private static void refreshEnnemies(Ennemy ennemy, ModeleLabyrinth jeu) {
        // Réinitialisation des attributs de base
        ennemy.setLastAttackCount(0);
        //ennemy.setSurvivalTime(0);
        ennemy.setKillerType(null);
        ennemy.setIsDead(false);
        ennemy.setToStart(jeu);
        ennemy.setArrived(false);

        // Réinitialisation des stats
        double[] statsStart = EvolutionSteering.startStats.get(ennemy);
        if (statsStart != null) {
            ennemy.setHealth(1000000);
            ennemy.setSpeed(statsStart[1]);
            ennemy.setDamages(statsStart[2]);
            ennemy.setAttackSpeed(statsStart[3]);
        } else {
            System.err.println("Aucune stats sauvegardée pour " + ennemy.getName());
        }
    }

    public static void saveStartStats(List<Ennemy> ennemies) {
        synchronized (startStats) {
            for (Ennemy e : ennemies) {
                double[] stats = {e.getHealth(), e.getSpeed(), e.getDamages(), e.getAttackSpeed(), e.getRange()};
                startStats.put(e, stats);
            }
        }
    }
}
