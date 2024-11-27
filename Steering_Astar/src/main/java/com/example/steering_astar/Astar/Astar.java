package com.example.steering_astar.Astar;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Astar {
    boolean isValid(char[][] grid, int rows, int cols, Pair point) {
        if (rows > 0 && cols > 0)
            return (point.first >= 0) && (point.first < rows)
                    && (point.second >= 0)
                    && (point.second < cols);

        return false;
    }


    boolean isUnBlocked(char[][] grid, int rows, int cols,
                        Pair point) {
        return isValid(grid, rows, cols, point)
                && grid[point.first][point.second] == '.' || grid[point.first][point.second] == 'E' || grid[point.first][point.second] == 'S';
    }


    boolean isDestination(Pair position, Pair dest) {
        return position == dest || position.equals(dest);
    }


    double calculateHValue(Pair src, Pair dest) {
        return Math.sqrt(Math.pow((src.first - dest.first), 2.0) + Math.pow((src.second - dest.second), 2.0));
    }


    private ArrayList<Pair> tracePath(Cell[][] cellDetails, int cols, int rows, Pair dest) {
        ArrayList<Pair> pathArray = new ArrayList<Pair>();
        System.out.println("The Path:  ");

        Stack<Pair> path = new Stack<>();

        int row = dest.first;
        int col = dest.second;

        Pair nextNode;
        do {
            path.push(new Pair(row, col));
            pathArray.add(new Pair(row, col));
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


    public ArrayList<Pair> aStarSearch(char[][] grid, int rows, int cols, Pair src, Pair dest) {

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


            for (int addX = -1; addX <= 1; addX++) {
                for (int addY = -1; addY <= 1; addY++) {
                    Pair neighbour = new Pair(i + addX, j + addY);
                    if (isValid(grid, rows, cols, neighbour)) {
                        if (cellDetails[neighbour.first] == null) {
                            cellDetails[neighbour.first] = new Cell[cols];
                        }
                        if (cellDetails[neighbour.first][neighbour.second] == null) {
                            cellDetails[neighbour.first][neighbour.second] = new Cell();
                        }

                        if (isDestination(neighbour, dest)) {
                            cellDetails[neighbour.first][neighbour.second].parent = new Pair(i, j);
                            System.out.println("The destination cell is found");
                            ArrayList<Pair> path = tracePath(cellDetails, rows, cols, dest);
                            return path;
                        } else if (!closedList[neighbour.first][neighbour.second]
                                && isUnBlocked(grid, rows, cols, neighbour)) {
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
        }
        System.err.println("Failed to find the Destination Cell");
        return null;
    }


}

