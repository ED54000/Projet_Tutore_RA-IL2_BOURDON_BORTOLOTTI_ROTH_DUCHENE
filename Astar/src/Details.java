/***
 * Classe représentant un élément avec une valeur et ses coordonnées.
 *
 * Cette classe stocke une valeur numérique associée à des coordonnées
 * dans un espace bidimensionnel (i, j).
 */
public  class Details {

    /***
     * La valeur numérique associée à cet élément.
     * Peut représenter un poids, un coût, ou toute autre mesure quantitative.
     */
    double value;

    /***
     * Coordonnée de ligne/rangée pour cet élément.
     */
    int i;

    /***
     * Coordonnée de colonne pour cet élément.
     */
    int j;

     /***
     * Constructeur pour créer un nouvel objet Details.
     *
     * @param value La valeur numérique à associer aux coordonnées
     * @param i     La coordonnée de ligne
     * @param j     La coordonnée de colonne
     */
    public Details(double value, int i, int j) {
        this.value = value;
        this.i = i;
        this.j = j;
    }
}