package evolution;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
import javafx.application.Platform;
import laby.ModeleLabyrinth;
import laby.views.ViewLabyrinth;
import mains.MainSimu;
import mains.MainSimuSteering;
import steering_astar.Steering.PathfollowingBehavior;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.*;

public class EvolutionSteering implements Evolve {

    public static final Map<Ennemy, double[]> startStats = Collections.synchronizedMap(new HashMap<>());
    public int nbchekpoints = 3;


    @Override
    public HashMap<ArrayList<Ennemy>, Double> evaluate(HashMap<ArrayList<Ennemy>, Double> stats) throws IOException {
        // Créer une nouvelle map pour stocker les résultats
        HashMap<ArrayList<Ennemy>, Double> newStats = new HashMap<>();
        //for (int i = 1; stats.keySet().size() < i; i++) {
        //    ArrayList<Ennemy> groupe = new ArrayList<>(stats.keySet()).get(i);
        //    Ennemy ennemy = groupe.get(0);

        for (ArrayList<Ennemy> groupe : stats.keySet()){
            Ennemy ennemy = groupe.get(0);
            ModeleLabyrinth jeu = new ModeleLabyrinth();
            jeu.nbEnnemiesToWin = 2;
            //refreshEnnemies(ennemy, jeu);
            ArrayList<Ennemy> copieGroupe = new ArrayList<>(List.of(ennemy));
            jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", copieGroupe, 1000, jeu.nbEnnemiesToWin);

            //Créer au hasard x checkpoints
            ArrayList<Vector2D> checkpoints = new ArrayList<>();
            for (int j = 0; j < nbchekpoints; j++) {
                //ajoute un checkpoint dans la limite du labyrinthe
                int hauteur = jeu.getCases().length * jeu.getTailleCase();
                int largeur = jeu.getCases()[0].length * jeu.getTailleCase();

                int x = (int) (Math.random() * largeur);
                int y = (int) (Math.random() * hauteur);

                //Vérifie que le checkpoint est sur une case vide
                while (jeu.getCases()[y / jeu.getTailleCase()][x / jeu.getTailleCase()] != '.') {
                    x = (int) (Math.random() * largeur);
                    y = (int) (Math.random() * hauteur);
                }
                checkpoints.add(new Vector2D(x, y));
            }
            //ajoute l'arrivée
            checkpoints.add(jeu.getArrival());
            //créer PathFpllowingBehavior
            PathfollowingBehavior pathfollowingBehavior = new PathfollowingBehavior(checkpoints);
            ennemy.setBehavior(pathfollowingBehavior);

            double score = simulate(jeu);
            refreshEnnemies(ennemy, jeu);
            newStats.put(groupe, score);
        }
        stats.putAll(newStats);
        return stats;
    }

    @Override
    public double simulate(ModeleLabyrinth jeu) {
        System.out.println("Simulation");

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
        double total = 0;
        Ennemy e = ennemies.get(0);

        System.out.println("Survival time : "+(double)e.getSurvivalTime());
        System.out.println("Distance to arrival: "+e.getDistanceToArrival());
        System.out.println("Distance parcouru : "+e.getDistanceTraveled());
        //TODO : Rajouter le nombre de hit que l'ennemi a pris, prednre la distance ToArrival quand il meurt
        //System.out.println("Bonus : "+bonus);
        //return - ((double) e.getSurvivalTime()) - e.getDistanceToArrival()*10;
        int bonus = e.getIsDead() ? -100000 : 100000;
        if (bonus == -100000) {
            total = bonus - e.getDistanceToArrival();
        }
        else {
            total = bonus + e.getDistanceTraveled();
        }
        return total;
    }

    @Override
    public ArrayList<ArrayList<Ennemy>> evolve(HashMap<ArrayList<Ennemy>, Double> ennemies) {
        //on trie les ennemis par score
        ArrayList<Map.Entry<ArrayList<Ennemy>, Double>> groupeTries = new ArrayList<>(ennemies.entrySet());
        groupeTries.sort((g1, g2) -> Double.compare(g2.getValue(), g1.getValue()));

        //On prend les 50% meilleurs ennemis
        int size = groupeTries.size();
        int moitie = size / 2;

        ArrayList<ArrayList<Ennemy>> meilleurs = new ArrayList<>();
        for (int i = 0; i < moitie; i++) {
            //si le groupe est vide on ne fait rien
            if (groupeTries.get(i).getKey().isEmpty()) {
                continue;
            }
            meilleurs.add(groupeTries.get(i).getKey());
        }
        //Générer les enfants via le croisement
        ArrayList<ArrayList<Ennemy>> enfants = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size-moitie; i++) {
            // Sélectionner deux parents aléatoires parmi les meilleurs
            ArrayList<Ennemy> parent1 = meilleurs.get(random.nextInt(meilleurs.size()));
            ArrayList<Ennemy> parent2 = meilleurs.get(random.nextInt(meilleurs.size()));
            // Créer un enfant en croisant les parents
            ArrayList<Ennemy> enfant = croiserGroupes(parent1, parent2);
            enfants.add(enfant);
        }

        //Combiner les meilleurs et les enfants
        ArrayList<ArrayList<Ennemy>> nouvellePopulation = new ArrayList<>();
        nouvellePopulation.addAll(meilleurs);
        nouvellePopulation.addAll(enfants);

        System.out.println("Nouvelle population : "+nouvellePopulation);
        //Appliquer une mutation sur la nouvelle population
        return mutate(nouvellePopulation);
    }

    ArrayList<Ennemy> croiserGroupes(ArrayList<Ennemy> g1, ArrayList<Ennemy> g2) {
        //On récupère le premier ennemi de chaque groupe
        Ennemy e1 = g1.get(0);
        Ennemy e2 = g2.get(0);
        System.out.println("Liste des behavoirs de e1 : "+e1.getListBehavior());
        System.out.println("Liste des behavoirs de e2 : "+e2.getListBehavior());
        //On récupère la listes de waypoints de chaque ennemi
        ArrayList<Vector2D> waypoints1 = ((PathfollowingBehavior) e1.getListBehavior().get(0)).getCheckpoints();
        ArrayList<Vector2D> waypoints2 = ((PathfollowingBehavior) e2.getListBehavior().get(0)).getCheckpoints();
        //On créer une nouvelle liste de waypoints pour l'enfant
        ArrayList<Vector2D> waypointsEnfant = new ArrayList<>();
        for (int i = 0; i < nbchekpoints; i++) {
            //On récupère les waypoints de chaque ennemi
            Vector2D waypoint1 = waypoints1.get(i);
            Vector2D waypoint2 = waypoints2.get(i);
            //On ajoute le milieu des deux waypoints
            waypointsEnfant.add(new Vector2D((waypoint1.getX() + waypoint2.getX()) / 2, (waypoint1.getY() + waypoint2.getY()) / 2));
        }

        //On ajoute l'arrivé
        waypointsEnfant.add(ModeleLabyrinth.getArrival());
        //On créer un nouveau PathfollowingBehavior pour l'enfant
        PathfollowingBehavior pathfollowingBehavior = new PathfollowingBehavior(waypointsEnfant);
        //On créer un nouvel ennemi pour l'enfant
        Giant enfant = new Giant(new Vector2D(0, 0), "GiantEnfant");
        //On ajoute le PathfollowingBehavior à l'enfant
        enfant.setBehavior(pathfollowingBehavior);

        ArrayList<Ennemy> listeEnfant = new ArrayList<>();
        listeEnfant.add(enfant);
        //On retourne l'enfant
        return listeEnfant;
    }

    /**
     * Méthode pour choisir aléatoirement une propriété entre deux options.
     */
    private <T> T randomChoice(T option1, T option2) {
        return new Random().nextBoolean() ? option1 : option2;
    }

    /**
     * Méthode pour appliquer une mutation
     * @param nouvellePopulation
     * @return
     */
    private ArrayList<ArrayList<Ennemy>> mutate(ArrayList<ArrayList<Ennemy>> nouvellePopulation) {
        // On boucle sur la population
        for (ArrayList<Ennemy> groupe : nouvellePopulation) {
            Ennemy ennemy = groupe.get(0);
            // On récupère les waypoints de l'ennemi
            List<Vector2D> waypoints = ((PathfollowingBehavior) ennemy.getListBehavior().get(0)).getCheckpoints();
            for (Vector2D waypoint : waypoints) {
                // On applique une mutation sur chaque coordonnée TODO : Tester avec des valeurs plus grandes
                waypoint.setX(waypoint.getX() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
                waypoint.setY(waypoint.getY() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            }
        }
        return nouvellePopulation;
    }

    public static void refreshEnnemies(Ennemy ennemy, ModeleLabyrinth jeu) {
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
            ennemy.setHealth(statsStart[0]);
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
