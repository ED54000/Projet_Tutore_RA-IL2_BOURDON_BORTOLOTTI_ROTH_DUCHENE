public class Cell {

    /***
     * Coordonnées du nœud parent dans l'algorithme de recherche de chemin.
     * Utilisé pour retracer le chemin optimal une fois la destination atteinte.
     */
    public Pair parent;

    /***
     * Coût total estimé du chemin (f = g + h dans l'algorithme A*).
     * f représente l'estimation complète du coût du chemin.
     */
    public double f;

    /***
     * Coût réel du chemin depuis le point de départ jusqu'à ce nœud.
     * g représente la distance déjà parcourue.
     */
    public double g;

    /***
     * Estimation heuristique du coût restant jusqu'à la destination.
     * h représente l'estimation de la distance restante à parcourir.
     */
    public double h;

    /**
     * Constructeur par défaut initialisant les valeurs à des états par défaut.
     *
     * - Le parent est initialisé aux coordonnées (-1, -1)
     * - Les coûts f, g, et h sont initialisés à -1
     */
    Cell()
    {
        parent = new Pair(-1, -1);
        f = -1;
        g = -1;
        h = -1;
    }
}