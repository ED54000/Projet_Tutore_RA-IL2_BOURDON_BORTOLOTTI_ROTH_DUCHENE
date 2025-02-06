package evolution;

import entites.enemies.Ennemy;
import entites.enemies.Giant;
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
            // Une fois le score calculé, on remet les statistiques de départ
            ennemy.setHealth(startStats.get(ennemy)[0]);
            ennemy.setSpeed(startStats.get(ennemy)[1]);
            ennemy.setDamages(startStats.get(ennemy)[2]);
            ennemy.setAttackSpeed(startStats.get(ennemy)[3]);

            System.out.println("Ennemis après simu :" + ennemy.getHealth() + " " + ennemy.getSpeed() + " " + ennemy.getDamages() + " " + ennemy.getAttackSpeed());
        }

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

        double score = 0;
        long lastUpdateTime = System.nanoTime();
        // Tant que la manche est en cours
        while (!jeu.isPause()) {
            long currentTime = System.nanoTime();
            double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

            jeu.update(elapsedTimeInSeconds);
            lastUpdateTime = currentTime;
        }
        // On retourne le score
        return getScore(jeu.getEnnemyEndOfManche().get(0));
    }

    public double getScore(Ennemy e){
        //Ajoute 20 si l'ennemi est en vie et enleve 20 si l'ennemi est mort
        int bonus = e.isDead() ? -20 : 20;
        System.out.println("Survival time : "+e.getSurvivalTime());
        System.out.println("Bonus : "+bonus);

        double score = ((double) e.getSurvivalTime() /1000000) + bonus;
        return score;
    }

    public ArrayList<Ennemy> evolve(HashMap<Ennemy, Double> giants) {
        // 1. Trier les géants par score décroissant
        ArrayList<Map.Entry<Ennemy, Double>> giantsTries = new ArrayList<>(giants.entrySet());
        giantsTries.sort((g1, g2) -> Double.compare(g2.getValue(), g1.getValue()));

        // 2. Sélectionner la moitié des meilleurs géants
        int moitié = giantsTries.size() / 2;
        ArrayList<Ennemy> meilleurs = new ArrayList<>();
        for (int i = 0; i < moitié; i++) {
            meilleurs.add(giantsTries.get(i).getKey());
        }

        // 3. Générer les enfants via le croisement
        ArrayList<Ennemy> enfants = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < moitié; i++) {
            // Sélectionner deux parents aléatoires parmi les meilleurs
            Ennemy parent1 = meilleurs.get(random.nextInt(meilleurs.size()));
            Ennemy parent2 = meilleurs.get(random.nextInt(meilleurs.size()));

            // Créer un enfant en croisant les parents
            Ennemy enfant = croiser(parent1, parent2);
            enfants.add(enfant);
        }

        // 4. Combiner les meilleurs géants et les enfants
        ArrayList<Ennemy> nouvellePopulation = new ArrayList<>();
        nouvellePopulation.addAll(meilleurs);
        nouvellePopulation.addAll(enfants);

        // 5. Appliquer une mutation sur la nouvelle population
        nouvellePopulation = mutate(nouvellePopulation);

        return nouvellePopulation;
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
