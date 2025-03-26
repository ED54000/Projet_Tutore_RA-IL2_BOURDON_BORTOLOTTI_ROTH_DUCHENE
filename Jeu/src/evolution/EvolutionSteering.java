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


    @Override
    public HashMap<ArrayList<Ennemy>, Double> evaluate(HashMap<ArrayList<Ennemy>, Double> stats) throws IOException {
        // Créer une nouvelle map pour stocker les résultats
        HashMap<ArrayList<Ennemy>, Double> newStats = new HashMap<>();

        for (ArrayList<Ennemy> groupe : stats.keySet()){
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
                int hauteur = jeu.getCases().length * jeu.getTailleCase();
                int largeur = jeu.getCases()[0].length * jeu.getTailleCase();
                checkpoints.add(new Vector2D((int) (Math.random() * largeur), (int) (Math.random() * hauteur)));
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

        // On affiche les informations sur les ennemis
        Ennemy e = jeu.enemies.get(0);
        System.out.println("Ennemi : " + e.getName());
        System.out.println("Vitesse : " + e.getSpeed());
        System.out.println("Dégâts : " + e.getDamages());
        System.out.println("Vie : " + e.getHealth());
        System.out.println("Vitesse d'attaque : " + e.getAttackSpeed());
        System.out.println("Portée : " + e.getRange());
        System.out.println("Waypoints : " + ((PathfollowingBehavior) e.getListBehavior().get(0)).getCheckpoints());
        System.out.println("Mort : " + e.getIsDead());
        System.out.println("Arrivé : " + e.getIsArrived());
        System.out.println("Survie : " + e.getSurvivalTime());
        System.out.println("Distance à l'arrivée : " + e.getDistanceToArrival());
        System.out.println("Type de tueur : " + e.getKillerType());
        System.out.println("Nombre de coups reçus : " + e.getLastAttackCount());


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
        //On récupère la listes de waypoints de chaque ennemi
        ArrayList<Vector2D> waypoints1 = ((PathfollowingBehavior) e1.getListBehavior().get(0)).getCheckpoints();
        ArrayList<Vector2D> waypoints2 = ((PathfollowingBehavior) e2.getListBehavior().get(0)).getCheckpoints();
        //On créer une nouvelle liste de waypoints pour l'enfant
        ArrayList<Vector2D> waypointsEnfant = new ArrayList<>();
        //On ajoute le milieu des deux premiers waypoints
        waypointsEnfant.add(new Vector2D((waypoints1.get(0).getX() + waypoints2.get(0).getX()) / 2, (waypoints1.get(0).getY() + waypoints2.get(0).getY()) / 2));
        //On ajoute le milieu des deuxièmes waypoints
        waypointsEnfant.add(new Vector2D((waypoints1.get(1).getX() + waypoints2.get(1).getX()) / 2, (waypoints1.get(1).getY() + waypoints2.get(1).getY()) / 2));
        //On ajoute le milieu des troisièmes waypoints
        waypointsEnfant.add(new Vector2D((waypoints1.get(2).getX() + waypoints2.get(2).getX()) / 2, (waypoints1.get(2).getY() + waypoints2.get(2).getY()) / 2));
        //On ajoute l'arrivé
        waypointsEnfant.add(new Vector2D(ModeleLabyrinth.getYArrival(), ModeleLabyrinth.getXArrival()));
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
            // On récupère les coordonnées de chaque waypoint
            Vector2D waypoint1 = waypoints.get(0);
            Vector2D waypoint2 = waypoints.get(1);
            Vector2D waypoint3 = waypoints.get(2);
            // On applique une mutation sur chaque coordonnée TODO : Tester avec des valeurs plus grandes
            waypoint1.setX(waypoint1.getX() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            waypoint1.setY(waypoint1.getY() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            waypoint2.setX(waypoint2.getX() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            waypoint2.setY(waypoint2.getY() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            waypoint3.setX(waypoint3.getX() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
            waypoint3.setY(waypoint3.getY() + (new Random().nextBoolean() ? 1 : -1) * new Random().nextInt(15));
        }

        return nouvellePopulation;
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
