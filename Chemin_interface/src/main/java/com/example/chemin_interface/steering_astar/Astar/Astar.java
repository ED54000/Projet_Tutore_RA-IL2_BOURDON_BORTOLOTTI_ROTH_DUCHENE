package com.example.chemin_interface.steering_astar.Astar;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import com.example.chemin_interface.steering_astar.Steering.*;

import java.util.*;

/***
 * Classe Astar qui choisi le chemin le plus court avec l'algorithme A*
 */
public class Astar {

    /***
     * Vérifie si un point donné est valide dans une grille bidimensionnelle.
     * Un point est considéré comme valide s'il est situé à l'intérieur des limites de la grille.
     * @param grid La grille bidimensionnelle à vérifier
     * @param point Le point (coordonnées) à valider
     * @return true si le point est à l'intérieur des limites de la grille, false sinon
     */
    boolean isValid(char[][] grid, Vector2D point) {
        if (grid.length > 0 && grid[0].length > 0)
            return (point.x >= 0) && (point.x < grid.length)
                    && (point.y >= 0)
                    && (point.y < grid[0].length);

        return false;
    }

    /***
     * Détermine si un point donné peut être traversé dans la grille.
     * Un point est considéré comme libre (unblocked) s'il remplit l'une des conditions suivantes :
     * - Le point est valide dans la grille
     * - Le point est un chemin ('.')
     * - Le point est une arrivée ('E')
     * - Le point est un départ ('S')
     * - Le point est près d'une tour "obligatoire"
     * @param grid La grille bidimensionnelle à parcourir
     * @param point Le point à vérifier
     * @return true si le point peut être traversé, false sinon
     */
    boolean isUnblocked(char[][] grid, Vector2D point) {
        if (isValid(grid, point)) {
            char cell = grid[(int) point.x][(int) point.y];
            return cell == '.' || cell == 'E' || cell == 'S';
        }

        return false;
    }


    /***
     * Vérifie si le point testé est le point d'arrivé
     * @param position Le point que l'on teste
     * @param dest Le point d'arrivée
     * @return true si le point testé est le point d'arrivé false sinon
     */
    boolean isDestination(Vector2D position, Vector2D dest) {
        return position == dest || position.equals(dest);
    }


    /***
     * Calcule la distance heuristique (distance euclidienne) entre deux points.
     * La formule utilisée est : √((x1 - x2)² + (y1 - y2)²)
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return La distance euclidienne entre les deux points
     */
    double calculateHValue(Vector2D src, Vector2D dest) {
        return Math.sqrt(Math.pow((src.x - dest.x), 2.0) + Math.pow((src.y - dest.y), 2.0));
    }

    /***
     * Retrace et construit le chemin le plus court à partir des détails de cellules.
     * @param cellDetails Un tableau 2D de cellules contenant les informations
     *                    de parcours pour chaque cellule du chemin
     * @param dest Le point de destination final, représenté par ses coordonnées de ligne et de colonne
     * @return Une liste des coordonnées (Vector2D) représentant le chemin le plus court,
     *         de la destination à la source
     */
    private ArrayList<Vector2D> tracePath(Cell[][] cellDetails, Vector2D dest) {
        ArrayList<Vector2D> pathArray = new ArrayList<>();
        System.out.println("The Path:  ");

        Stack<Vector2D> path = new Stack<>();

        double row = dest.x;
        double col = dest.y;

        Vector2D nextNode;
        do {
            path.push(new Vector2D(row, col));
            pathArray.addFirst(new Vector2D(col*50,row*50));
            nextNode = cellDetails[(int) row][(int) col].parent;
            row = nextNode.x;
            col = nextNode.y;
        } while (cellDetails[(int) row][(int) col].parent != nextNode);


        while (!path.empty()) {
            Vector2D p = path.peek();
            path.pop();
            System.out.println("-> (" + p.x + "," + p.y + ") ");
        }
        return pathArray;
    }

    /***
     *
     * @param grid La grille bidimensionnelle à parcourir
     * @param rows Nombre de lignes dans la grille
     * @param cols Nombre de colonnes dans la grille
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return La nouvelle grille avec les tours non obligatoire et leurs environs remplacés des murs
     *  si les tours non obligatoires combinées ne bloquent pas le chemin
     */
    public char[][] gridWithTower(char[][] grid, int rows, int cols, Vector2D src, Vector2D dest) throws Exception {
        // Crée une copie de la grille
        char[][] newGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
        }

        // Parcours chaque cellule pour vérifier les tours
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'T') {
                    Vector2D currentTower = new Vector2D(i, j);

                    // Si la tour n'est pas obligatoire, on la transforme en mur et aussi ses alentours
                    if (!isTowerObligatory(grid, currentTower, src, dest)) {
                        // Transforme la tour elle-même en mur
                        newGrid[i][j] = '#';

                        // Transforme les alentours de la tour en murs
                        for (int di = -1; di <= 1; di++) {
                            for (int dj = -1; dj <= 1; dj++) {
                                int newRow = i + di;
                                int newCol = j + dj;

                                // Vérifie que les indices sont valides et n'affecte pas la tour elle-même
                                if (isValid(grid, new Vector2D(newRow, newCol)) && !(di == 0 && dj == 0)) {
                                    newGrid[newRow][newCol] = '#'; // Transforme en mur
                                }
                            }
                        }
                    }
                }
            }
        }

        // Si le chemin est bloque
        if (isPathBlocked(newGrid,src,dest)){
            return grid;
        }else{
            return newGrid;
        }
    }


    /***
     * Teste si la tour est obligatoire (si un ennemi est obligé de passer à côté)
     * @param grid La grille bidimensionnelle à parcourir
     * @param tower La tour à tester
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return true si la tour bloque le chemin, false sinon
     */
    boolean isTowerObligatory(char[][] grid, Vector2D tower, Vector2D src, Vector2D dest) throws Exception {
        // Teste si le caractère est bien une tour
        if (grid[(int) tower.x][(int) tower.y] == 'T') {
            // Crée une copie de la grille pour modifier les alentours de la tour
            char[][] gridCopy = new char[grid.length][grid[0].length];
            for (int i = 0; i < grid.length; i++) {
                System.arraycopy(grid[i], 0, gridCopy[i], 0, grid[i].length);
            }

            // Définir une zone autour de la tour pour être transformée en mur
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow =(int) tower.x + i;
                    int newCol =(int) tower.y + j;

                    // Vérifier la validité des indices et ne pas affecter la tour elle-même
                    if (isValid(gridCopy, new Vector2D(newRow, newCol)) && !(i == 0 && j == 0)) {
                        gridCopy[newRow][newCol] = '#'; // Transforme les alentours de la tour en mur
                    }
                }
            }

            // Vérifier si le chemin est bloqué avec ces murs autour de la tour
            return isPathBlocked(gridCopy, src, dest); // Si le chemin est bloqué, la tour est obligatoire
        }
        else {
            throw new Exception("Le caractère : "+ grid[(int) tower.x][(int) tower.y] +" n'est pas une tour");
        }
    }

    /***
     * Teste si le chemin est bloqué
     * @param grid La grille bidimensionnelle à parcourir
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return true si le chemin est bloqué, false sinon
     */
    public boolean isPathBlocked(char[][] grid, Vector2D src, Vector2D dest) {
        // Si la source ou la destination ne sont pas valides ou sont bloquées
        if (!isValid(grid, src) || !isValid(grid, dest) || grid[(int) src.x][(int) src.y] == '#' || grid[(int) dest.x][(int) dest.y] == '#') {
            return true;
        }

        // Dimensions de la grille
        int rows = grid.length;
        int cols = grid[0].length;

        // Tableau pour suivre les cellules visitées
        boolean[][] visited = new boolean[rows][cols];

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // File pour la recherche en largeur
        Queue<Vector2D> queue = new LinkedList<>();
        queue.add(src);
        visited[(int) src.x][(int) src.y] = true;

        while (!queue.isEmpty()) {
            Vector2D current = queue.poll();

            // Si on atteint la destination
            if (current.equals(dest)) {
                return false; // Un chemin existe, donc il n'est pas bloqué
            }

            // Explorer les voisins
            for (int[] dir : directions) {
                int newRow = (int) current.x + dir[0];
                int newCol = (int) current.y + dir[1];
                Vector2D neighbor = new Vector2D(newRow, newCol);

                // Vérifier si le voisin est valide et non visité
                if (isValid(grid, neighbor) && !visited[newRow][newCol] && grid[newRow][newCol] != '#') {
                    queue.add(neighbor);
                    visited[newRow][newCol] = true;
                }
            }
        }
        // Si on ne peut pas atteindre la destination
        return true; // Aucun chemin trouvé, donc le chemin est bloqué
    }

    /***
     * Algorithme de choix de chemin A*
     * @param grid La grille bidimensionnelle à parcourir
     * @param rows Nombre de lignes dans la grille
     * @param cols Nombre de colonnes dans la grille
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @param comp Le comportement choisi qui influe sur le choix du chemin
     *     - Par défaut le chemin choisi est le plus court entre le départ et l'arrivé
     *     - Le comportement "Fugitive" fuit les tours et prend le chemin le plus sécurisé
     *     - Le comportement "Kamikaze" considère la tour la plus proche comme son arrivée
     * @return Le chemin le plus court
     */
    public ArrayList<Vector2D> aStarSearch(char[][] grid, int rows, int cols, Vector2D src, Vector2D dest, String comp) {

        if (comp.equals("Fugitive")) {
            try {
                grid = gridWithTower(grid, rows, cols, src, dest);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        if (comp.equals("Kamikaze")) {
            try {
                double[] newEnd = Vector2D.getCloserPairIndex(grid, 'T');
                dest = new Vector2D(newEnd[0], newEnd[1]);
                grid[(int) dest.x][(int) dest.y] = 'E';
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        if (!isValid(grid, src)) {
            System.err.println("Source is invalid...");
            return null;
        }
        if (!isValid(grid, dest)) {
            System.err.println("Destination is invalid...");
            return null;
        }
        if (!isUnblocked(grid, src)
                || !isUnblocked(grid, dest)) {
            System.err.println("Source or destination is blocked...");
            return null;
        }
        if (isDestination(src, dest)) {
            System.err.println("We're already (t)here...");
            return null;
        }


        boolean[][] closedList = new boolean[rows][cols];
        Cell[][] cellDetails = new Cell[rows][cols];

        double i , j;

        i =  src.x;
        j =  src.y;
        cellDetails[(int) i][(int) j] = new Cell();
        cellDetails[(int) i][(int) j].f = 0.0;
        cellDetails[(int) i][(int) j].g = 0.0;
        cellDetails[(int) i][(int) j].h = 0.0;
        cellDetails[(int) i][(int) j].parent = new Vector2D(i, j);

        PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> (int) Math.round(o1.value - o2.value));

        openList.add(new Details(0.0, i, j));

        while (!openList.isEmpty()) {
            Details p = openList.peek();
            i = (int) p.i;
            j =  (int) p.j;
            openList.poll();
            closedList[(int) i][(int) j] = true;
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                Vector2D neighbour = new Vector2D(i + dir[0], j + dir[1]);
                if (isValid(grid, neighbour)) {
                    if (cellDetails[(int) neighbour.x] == null) {
                        cellDetails[(int) neighbour.x] = new Cell[cols];
                    }
                    if (cellDetails[(int) neighbour.x][(int) neighbour.y] == null) {
                        cellDetails[(int) neighbour.x][(int) neighbour.y] = new Cell();
                    }

                    if (isDestination(neighbour, dest)) {
                        cellDetails[(int) neighbour.x][(int) neighbour.y].parent = new Vector2D(i, j);
                        System.out.println("The destination cell is found");
                        return tracePath(cellDetails, dest);
                    } else if (!closedList[(int) neighbour.x][(int) neighbour.y]
                            && isUnblocked(grid, neighbour)) {
                        double gNew, hNew, fNew;
                        gNew = cellDetails[(int) i][(int) j].g + 1.0;
                        hNew = calculateHValue(neighbour, dest);
                        fNew = gNew + hNew;

                        if (cellDetails[(int) neighbour.x][(int) neighbour.y].f == -1
                                || cellDetails[(int) neighbour.x][(int) neighbour.y].f > fNew) {

                            openList.add(new Details(fNew,  neighbour.x, neighbour.y));
                            cellDetails[(int) neighbour.x][(int) neighbour.y].g = gNew;
                            cellDetails[(int) neighbour.x][(int) neighbour.y].f = fNew;
                            cellDetails[(int) neighbour.x][(int) neighbour.y].parent = new Vector2D(i, j);
                        }
                    }
                }
            }
        }


        System.err.println("Failed to find the Destination Cell");
        return null;
    }


}


