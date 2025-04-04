package steering_astar.Steering;

import laby.ModeleLabyrinth;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.round;


/***
 * classe definissant des coordonnees reelles x,y
 * et des methodes pour effectuer des calculs sur ces coordonnees
 */
public class Vector2D {
    private double x, y;

    /***
     * constructeur de la classe
     * @param x coordonnee selon l'axe des x
     * @param y coordonnee selon l'axe des y
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /***
     * methode effectuer une addition de coordonnees
     * @param v coordonnees qu'on additionne aux coordonnees courantes
     * @return de nouvelles coordonnees correspondant a l'addition des
     * coordonnees courantes et des coordonnees du parametre v
     */
    public Vector2D add(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    /***
     * methode effectuer une soustraction de coordonnees
     * @param v coordonnees qu'on soustrait aux coordonnees courantes
     * @return de nouvelles coordonnees correspondant a la soustraction des
     * coordonnees courantes et des coordonnees du parametre v
     */
    public Vector2D subtract(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    /***
     * methode effectuer une multiplication de coordonnees
     * @param scalar reel par lequel on va multiplier les coordonnees courantes
     * @return de nouvelles coordonnees correspondant a la multiplication des
     * coordonnees courantes et du parametre scalar
     */
    public Vector2D scale(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /***
     * methode effectuer une division de coordonnees
     * @param diviser reel par lequel on va diviser les coordonnees courantes
     * @return de nouvelles coordonnees correspondant a la division des
     * coordonnees courantes et du parametre diviser
     */
    public Vector2D divide(double diviser) {
        return new Vector2D(this.x / diviser, this.y / diviser);
    }

    /***
     * methode permettant de calculer le module d'un vecteur
     * @return un reel correspondant au module (longueur) du vecteur
     */
    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /***
     * methode permettant de normaliser un vecteur, donc de faire passer ses coordonnées entre 0 et 1
     * @return le nouveau vecteur avec les coordonnées normalisées
     */
    public Vector2D normalize() {
        double mag = magnitude();
        return mag == 0 ? new Vector2D(0, 0) : scale(1 / mag);
    }

    /***
     * methode calculant la distance entre deux coordonnees
     * @param other les coordonnees du point pour lequel on veut mesurer la distance
     * @return un reel correspondant a la distance entre les coordonnees sur lequel
     * on appelle la methode et les coordonnees du parametre other
     */
    public double distanceTo(Vector2D other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(x, vector2D.x) == 0 && Double.compare(y, vector2D.y) == 0;
    }



    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int[] getCaseFromVector(Vector2D vector) {
        int x = (int) round(vector.x);
        int y = (int) round(vector.y);
        return new int[]{x, y};
    }

    public boolean isObstacle() {
        ModeleLabyrinth m = ModeleLabyrinth.getLabyrinth();

        int[] coordCase = getCaseFromVector(this);
        int x = coordCase[0];
        int y = coordCase[1];

        if (x < 0 || y < 0 || x >= m.getCases()[0].length || y >= m.getCases().length) {
            return true;
        }

        char theCase = m.getCase(y, x);
        return theCase == '#' ;
    }

    public Vector2D getClosestCaseCenter(){
        int[] coordCase = getCaseFromVector(this);
        return new Vector2D(coordCase[0] * ModeleLabyrinth.getTailleCase(), coordCase[1] * ModeleLabyrinth.getTailleCase());
    }

    public Vector2D div(int i) {
        return new Vector2D(this.x / i, this.y / i);
    }
}

