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
    boolean isValid(char[][] grid, Pair point) {
        if (grid.length > 0 && grid[0].length > 0)
            return (point.first >= 0) && (point.first < grid.length)
                    && (point.second >= 0)
                    && (point.second < grid[0].length);

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
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return true si le point peut être traversé, false sinon
     */
    boolean isUnblocked(char[][] grid, Pair point, Pair src, Pair dest) {
        if (isValid(grid, point)) {
            char cell = grid[point.first][point.second];
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
    boolean isDestination(Pair position, Pair dest) {
        return position == dest || position.equals(dest);
    }


    /***
     * Calcule la distance heuristique (distance euclidienne) entre deux points.
     * La formule utilisée est : √((x1 - x2)² + (y1 - y2)²)
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return La distance euclidienne entre les deux points
     */
    double calculateHValue(Pair src, Pair dest) {
        return Math.sqrt(Math.pow((src.first - dest.first), 2.0) + Math.pow((src.second - dest.second), 2.0));
    }

    /***
     * Retrace et construit le chemin le plus court à partir des détails de cellules.
     * @param cellDetails Un tableau 2D de cellules contenant les informations
     *                    de parcours pour chaque cellule du chemin
     * @param dest Le point de destination final, représenté par ses coordonnées de ligne et de colonne
     * @return Une liste des coordonnées (Pair) représentant le chemin le plus court,
     *         de la destination à la source
     */
    private ArrayList<Pair> tracePath(Cell[][] cellDetails, Pair dest) {
        ArrayList<Pair> pathArray = new ArrayList<>();
        System.out.println("The Path:  ");

        Stack<Pair> path = new Stack<>();

        int row = dest.first;
        int col = dest.second;

        Pair nextNode;
        do {
            path.push(new Pair(row, col));
            pathArray.addFirst(new Pair(col,row ));
            nextNode = cellDetails[row][col].parent;
            row = nextNode.first;
            col = nextNode.second;
        } while (cellDetails[row][col].parent != nextNode);


        while (!path.empty()) {
            Pair p = path.peek();
            path.pop();
            System.out.println("-> (" + p.first + "," + p.second + ") ");
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
     */
    public char[][] gridWithTower(char[][] grid, int rows, int cols, Pair src, Pair dest) throws Exception {
        // Crée une copie de la grille
        char[][] newGrid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(grid[i], 0, newGrid[i], 0, cols);
        }

        // Parcours chaque cellule pour vérifier les tours
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'T') {
                    Pair currentTower = new Pair(i, j);

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
                                if (isValid(grid, new Pair(newRow, newCol)) && !(di == 0 && dj == 0)) {
                                    newGrid[newRow][newCol] = '#'; // Transforme en mur
                                }
                            }
                        }
                    }
                }
            }
        }

        // Retourner la grille mise à jour avec les tours non obligatoires transformées en murs
        return newGrid;
    }


    /***
     * Teste si la tour est obligatoire (si un ennemi est obligé de passer à côté)
     * @param grid La grille bidimensionnelle à parcourir
     * @param tower La tour à tester
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return true si la tour bloque le chemin, false sinon
     */
    boolean isTowerObligatory(char[][] grid, Pair tower, Pair src, Pair dest) throws Exception {
        // Teste si le caractère est bien une tour
        if (grid[tower.first][tower.second] == 'T') {
            // Crée une copie de la grille pour modifier les alentours de la tour
            char[][] gridCopy = new char[grid.length][grid[0].length];
            for (int i = 0; i < grid.length; i++) {
                System.arraycopy(grid[i], 0, gridCopy[i], 0, grid[i].length);
            }

            // Définir une zone autour de la tour pour être transformée en mur
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = tower.first + i;
                    int newCol = tower.second + j;

                    // Vérifier la validité des indices et ne pas affecter la tour elle-même
                    if (isValid(gridCopy, new Pair(newRow, newCol)) && !(i == 0 && j == 0)) {
                        gridCopy[newRow][newCol] = '#'; // Transforme les alentours de la tour en mur
                    }
                }
            }

            // Vérifier si le chemin est bloqué avec ces murs autour de la tour
            return isPathBlocked(gridCopy, src, dest); // Si le chemin est bloqué, la tour est obligatoire
        }
        else {
            throw new Exception("Le caractère : "+ grid[tower.first][tower.second] +" n'est pas une tour");
        }
    }

    /***
     * Teste si le chemin est bloqué
     * @param grid La grille bidimensionnelle à parcourir
     * @param src Le point de départ du chemin
     * @param dest Le point d'arrivée du chemin
     * @return true si le chemin est bloqué, false sinon
     */
    public boolean isPathBlocked(char[][] grid, Pair src, Pair dest) {
        // Si la source ou la destination ne sont pas valides ou sont bloquées
        if (!isValid(grid, src) || !isValid(grid, dest) || grid[src.first][src.second] == '#' || grid[dest.first][dest.second] == '#') {
            return true;
        }

        // Dimensions de la grille
        int rows = grid.length;
        int cols = grid[0].length;

        // Tableau pour suivre les cellules visitées
        boolean[][] visited = new boolean[rows][cols];

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // File pour la recherche en largeur
        Queue<Pair> queue = new LinkedList<>();
        queue.add(src);
        visited[src.first][src.second] = true;

        while (!queue.isEmpty()) {
            Pair current = queue.poll();

            // Si on atteint la destination
            if (current.equals(dest)) {
                return false; // Un chemin existe, donc il n'est pas bloqué
            }

            // Explorer les voisins
            for (int[] dir : directions) {
                int newRow = current.first + dir[0];
                int newCol = current.second + dir[1];
                Pair neighbor = new Pair(newRow, newCol);

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
    public ArrayList<Pair> aStarSearch(char[][] grid, int rows, int cols, Pair src, Pair dest, String comp) {

        if (comp.equals("Fugitive")) {
            try {
                grid = gridWithTower(grid, rows, cols, src, dest);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        if (comp.equals("Kamikaze")) {
            try {
                int[] newEnd = Pair.getCloserPairIndex(grid, 'T');
                dest = new Pair(newEnd[0], newEnd[1]);
                grid[dest.first][dest.second] = 'E';
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
        if (!isUnblocked(grid, src, src, dest)
                || !isUnblocked(grid, dest, src, dest)) {
            System.err.println("Source or destination is blocked...");
            return null;
        }
        if (isDestination(src, dest)) {
            System.err.println("We're already (t)here...");
            return null;
        }


        boolean[][] closedList = new boolean[rows][cols];
        Cell[][] cellDetails = new Cell[rows][cols];

        int i, j;

        i = src.first;
        j = src.second;
        cellDetails[i][j] = new Cell();
        cellDetails[i][j].f = 0.0;
        cellDetails[i][j].g = 0.0;
        cellDetails[i][j].h = 0.0;
        cellDetails[i][j].parent = new Pair(i, j);

        PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> (int) Math.round(o1.value - o2.value));

        openList.add(new Details(0.0, i, j));

        while (!openList.isEmpty()) {
            Details p = openList.peek();
            i = p.i;
            j = p.j;
            openList.poll();
            closedList[i][j] = true;
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                Pair neighbour = new Pair(i + dir[0], j + dir[1]);
                if (isValid(grid, neighbour)) {
                    if (cellDetails[neighbour.first] == null) {
                        cellDetails[neighbour.first] = new Cell[cols];
                    }
                    if (cellDetails[neighbour.first][neighbour.second] == null) {
                        cellDetails[neighbour.first][neighbour.second] = new Cell();
                    }

                    if (isDestination(neighbour, dest)) {
                        cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                        System.out.println("The destination cell is found");
                        return tracePath(cellDetails, dest);
                    } else if (!closedList[neighbour.first][neighbour.second]
                            && isUnblocked(grid, neighbour, src, dest)) {
                        double gNew, hNew, fNew;
                        gNew = cellDetails[i][j].g + 1.0;
                        hNew = calculateHValue(neighbour, dest);
                        fNew = gNew + hNew;

                        if (cellDetails[neighbour.first][neighbour.second].f == -1
                                || cellDetails[neighbour.first][neighbour.second].f > fNew) {

                            openList.add(new Details(fNew, neighbour.first, neighbour.second));
                            cellDetails[neighbour.first][neighbour.second].g = gNew;
                            cellDetails[neighbour.first][neighbour.second].f = fNew;
                            cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                        }
                    }
                }
            }
        }


        System.err.println("Failed to find the Destination Cell");
        return null;
    }


}

