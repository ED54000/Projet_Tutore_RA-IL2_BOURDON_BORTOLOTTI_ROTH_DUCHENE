package steering_astar.Astar;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import steering_astar.Steering.Vector2D;

import java.util.*;

import static laby.ModeleLabyrinth.getTailleCase;


/***
 * Classe Astar qui choisi le chemin le plus court avec l'algorithme A*
 * L'algorithme à été repris sur https://codegym.cc/groups/posts/a-search-algorithm-in-java
 * Des modifications on été cependant faites
 * */
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
            return (point.getX() >= 0) && (point.getX()) < grid.length
                    && (point.getY() >= 0)
                    && (point.getY() < grid[0].length);

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
        Vector2D pointDivide = new Vector2D(point.getX(), point.getY());
        if (isValid(grid, pointDivide)) {
            char cell = grid[(int) point.getX()][(int) point.getY()];
            return cell == '.' || cell == 'E' || cell == 'S' || cell == 'B';
        }

        return false;
    }


    /***
     * Vérifie si le point testé est le point d'arrivé
     * @param position Le point que l'on teste
     * @param dest Le point d'arrivée
     * @return true si le point testé est le point d'arrivé false sinon
     */
    public boolean isDestination(Vector2D position, Vector2D dest) {
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
        return Math.sqrt(Math.pow((src.getX() - dest.getX()), 2.0)
                + Math.pow((src.getY() - dest.getY()), 2.0));
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

        double row = dest.getX();
        double col = dest.getY();

        Vector2D nextNode;
        do {
            path.push(new Vector2D(row, col));
            pathArray.addFirst(new Vector2D(col * getTailleCase(), row * getTailleCase()));
            nextNode = cellDetails[(int) row][(int) col].parent;
            row = nextNode.getX();
            col = nextNode.getY();
        } while (cellDetails[(int) row][(int) col].parent != nextNode);


        while (!path.empty()) {
            Vector2D p = path.peek();
            path.pop();
            //System.out.println("-> (" + p.getX() / ViewLabyrinth.getTailleCase() + "," + p.getY() / ViewLabyrinth.getTailleCase() + ") ");
        }
        return pathArray;
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
        double[][] costGrid = createTowerAvoidanceCostGrid(grid, comp);
        char[][] copyGrid = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copyGrid[i] = grid[i].clone();
        }
        if (comp.equals("Kamikaze")) {
            try {
                double[] newEnd = getNearTower(copyGrid);
                dest = new Vector2D(newEnd[0], newEnd[1]);
                copyGrid[(int) dest.getX()][(int) dest.getY()] = 'E';
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        Vector2D destDivide = new Vector2D(dest.getX(), dest.getY());
        Vector2D srcDivide = new Vector2D(src.getX(), src.getY());

        if (!isValid(copyGrid, srcDivide)) {
            System.err.println("Source is invalid...");
            return null;
        }
        if (!isValid(copyGrid, destDivide)) {
            System.err.println("Destination is invalid...");
            return null;
        }
        if (!isUnblocked(copyGrid, srcDivide)
                || !isUnblocked(copyGrid, destDivide)) {
            System.err.println("Source or destination is blocked...");
            return null;
        }
        if (isDestination(src, dest)) {
            System.err.println("We're already (t)here...");
            return null;
        }


        boolean[][] closedList = new boolean[rows][cols];
        Cell[][] cellDetails = new Cell[rows][cols];

        double i, j;

        i = src.getX();
        j = src.getY();
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
            j = (int) p.j;
            openList.poll();
            closedList[(int) i][(int) j] = true;
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                Vector2D neighbour = new Vector2D(i + dir[0], j + dir[1]);
                if (isValid(copyGrid, neighbour)) {
                    if (cellDetails[(int) neighbour.getX()] == null) {
                        cellDetails[(int) neighbour.getX()] = new Cell[cols];
                    }
                    if (cellDetails[(int) neighbour.getX()][(int) neighbour.getY()] == null) {
                        cellDetails[(int) neighbour.getX()][(int) neighbour.getY()] = new Cell();
                    }

                    if (isDestination(neighbour, destDivide)) {
                        cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].parent = new Vector2D(i, j);
                        System.out.println("The destination cell is found");
                        return tracePath(cellDetails, destDivide);
                    } else if (!closedList[(int) neighbour.getX()][(int) neighbour.getY()]
                            && isUnblocked(grid, neighbour)) {
                        double gNew, hNew, fNew;
                        gNew = cellDetails[(int) i][(int) j].g + 1.0;
                        hNew = calculateHValue(neighbour, destDivide) + getTowerPenalty(costGrid, neighbour);
                        fNew = gNew + hNew;

                        if (cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].f == -1
                                || cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].f > fNew) {

                            openList.add(new Details(fNew, neighbour.getX(), neighbour.getY()));
                            cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].g = gNew;
                            cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].f = fNew;
                            cellDetails[(int) neighbour.getX()][(int) neighbour.getY()].parent = new Vector2D(i, j);
                        }
                    }
                }
            }
        }
        System.err.println("Failed to find the Destination Cell");
        return null;
    }

    /***
     * Créé une carte des poids des élements
     * @param grid la grille à tester
     * @param comp le comportement de l'élément
     * @return la grille avec les poids
     */
    private double[][] createTowerAvoidanceCostGrid(char[][] grid, String comp) {
        double[][] costGrid = new double[grid.length][grid[0].length];
        Queue<Vector2D> queue = new LinkedList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        // Initialiser les coûts de base
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (comp.equals("Fugitive") && grid[i][j] == 'C') {
                    queue.add(new Vector2D(i, j));
                    visited[i][j] = true;
                    costGrid[i][j] = 1000.0; // Coût initial très élevé pour les tours
                } else {
                    costGrid[i][j] = 1.0; // Coût minimal pour les chemins traversables
                }
            }
        }

        // Directions pour la propagation BFS
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Propager la pénalité depuis les tours
        while (!queue.isEmpty()) {
            Vector2D current = queue.poll();
            for (int[] dir : directions) {
                int newRow = (int) current.getX() + dir[0];
                int newCol = (int) current.getY() + dir[1];
                Vector2D newPosition = new Vector2D(newRow, newCol);

                // Vérifier la validité de la position
                if (isValid(grid, newPosition) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;

                    // Ajouter un coût progressif autour des tours
                    costGrid[newRow][newCol] = costGrid[(int) current.getX()][(int) current.getY()] / 2;

                    // Si la zone est entourée de tours, ajouter une forte pénalité
                    if (countNearbyTowers(grid, newPosition) >= 3) { // Seuil ajustable
                        costGrid[newRow][newCol] += 50.0;
                    }

                    // Ajouter cette cellule à la file pour une propagation continue
                    queue.add(newPosition);
                }
            }
        }

        // Ajouter une pénalité extrême pour les zones sans issue
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (costGrid[i][j] > 1000.0) {
                    costGrid[i][j] = 100000.0; // Zone pratiquement infranchissable
                }
            }
        }
        return costGrid;
    }

    /**
     * Comptabilise le nombre de tours autour d'une cellule.
     *
     * @param grid     La grille à vérifier
     * @param position La position de la cellule à examiner
     * @return Le nombre de tours dans les cellules voisines
     */
    private int countNearbyTowers(char[][] grid, Vector2D position) {
        int towerCount = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = (int) position.getY() + dir[1];
            int newCol = (int) position.getX() + dir[0];
            Vector2D newPosition = new Vector2D(newRow, newCol);

            if (isValid(grid, newPosition) && grid[newRow][newCol] == 'C') {
                towerCount++;
            }
        }
        return towerCount;
    }

    private double getTowerPenalty(double[][] costGrid, Vector2D point) {
        return costGrid[(int) point.getX()][(int) point.getY()];
    }

    private double[] getNearTower(char[][] grid) {
        char[] charTowers = {'A', 'B', 'C'};
        double[] nearest = {Double.MAX_VALUE, Double.MAX_VALUE};
        double nearestDistance = Double.MAX_VALUE;

        for (char c : charTowers) {
            try {
                double[] near = Vector2D.getCloserPairIndex(grid, c);

                double currentDistance = Math.sqrt(Math.pow(near[0], 2) + Math.pow(near[1], 2));

                if (currentDistance < nearestDistance) {
                    nearestDistance = currentDistance;
                    nearest = near;
                }
            } catch (Exception e) {
                System.err.println("Defense " + c + " not found: " + e.getMessage());

            }
        }

        return nearest;
    }


}
