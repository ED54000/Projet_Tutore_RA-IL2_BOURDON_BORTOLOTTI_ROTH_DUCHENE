package evolution;

import entites.enemies.*;
import laby.ModeleLabyrinth;
import steering_astar.Steering.Vector2D;

import java.io.IOException;
import java.util.*;

public class Evolution {

    // On stocke les statistiques des ennemis au départ de la manche
    public static final Map<Ennemy, double[]> startStats = Collections.synchronizedMap(new HashMap<>());

    public HashMap<ArrayList<Ennemy>, Double> evaluate(HashMap<ArrayList<Ennemy>, Double> stats) throws IOException {
        // Créer une nouvelle map pour stocker les résultats
        HashMap<ArrayList<Ennemy>, Double> newStats = new HashMap<>();
        // On boucle sur la map, mais on ne modifie pas la map pendant l'itération
        for (ArrayList<Ennemy> groupe : stats.keySet()) {
            //si le groupe est vide on ne fait rien
            if (groupe.isEmpty()) {
                continue;
            }


            ModeleLabyrinth jeu = new ModeleLabyrinth();
            jeu.nbEnnemiesToWin = groupe.size(); //Fixe le nombre d'ennemis qui doivent passer pour gagner au npmbre de base -1
            //crée une copie de groupe
            ArrayList<Ennemy> copieGroupe = new ArrayList<>();
            System.out.println("Groupe avant : "+groupe);
            for (Ennemy ennemy : groupe) {
                ennemy.setSurvivalTime(0);
                refreshEnnemiesAndAdd(ennemy, jeu, copieGroupe);
            }
            System.out.println("Groupe tout neuf : "+copieGroupe);

            jeu.creerLabyrinthe("Ressources/Labyrinthe3.txt", copieGroupe, 1000, jeu.nbEnnemiesToWin);
            double score = simulate(jeu);
            if (jeu.etreFini()){
                System.out.println("Jeu fini les enneies ont gagné");
                //stop l'évolution
                return null;
            }
            jeu = null;
            // Après évaluation, réaffecter les statistiques de départ
            for (Ennemy ennemies : groupe) {
                //TODO ennemies a mettre en ennemy
                ennemies.setArrived(false);
                ennemies.setIsDead(false);

                double[] statsStart = startStats.get(ennemies);
                if (statsStart != null) {
                    ennemies.setHealth(statsStart[0]);
                    ennemies.setSpeed(statsStart[1]);
                    ennemies.setDamages(statsStart[2]);
                    ennemies.setAttackSpeed(statsStart[3]);
                } else {
                    System.err.println("Aucune stats sauvegardée pour " + ennemies.getName());
                }
            }
            //On stocke le score pour ce groupe
            newStats.put(groupe, score);
        }
        // Remplacer les anciens scores avec les nouveaux
        stats.putAll(newStats);

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
        while (!jeu.getPause() && !jeu.getPauseManche()) {
            long currentTime = System.nanoTime();
            double elapsedTimeInSeconds = (currentTime - lastUpdateTime) / 1_000_000_000.0;

            jeu.update(elapsedTimeInSeconds);
            lastUpdateTime = currentTime;
        }
        // On retourne le score
        return getScore(jeu.getEnnemyEndOfManche());
    }

    public double getScore(ArrayList<Ennemy> e) {
        //calcul du score pour un groupe d'ennemis
        double score = 0;
        for (Ennemy ennemy : e) {
            score += getScoreOneEnnemy(ennemy);
        }

        //TODO : Utiliser le temp que le grp a mit pour arriver

        return score;
    }

    public double getScoreOneEnnemy(Ennemy e) {
        //Ajoute 20 si l'ennemi est en vie et enleve 20 si l'ennemi est mort
        int bonus = e.getIsDead() ? -1000 : 1000;
        System.out.println("Survival time : "+(double)e.getSurvivalTime());
        System.out.println("Distance to arrival: "+e.getDistanceToArrival());
        System.out.println("Bonus : "+bonus);

        double score = bonus - ((double) e.getSurvivalTime()) - e.getDistanceToArrival()*10;

        return score;
    }

    public ArrayList<ArrayList<Ennemy>> evolve(HashMap<ArrayList<Ennemy>, Double> ennemies) {

        //System.out.println("Groupe initial : "+ennemies);
        //Trier les ennemies par score décroissant
        ArrayList<Map.Entry<ArrayList<Ennemy>, Double>> groupeTries = new ArrayList<>(ennemies.entrySet());
        groupeTries.sort((g1, g2) -> Double.compare(g2.getValue(), g1.getValue()));

        //System.out.println("Groupe trié : "+groupeTries);
        //System.out.println("Meilleur groupe : "+groupeTries.get(0).getKey());

        //System.out.println("Groupe trié : "+groupeTries);
        //Sélectionner la moitié des meilleurs groupes
        int size = groupeTries.size();
        //int moitié = (int)Math.ceil(groupeTries.size() / 10);
        int moitie = size / 2;
        //System.out.println("Moitié : "+moitie);
        ArrayList<ArrayList<Ennemy>> meilleurs = new ArrayList<>();
        for (int i = 0; i < moitie; i++) {
            //si le groupe est vide on ne fait rien
            if (groupeTries.get(i).getKey().isEmpty()) {
                continue;
            }
            meilleurs.add(groupeTries.get(i).getKey());
        }
        //System.out.println("Meilleurs groupes : "+meilleurs);

        //Générer les enfants via le croisement
        ArrayList<ArrayList<Ennemy>> enfants = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size-moitie; i++) {
            // Sélectionner deux parents aléatoires parmi les meilleurs
            ArrayList<Ennemy> parent1 = meilleurs.get(random.nextInt(meilleurs.size()));
            ArrayList<Ennemy> parent2 = meilleurs.get(random.nextInt(meilleurs.size()));

            //System.out.println("Liste parent 1 : "+parent1);
            //System.out.println("Liste parent 2 : "+parent2);

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

    /**
     * Méthode pour appliquer une mutation sur une population de géants
     * @param nouvellePopulation
     * @return
     */
    private ArrayList<ArrayList<Ennemy>> mutate(ArrayList<ArrayList<Ennemy>> nouvellePopulation) {
        // On boucle sur la population
        for (ArrayList<Ennemy> groupe : nouvellePopulation) {
            // On applique une mutation sur chaque ennemy
            for (Ennemy ennemy : groupe) {
                //TODO faire les vérif aussi pour vie et damage
                //on set la vie si < 500
                double newHealth = mutateValue(ennemy.getHealth());
                ennemy.setHealth(newHealth > 500 ? 500 : newHealth);
                //on set la speed si < 4
                double newSpeed = mutateValue(ennemy.getSpeed());
                ennemy.setSpeed(newSpeed > 4 ? 4 : newSpeed);
                //on set les dégâts si < 50
                double newDamages = mutateValue(ennemy.getDamages());
                ennemy.setDamages(newDamages > 50 ? 50 : newDamages);
                //ennemy.setAttackSpeed(mutateValue(ennemy.getAttackSpeed()));
            }
        }

        return nouvellePopulation;
    }

    /**
     * Méthode pour muter une valeur (ajouter un peu de bruit, 5% ici)
     * @param value la valeur à muter
     * @return la valeur mutée
     */
    private double mutateValue(double value) {
        //return value * (1 + (Math.random() * 0.10 - 0.05));
        return value * (1 + (-0.02 + Math.random() * 0.07));
    }

    /**
     * Méthode pour croiser deux ennemis et générer un enfant.
     * À personnaliser en fonction des propriétés des géants.
     */
    private Ennemy croiser(Ennemy parent1, Ennemy parent2) {
        // Exemple de croisement (à adapter selon la structure de l'ennemy)
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

    ArrayList<Ennemy> croiserGroupes(ArrayList<Ennemy> g1, ArrayList<Ennemy> g2) {
        ArrayList<Ennemy> enfant = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < g1.size(); i++) {
            //on vérifie que la liste ne contient pas déjà l'ennemi sinon on en ajoute un autre
            Ennemy e = randomChoice(
                    g1.get(random.nextInt(g1.size())),
                    g2.get(random.nextInt(g2.size()))
            );
            while (enfant.contains(e)) {
                e = randomChoice(
                        g1.get(random.nextInt(g1.size())),
                        g2.get(random.nextInt(g2.size()))
                );
            }
            enfant.add(e);
        }
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

    public static void refreshEnnemiesAndAdd(Ennemy ennemy, ModeleLabyrinth jeu, List<Ennemy> copieGroupe) {
        // Réinitialisation des attributs de base
        ennemy.setLastAttackCount(0);
        //ennemy.setSurvivalTime(0);
        ennemy.setKillerType(null);
        ennemy.setIsDead(false);
        ennemy.setToStart(jeu);
        ennemy.setArrived(false);

        // Réinitialisation des stats
        double[] statsStart = Evolution.startStats.get(ennemy);
        if (statsStart != null) {
            ennemy.setHealth(statsStart[0]);
            ennemy.setSpeed(statsStart[1]);
            ennemy.setDamages(statsStart[2]);
            ennemy.setAttackSpeed(statsStart[3]);
        } else {
            System.err.println("Aucune stats sauvegardée pour " + ennemy.getName());
        }

        // Ajout à la copie du groupe
        copieGroupe.add(ennemy);
    }

}
