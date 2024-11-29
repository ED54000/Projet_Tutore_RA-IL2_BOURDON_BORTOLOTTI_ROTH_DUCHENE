public class Cell {
    public Pair parent;

    public double f, g, h;

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

    /**
     * Constructeur personnalisé permettant de définir tous les attributs de la cellule.
     *
     * @param parent Coordonnées du nœud parent dans le chemin
     * @param f      Coût total estimé du chemin
     * @param g      Coût réel du chemin depuis le point de départ
     * @param h      Estimation heuristique du coût restant jusqu'à la destination
     */
    public Cell(Pair parent, double f, double g, double h) {
        this.parent = parent;
        this.f = f;
        this.g = g;
        this.h = h;
    }

}