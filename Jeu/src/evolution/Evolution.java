package evolution;

import entites.enemies.*;
import laby.ModeleLabyrinth;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.*;

public class Evolution {

    // On stocke les statistiques des ennemis au départ de la manche
    public static final Map<Ennemy, double[]> startStats = Collections.synchronizedMap(new HashMap<>());

    public HashMap<Ennemy, Double> evaluate(HashMap<Ennemy, Double> stats) throws IOException {
        // On boucle sur les agents de la map
        for (Ennemy ennemy : stats.keySet()) {
            // On crée un environnement pour l'agent
            ModeleLabyrinth jeu = new ModeleLabyrinth();
            ArrayList<Ennemy> ennemies = new ArrayList<>();
            ennemies.add(ennemy);
            jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", ennemies, 1000, 1200);
            stats.put(ennemy, simulate(jeu));
            jeu = null;
        }

        // Après évaluation, on réaffecte les statistiques de départ aux ennemis
        for (Ennemy ennemy : stats.keySet()) {
            double[] statsStart = startStats.get(ennemy);
            if (statsStart != null) {
                ennemy.setHealth(statsStart[0]);
                ennemy.setSpeed(statsStart[1]);
                ennemy.setDamages(statsStart[2]);
                ennemy.setAttackSpeed(statsStart[3]);
            } else {
                System.err.println("Aucune stats sauvegardée pour " + ennemy.getName());
            }
        }

        // On retourne la map des ennemy|score
        return stats;
    }

    /**
     * Simule une manche
     * @param jeu le jeu à simuler
     * @return le score de l'agent après la simulation
     */
    public double simulate(ModeleLabyrinth jeu){
        // On sauvegarde les statistiques de départ des ennemis
        saveStartStats(jeu.enemies);
        long lastUpdateTime = System.nanoTime();
        // Tant que la manche est en cours
        while (!jeu.getPause()) {
            long currentTime = System.nanoTime();
            double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

            jeu.update(elapsedTimeInSeconds);
            lastUpdateTime = currentTime;
        }
        // On retourne le score
        return getScore(jeu.getEnnemyEndOfManche().get(0));
    }

    public double getScore(Ennemy e) {
        //Ajoute 20 si l'ennemi est en vie et enleve 20 si l'ennemi est mort
        int bonus = e.getIsDead() ? -1000 : 1000;
        //System.out.println("Survival time : "+e.getSurvivalTime() /1000000000);
        //System.out.println("Distance to arrival: "+e.getDistanceToArrival());
        //System.out.println("Bonus : "+bonus);

        double score = ((double) e.getSurvivalTime() /1000000) + bonus - e.getDistanceToArrival()*10;
        return score;
    }

    public ArrayList<Ennemy> evolve(HashMap<Ennemy, Double> giants) {
        //Trier les géants par score décroissant
        ArrayList<Map.Entry<Ennemy, Double>> ennemiesTries = new ArrayList<>(giants.entrySet());
        ennemiesTries.sort((g1, g2) -> Double.compare(g2.getValue(), g1.getValue()));

        System.out.println("Ennemies triés : "+ennemiesTries);
        //Sélectionner la moitié des meilleurs géants
        int size = ennemiesTries.size();
        //int moitié = (int)Math.ceil(ennemiesTries.size() / 10);
        int moitie = size / 2;
        System.out.println("Moitié : "+moitie);
        ArrayList<Ennemy> meilleurs = new ArrayList<>();
        for (int i = 0; i < moitie; i++) {
            meilleurs.add(ennemiesTries.get(i).getKey());
        }
        System.out.println("Meilleurs ennemies : "+meilleurs);

        //Générer les enfants via le croisement
        ArrayList<Ennemy> enfants = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size-moitie; i++) {
            // Sélectionner deux parents aléatoires parmi les meilleurs
            Ennemy parent1 = meilleurs.get(random.nextInt(meilleurs.size()));
            Ennemy parent2 = meilleurs.get(random.nextInt(meilleurs.size()));

            // Créer un enfant en croisant les parents
            Ennemy enfant = croiser(parent1, parent2);
            enfants.add(enfant);
        }

        //Combiner les meilleurs géants et les enfants
        ArrayList<Ennemy> nouvellePopulation = new ArrayList<>();
        nouvellePopulation.addAll(meilleurs);
        nouvellePopulation.addAll(enfants);

        //Appliquer une mutation sur la nouvelle population
        return mutate(nouvellePopulation);
    }

    /**
     * Méthode pour appliquer une mutation sur une population de géants
     * @param nouvellePopulation
     * @return
     */
    private ArrayList<Ennemy> mutate(ArrayList<Ennemy> nouvellePopulation) {

        // On boucle sur les géants de la population
        for (Ennemy giant : nouvellePopulation) {
            // On applique une mutation sur chaque géant
            giant.setHealth(mutateValue(giant.getHealth()));
            giant.setSpeed(mutateValue(giant.getSpeed()));
            giant.setDamages(mutateValue(giant.getDamages()));
            giant.setAttackSpeed(mutateValue(giant.getAttackSpeed()));
        }

        return nouvellePopulation;
    }

    /**
     * Méthode pour muter une valeur (ajouter un peu de bruit, 5% ici)
     * @param value la valeur à muter
     * @return la valeur mutée
     */
    private double mutateValue(double value) {
        return value * (1 + (Math.random() * 0.10 - 0.05));
    }

    /**
     * Méthode pour croiser deux géants et générer un enfant.
     * À personnaliser en fonction des propriétés des géants.
     */
    private Ennemy croiser(Ennemy parent1, Ennemy parent2) {
        // Exemple de croisement (à adapter selon la structure de Giant)
        Ennemy enfant = null;
        if (parent1 instanceof Giant) {
            enfant = new Giant(new Vector2D(0, 0), "GiantEnfant");
        } else if (parent1 instanceof Ninja) {
            enfant = new Ninja(new Vector2D(0, 0), "NinjaEnfant");
        } else if (parent1 instanceof Druide) {
            enfant = new Druide(new Vector2D(0, 0), "DruideEnfant");
        } else if (parent1 instanceof Berserker) {
            enfant = new Berserker(new Vector2D(0, 0), "BerserkerEnfant");
        }

        //enfant.setSprite(null);

        enfant.setHealth(randomChoice(parent1.getHealth(), parent2.getHealth()));
        enfant.setSpeed(randomChoice(parent1.getSpeed(), parent2.getSpeed()));
        enfant.setDamages(randomChoice(parent1.getDamages(), parent2.getDamages()));
        enfant.setAttackSpeed(randomChoice(parent1.getAttackSpeed(), parent2.getAttackSpeed()));
        // Ajouter ici les propriétés spécifiques à croiser
        return enfant;
    }

    /**
     * Méthode pour choisir aléatoirement une propriété entre deux options.
     */
    private <T> T randomChoice(T option1, T option2) {
        return new Random().nextBoolean() ? option1 : option2;
    }

    /**
     * Sauvegarde les statistiques de départ des ennemis
     * @param ennemies Liste des ennemis
     */
    public static void saveStartStats(List<Ennemy> ennemies) {
        synchronized (startStats) {
            for (Ennemy e : ennemies) {
                double[] stats = {e.getHealth(), e.getSpeed(), e.getDamages(), e.getAttackSpeed(), e.getRange()};
                startStats.put(e, stats);
            }
        }
    }
}
