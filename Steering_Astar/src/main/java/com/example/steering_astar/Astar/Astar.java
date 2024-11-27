package com.example.steering_astar.Astar;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import com.example.steering_astar.Steering.Vector2D;

public class Astar {
    boolean isValid(char[][] grid, int rows, int cols, Vector2D point) {
        if (rows > 0 && cols > 0)
            return (point.x >= 0) && (point.x < rows)
                    && (point.y >= 0)
                    && (point.y < cols);

        return false;
    }


    boolean isUnBlocked(char[][] grid, int rows, int cols,Vector2D point) {
        int intX = (int)point.x;
        int intY = (int)point.y;
        return isValid(grid, rows, cols, point)
                && grid[intX][intY] == '.' || grid[intX][intY] == 'E' || grid[intX][intY] == 'S';
    }


    boolean isDestination(Vector2D position, Vector2D dest) {
        return position == dest || position.equals(dest);
    }


    double calculateHValue(Vector2D src, Vector2D dest) {
        return Math.sqrt(Math.pow((src.x - dest.x), 2.0) + Math.pow((src.y - dest.y), 2.0));
    }


    private ArrayList<Vector2D> tracePath(Cell[][] cellDetails, int cols, int rows, Vector2D dest) {
        ArrayList<Vector2D> pathArray = new ArrayList<>();
        System.out.println("The Path:  ");

        Stack<Vector2D> path = new Stack<>();

        int row =  (int) dest.x;
        int col =  (int) dest.y;

        Vector2D nextNode;
        do {
            path.push(new Vector2D(col, row));
            pathArray.addFirst(new Vector2D(col*100, row*100));
            nextNode = cellDetails[row][col].parent;
            row = (int) nextNode.x;
            col = (int) nextNode.y;
        } while (cellDetails[row][col].parent != nextNode);


        while (!path.empty()) {
            Vector2D p = path.peek();
            path.pop();
            System.out.println(p.x + " " + p.y);
        }
        return pathArray;
    }


    public ArrayList<Vector2D> aStarSearch(char[][] grid, int rows, int cols, Vector2D src, Vector2D dest) {

        if (!isValid(grid, rows, cols, src)) {
            System.err.println("Source is invalid...");
            return null;
        }
        if (!isValid(grid, rows, cols, dest)) {
            System.err.println("Destination is invalid...");
            return null;
        }
        if (!isUnBlocked(grid, rows, cols, src)
                || !isUnBlocked(grid, rows, cols, dest)) {
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

        i = (int)src.x;
        j = (int) src.y;
        cellDetails[i][j] = new Cell();
        cellDetails[i][j].f = 0.0;
        cellDetails[i][j].g = 0.0;
        cellDetails[i][j].h = 0.0;
        cellDetails[i][j].parent = new Vector2D(i, j);


        PriorityQueue<Details> openList = new PriorityQueue<>((o1, o2) -> (int) Math.round(o1.value - o2.value));


        openList.add(new Details(0.0, i, j));

        while (!openList.isEmpty()) {
            Details p = openList.peek();

            i = (int) p.i;
            j = (int) p.j;

            openList.poll();
            closedList[i][j] = true;


            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                Vector2D neighbour = new Vector2D(i + dir[0], j + dir[1]);
                int intNeighbourX = (int) neighbour.x;
                int intNeighbourY = (int) neighbour.y;
                if (isValid(grid, rows, cols, neighbour)) {
                    if (cellDetails[intNeighbourX] == null) {
                        cellDetails[intNeighbourX] = new Cell[cols];
                    }
                    if (cellDetails[intNeighbourX][intNeighbourY] == null) {
                        cellDetails[intNeighbourX][intNeighbourY] = new Cell();
                    }

                    if (isDestination(neighbour, dest)) {
                        cellDetails[intNeighbourX][intNeighbourY].parent = new Vector2D(i, j);
                        System.out.println("The destination cell is found");
                        ArrayList<Vector2D> path = tracePath(cellDetails, rows, cols, dest);
                        return path;
                    } else if (!closedList[intNeighbourX][intNeighbourY]
                            && isUnBlocked(grid, rows, cols, neighbour)) {
                        double gNew, hNew, fNew;
                        gNew = cellDetails[i][j].g + 1.0;
                        hNew = calculateHValue(neighbour, dest);
                        fNew = gNew + hNew;

                        if (cellDetails[intNeighbourX][intNeighbourY].f == -1
                                || cellDetails[intNeighbourX][intNeighbourY].f > fNew) {

                            openList.add(new Details(fNew, intNeighbourX, intNeighbourY));
                            cellDetails[intNeighbourX][intNeighbourY].g = gNew;
                            cellDetails[intNeighbourX][intNeighbourY].f = fNew;
                            cellDetails[intNeighbourX][intNeighbourY].parent = new Vector2D(i, j);
                        }
                    }
                }
            }
        }
        System.err.println("Failed to find the Destination Cell");
        return null;
    }


}

