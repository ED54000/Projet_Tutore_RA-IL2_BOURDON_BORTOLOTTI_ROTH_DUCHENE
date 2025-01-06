package evolution;

import entites.enemies.Ennemy;

import java.util.ArrayList;
import java.util.List;

public class EnnemyEvolution {

    public final static double distStartEnd = 200; //a enlever

    /**
     * Retourne les meilleurs couples d'ennemis pour chaque type
     * @param ennemies la liste complète des ennemis
     * @return un tableau à deux dimensions d'ennemis, chaque sous-tableau contient les deux meilleurs ennemis d'un type
     */
    public static Ennemy[][] getBestCouples(List<Ennemy> ennemies) {
        // Inititation des données nécessaires
        Ennemy[][] bestCouples = {{null, null}, {null, null}, {null, null}, {null, null}}; // [0] : meilleur couple fuyard, [1] : meilleur couple normal, [2] : meilleur couple healer, [3] : meilleur couple kamikaze
        Ennemy[] bestFugitives = {null, null}; // [0] : meilleur ennemi, [1] : deuxième meilleur ennemi
        Ennemy[] bestNormals = {null, null};
        Ennemy[] bestHealers = {null, null};
        Ennemy[] bestKamikazes = {null, null};
        double[] bestFugitiveScores = {0, 0}; //[0] : meilleur score, [1] : deuxième meilleur score
        double[] bestNormalScores = {0, 0};
        double[] bestHealerScores = {0, 0};
        double[] bestKamikazeScores = {0, 0};

        // On parcourt la liste des ennemis :
        for (Ennemy e : ennemies) {
            switch (e.getBehavior()){
                case "Fugitive":
                    // On calcule le score de l'ennemi
                    double score = distStartEnd - e.getDistanceToArrival() + 0.2 * e.getSurvivalTime();
                    // On ajoute des points si l'ennemi est arrivé
                    if(e.isItArrived()){
                        score += 10000;
                    }
                    // Si le score est meilleur que le score du second meilleur ennemi actuel
                    if(score > bestFugitiveScores[1]){
                        // Si le score est meilleur que le score du meilleur ennemi actuel
                        if(score > bestFugitiveScores[0]){
                            // On décale le meilleur ennemi actuel en second meilleur ennemi
                            bestFugitives[1] = bestFugitives[0];
                            bestFugitiveScores[1] = bestFugitiveScores[0];
                            // On met à jour le meilleur ennemi actuel
                            bestFugitives[0] = e;
                            bestFugitiveScores[0] = score;
                        } else {
                            // Sinon on met à jour le second meilleur ennemi
                            bestFugitives[1] = e;
                            bestFugitiveScores[1] = score;
                        }
                    }
                    break;
                case "Normal":
                    // On calcule le score de l'ennemi
                    score = 1/e.getDistanceToArrival() + 0.2 * e.getSurvivalTime();
                    // On ajoute des points si l'ennemi est arrivé
                    if(e.isItArrived()){
                        score += 10000;
                    }
                    // Si le score est meilleur que le score du second meilleur ennemi actuel
                    if(score > bestNormalScores[1]){
                        // Si le score est meilleur que le score du meilleur ennemi actuel
                        if(score > bestNormalScores[0]){
                            // On décale le meilleur ennemi actuel en second meilleur ennemi
                            bestNormals[1] = bestNormals[0];
                            bestNormalScores[1] = bestNormalScores[0];
                            // On met à jour le meilleur ennemi actuel
                            bestNormals[0] = e;
                            bestNormalScores[0] = score;
                        } else {
                            // Sinon on met à jour le second meilleur ennemi
                            bestNormals[1] = e;
                            bestNormalScores[1] = score;
                        }
                    }
                    break;
                case "Healer":
                    // On calcule le score de l'ennemi
                    score = 1/e.getDistanceToArrival() + 0.2 * e.getSurvivalTime();
                    // On ajoute des points si l'ennemi est arrivé
                    if(e.isItArrived()){
                        score += 10000;
                    }
                    // Si le score est meilleur que le score du second meilleur ennemi actuel
                    if(score > bestHealerScores[1]){
                        // Si le score est meilleur que le score du meilleur ennemi actuel
                        if(score > bestHealerScores[0]){
                            // On décale le meilleur ennemi actuel en second meilleur ennemi
                            bestHealers[1] = bestHealers[0];
                            bestHealerScores[1] = bestHealerScores[0];
                            // On met à jour le meilleur ennemi actuel
                            bestHealers[0] = e;
                            bestHealerScores[0] = score;
                        } else {
                            // Sinon on met à jour le second meilleur ennemi
                            bestHealers[1] = e;
                            bestHealerScores[1] = score;
                        }
                    }
                    break;
                case "Kamikaze":
                    // On calcule le score de l'ennemi
                    score = 1/e.getDistanceToArrival() + 0.2 * e.getSurvivalTime();
                    // On ajoute des points si l'ennemi est arrivé
                    if(e.isItArrived()){
                        score += 10000;
                    }
                    // Si le score est meilleur que le score du second meilleur ennemi actuel
                    if(score > bestKamikazeScores[1]){
                        // Si le score est meilleur que le score du meilleur ennemi actuel
                        if(score > bestKamikazeScores[0]){
                            // On décale le meilleur ennemi actuel en second meilleur ennemi
                            bestKamikazes[1] = bestKamikazes[0];
                            bestKamikazeScores[1] = bestKamikazeScores[0];
                            // On met à jour le meilleur ennemi actuel
                            bestKamikazes[0] = e;
                            bestKamikazeScores[0] = score;
                        } else {
                            // Sinon on met à jour le second meilleur ennemi
                            bestKamikazes[1] = e;
                            bestKamikazeScores[1] = score;
                        }
                    }
                    break;
            }
        }

        // On met à jour le tableau des meilleurs couples
        bestCouples[0] = bestFugitives;
        bestCouples[1] = bestNormals;
        bestCouples[2] = bestHealers;
        bestCouples[3] = bestKamikazes;

        // On le retourne
        return bestCouples;
    }

    /**
     * Retourne les statistiques moyennes des meilleurs ennemis
     * @param ennemies la liste complète des ennemis
     * @return
     */
    public static double[][] getAverageStats(List<Ennemy> ennemies) {
        // On récupère les meilleurs couples
        Ennemy[][] bestCouples = getBestCouples(ennemies);
        // On créee un tableau pour les statistiques moyennes
        double[][] averageStats = new double[4][5]; // [0] : fuyarts, [1] : normaux, [2] : soigneurs, [3] : kamikazes
        // [][0] : vie, [][1] : vitesse, [][2] : dégats, [][3] : vitesse d'attaque, [][4] : portée

        // On parcourt les couples de meilleurs ennemis
        for (int i = 0; i < bestCouples.length; i++) {
            // On crée des variables pour les statistiques moyennes
            double healthSum = 0;
            double speedSum = 0;
            double damagesSum = 0;
            double attackSpeedSum = 0;
            double rangeSum = 0;
            // On parcourt les ennemis de chaque couple
            for (int j = 0; j < bestCouples[i].length; j++) {
                // Si l'ennemi n'est pas null
                if(bestCouples[i][j] != null){
                    // On récupère l'ennemi
                    Ennemy e = bestCouples[i][j];
                    // On met à jour les statistiques moyennes
                    healthSum += e.getHealth();
                    speedSum += e.getSpeed();
                    damagesSum += e.getDamages();
                    attackSpeedSum += e.getAttackSpeed();
                    rangeSum += e.getRange();
                }
            }
            // Si il y a deux ennemis dans le couple
            if (bestCouples[i][1] != null && bestCouples[i][0] != null) {
                // On calcule les statistiques moyennes
                averageStats[i][0] = healthSum / 2;
                averageStats[i][1] = speedSum / 2;
                averageStats[i][2] = damagesSum / 2;
                averageStats[i][3] = attackSpeedSum / 2;
                averageStats[i][4] = rangeSum / 2;
            }
            // Sinon si il n'y a qu'un ennemi dans le couple
            else if (bestCouples[i][1] == null && bestCouples[i][0] != null) {
                // On ne modifie pas les statistiques moyennes (la catégorie de l'ennemi sera par contre changée)
                averageStats[i][0] = healthSum;
                averageStats[i][1] = speedSum;
                averageStats[i][2] = damagesSum;
                averageStats[i][3] = attackSpeedSum;
                averageStats[i][4] = rangeSum;
            }
            else if (bestCouples[i][0] != null) {
                // Sinon on met les statistiques moyennes à 0
                averageStats[i][0] = 0;
                averageStats[i][1] = 0;
                averageStats[i][2] = 0;
                averageStats[i][3] = 0;
                averageStats[i][4] = 0;
            }
        }
        return averageStats;
    }

    /**
     * Affecte les statistiques moyennes des meilleurs ennemis aux ennemis morts
     * @param ennemies la liste complète des ennemis
     */
    public static ArrayList<Ennemy> affectStatsToDeadEnnemies(ArrayList<Ennemy> ennemies) {
        // On récupère les ennemis morts
        ArrayList<Ennemy> deadEnnemies = getDeadEnnemies(ennemies);
        // On récupère les statistiques moyennes
        double[][] averageStats = getAverageStats(ennemies);
        // On parcourt les ennemis morts
        for (Ennemy e : deadEnnemies) {
            // En fonction du comportement de l'ennemi
            switch (e.getBehavior()) {
                case "Fugitive" :
                    // Si l'ennemi est mort, on lui affecte son killerType, sinon son type ne change pas
                    if(e.isDead()){
                        // On affecte le type de l'ennemi (le type de la tour qui l'a tué)
                        e.setType(e.getKillerType());
                    }
                    // On affecte les statistiques moyennes des fuyards
                    e.setHealth(averageStats[0][0]);
                    e.setSpeed(averageStats[0][1]);
                    e.setDamages(averageStats[0][2]);
                    e.setAttackSpeed(averageStats[0][3]);
                    e.setRange(averageStats[0][4]);
                    break;
                case "Normal" :
                    // Si l'ennemi est mort, on lui affecte son killerType, sinon son type ne change pas
                    if(e.isDead()){
                        // On affecte le type de l'ennemi (le type de la tour qui l'a tué)
                        e.setType(e.getKillerType());
                    }
                    // On affecte les statistiques moyennes des normaux
                    e.setHealth(averageStats[1][0]);
                    e.setSpeed(averageStats[1][1]);
                    e.setDamages(averageStats[1][2]);
                    e.setAttackSpeed(averageStats[1][3]);
                    e.setRange(averageStats[1][4]);
                    break;
                case "Healer" :
                    // Si l'ennemi est mort, on lui affecte son killerType, sinon son type ne change pas
                    if(e.isDead()){
                        // On affecte le type de l'ennemi (le type de la tour qui l'a tué)
                        e.setType(e.getKillerType());
                    }
                    // On affecte les statistiques moyennes des soigneurs
                    e.setHealth(averageStats[2][0]);
                    e.setSpeed(averageStats[2][1]);
                    e.setDamages(averageStats[2][2]);
                    e.setAttackSpeed(averageStats[2][3]);
                    e.setRange(averageStats[2][4]);
                    break;
                case "Kamikaze" :
                    // Si l'ennemi est mort, on lui affecte son killerType, sinon son type ne change pas
                    if(e.isDead()){
                        // On affecte le type de l'ennemi (le type de la tour qui l'a tué)
                        e.setType(e.getKillerType());
                    }
                    // On affecte les statistiques moyennes des kamikazes
                    e.setHealth(averageStats[3][0]);
                    e.setSpeed(averageStats[3][1]);
                    e.setDamages(averageStats[3][2]);
                    e.setAttackSpeed(averageStats[3][3]);
                    e.setRange(averageStats[3][4]);
                    break;
            }
        }
        return ennemies;
    }

    /**
     * Ajoute des statistiques aléatoires aux ennemis (mutation)
     * @param ennemies la liste complète des ennemis
     */
    public static ArrayList<Ennemy> addRandomStats(ArrayList<Ennemy> ennemies) {
        // Pour chaque ennemi
        for(Ennemy e: ennemies) {
            // Si l'ennemi n'a pas atteint la fin
            if(!e.isItArrived()) {
                //double randomDouble = min + (Math.random() * (max - min));
                // On ajoute des statistiques aléatoires
                // Vie entre -10 et 10
                e.setHealth(e.getHealth() + (-10 + (Math.random() * (10 - (-10)))));
                // Vitesse entre -2 et 2
                e.setSpeed(e.getSpeed() + (-2 + (Math.random() * (2 - (-2)))));
                // Dégats entre -3 et 3
                e.setDamages(e.getDamages() + (-3 + (Math.random() * (3 - (-3)))));
                // Vitesse d'attaque entre -1 et 1
                e.setAttackSpeed(e.getAttackSpeed() + (-1 + (Math.random() * (1 - (-1)))));
                // Portée entre -1 et 1
                e.setRange(e.getRange() + (-1 + (Math.random() * (1 - (-1)))));
            }
        }
        return ennemies;
    }

    /**
     * Retourne la liste des ennemis morts
     * @param ennemies la liste complète des ennemis
     * @return la liste des ennemis morts
     */
    public static ArrayList<Ennemy> getDeadEnnemies(List<Ennemy> ennemies) {
        // On créee une liste vide pour les ennemis morts
        ArrayList<Ennemy> deadEnnemies = new ArrayList<>();
        // On parcourt la liste des ennemis
        for (Ennemy e : ennemies) {
            // Si l'ennemi est mort, on l'ajoute à la liste des ennemis morts
            if (e.getHealth() <= 0) {
                deadEnnemies.add(e);
            }
        }
        // On retourne la liste des ennemis morts
        return deadEnnemies;
    }


    public static ArrayList<Ennemy> evoluer(ArrayList<Ennemy> ennemies){
        // On affecte les statistiques moyennes des meilleurs ennemis aux ennemis morts (pour chaque comportement)
        ennemies = affectStatsToDeadEnnemies(ennemies);
        // On ajoute des statistiques aléatoires aux ennemis (mutation)
        return addRandomStats(ennemies);
    }
}

