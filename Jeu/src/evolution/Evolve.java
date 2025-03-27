package evolution;

import entites.enemies.Ennemy;
import laby.ModeleLabyrinth;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public interface Evolve {

    /**
     * Évalue des groupes d'ennemis avec un score null et retourne tout ces groupes avec leur score correspondant.
     * @param stats La map des groupes d'ennemis et leurs scores associés.
     * @return La map mise à jour des groupes d'ennemis et leurs nouveaux scores.
     */
    HashMap<ArrayList<Ennemy>, Double> evaluate(HashMap<ArrayList<Ennemy>, Double> stats) throws IOException;

    /**
     * Simule une manche de jeu pour évaluer la performance d'un groupe d'ennemis.
     * @param jeu L'objet du jeu qui gère la simulation.
     * @return Le score obtenu pour le groupe d'ennemis à la fin de la simulation.
     */
    double simulate(ModeleLabyrinth jeu);

    /**
     * Fonction de score.
     * @param e La liste des ennemis à évaluer.
     * @return Le score final.
     */
    double getScore(ArrayList<Ennemy> e);

    /**
     * Applique une évolution à une population d'ennemis en sélectionnant les meilleurs groupes,
     * croisant les parents et appliquant des mutations.
     * @param ennemies La map des groupes d'ennemis avec leurs scores.
     * @return La nouvelle population d'ennemis après l'évolution.
     */
    ArrayList<ArrayList<Ennemy>> evolve(HashMap<ArrayList<Ennemy>, Double> ennemies);
}
