import java.util.ArrayList;

public class Pair {
    int first;
    int second;

    /***
     * Constructeur de la classe Pair.
     *
     * @param first  La coordonnée de ligne
     * @param second La coordonnée de colonne
     */
    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    /***
     * Trouve l'index de la première occurrence du caractère cible le plus proche dans la grille.
     * Le caractère le plus proche par rapport aux colonnes
     *
     * @param grid   La grille de caractères à parcourir
     * @param target Le caractère à rechercher
     * @return Un tableau d'entiers contenant les coordonnées [ligne, colonne]
     *         du caractère le plus proche
     * @throws Exception Si aucun caractère cible n'est trouvé dans la grille
     */
    public static int[] getCloserPairIndex(char[][] grid, char target) throws Exception {
        ArrayList<Pair> PairIndex = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == target) {
                    PairIndex.add(new Pair(row, col));
                }
            }
        }
        if (PairIndex.isEmpty()) {
            throw new Exception("Character '" + target + "' not found in the grid");
        }
        Pair closest = PairIndex.getFirst();
        for (int i = 1; i < PairIndex.size(); i++) {
            closest = getCloser(PairIndex.get(i), closest);
        }
        return new int[]{closest.first, closest.second};
    }

    /***
     * Trouve l'index de la première occurrence du caractère cible dans la grille.
     *
     * @param grid   La grille de caractères à parcourir
     * @param target Le caractère à rechercher
     * @return Un tableau d'entiers contenant les coordonnées [ligne, colonne] du caractère
     * @throws Exception Si aucun caractère cible n'est trouvé dans la grille
     */
    public static int[] getPairIndex(char[][] grid, char target) throws Exception {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == target) {
                    return new int[]{row, col};
                }
            }
        }
        throw new Exception("Character '" + target + "' not found in the grid");
    }

    /***
     *  Vérifie la présence d'un départ et d'une arrivée dans la grille
     * @param grid La grille de caractères à valider
     * @return true si la grille contient un départ et une arrivée, false sinon
     */
    public static boolean validateGrid(char[][] grid) {
        boolean isStart = false;
        boolean isEnd = false;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'S') {
                    isStart = true;
                } else if (grid[row][col] == 'E') {
                    isEnd = true;
                }
            }
        }
        return isStart && isEnd;
    }

    /***
     * Vérifie l'égalité entre deux objets Pair.
     * Deux Pair sont considérés égaux si leurs coordonnées first et second
     * sont identiques.
     *
     * @param obj L'objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair && this.first == ((Pair) obj).first && this.second == ((Pair) obj).second;
    }

    /**
     * Détermine la paire avec la coordonnée de colonne la plus petite.
     *
     * @param p1 La première paire de coordonnées
     * @param p2 La deuxième paire de coordonnées
     * @return La paire avec la coordonnée de colonne la plus petite
     */
    private static Pair getCloser(Pair p1, Pair p2) {
        if (p1.second < p2.second) {
            return p1;
        } else
            return p2;
    }


    @Override
    public String toString() {
        return "("+first +";"+second+")";
    }
}