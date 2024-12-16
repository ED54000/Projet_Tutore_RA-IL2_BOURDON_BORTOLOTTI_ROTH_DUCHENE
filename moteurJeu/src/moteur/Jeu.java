package moteur;


import javafx.scene.canvas.Canvas;

/**
 * modele de jeu
 */
public interface Jeu {

    /**
     * methode mise a jour du jeu
     *
     * @param secondes temps ecoule depuis la derniere mise a jour
     */
    void update(double secondes);

    /**
     * initialisation du jeu
     */
    void init(Canvas canvas);

    /**
     * verifie si le jeu est fini
     *
     * @return booleen true si le jeu est fini
     */
    boolean etreFini();
}
