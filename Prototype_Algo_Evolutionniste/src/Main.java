import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Ennemy> ennemies = List.of(
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Plant", 100, 5, 8, 2, 5, "Fugitive"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Healer"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Water", 90, 6, 12, 3, 5, "Kamikaze"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal"),
            new Ennemy(0, 0, "Fire", 80, 4, 15, 1, 5, "Normal")
        );

        Ennemy[][] bestCouples = EnnemyEvolution.getBestCouples(ennemies);
        System.out.println("Meilleurs couples : \n" + bestCouples[0][0].toString() + bestCouples[0][1].toString() + bestCouples[1][0].toString() + bestCouples[1][1].toString() + bestCouples[2][0].toString() + bestCouples[2][1].toString() + bestCouples[3][0].toString() + bestCouples[3][1].toString());

        System.out.println("\nMeilleur couple Fugitifs : \n" + bestCouples[0][0].toString() + bestCouples[0][1].toString());
        System.out.println("Tous les ennemis fugitifs : \n" + ennemies.get(0).toString() + ennemies.get(1).toString() + ennemies.get(2).toString()+ ennemies.get(3).toString()+ ennemies.get(4).toString()+ ennemies.get(5).toString()+ ennemies.get(6).toString()+ ennemies.get(7).toString()+ ennemies.get(8).toString());
        System.out.println("\nMeilleur couple Healers : \n" + bestCouples[1][0].toString() + bestCouples[1][1].toString());
        System.out.println("Tous les ennemis Healers : \n" + ennemies.get(9).toString() + ennemies.get(10).toString() + ennemies.get(11).toString()+ ennemies.get(12).toString()+ ennemies.get(13).toString()+ ennemies.get(14).toString()+ ennemies.get(15).toString()+ ennemies.get(16).toString()+ ennemies.get(17).toString());
        //System.out.println("Ennemis morts : " + EnnemyEvolution.getDeadEnnemies(ennemies));

        double[][] averageStats = EnnemyEvolution.getAverageStats(ennemies);
        for(int i = 0; i < averageStats.length; i++) {
            System.out.println("Moyenne des statistiques du couple " + i + " : ");
            System.out.println("Vie : " + averageStats[i][0]);
            System.out.println("Vitesse : " + averageStats[i][1]);
            System.out.println("Dégâts : " + averageStats[i][2]);
            System.out.println("Vitesse d'attaque : " + averageStats[i][3]);
            System.out.println("Portée : " + averageStats[i][4]);
        }
    }
}
