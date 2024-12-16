import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AstarTest {

    char[][] grid = {
            // 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
            /*0*/  { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
            /*1*/  { '#', 'S', '.', '.', '.', '.', '.', '#', '.', '.', '#', '.', '.', '.', '.', 'E' },
            /*2*/  { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '.', '#', '.', '#' },
            /*3*/  { '#', '.', '#', '.', '#', '.', '.', '.', '#', '.', '#', '#', '#', '.', '.', '#' },
            /*4*/  { '#', '.', 'T', '.', '#', '.', '#', '#', '#', '.', '#', '.', '#', '.', '.', '#' },
            /*5*/  { '#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#' },
            /*6*/  { '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#', '#', '.', '#' },
            /*7*/  { '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '#' },
            /*8*/  { '#', '.', '#', '#', '#', 'T', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#' },
            /*9*/  { '#', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#', '.', '#' },
            /*10*/ { '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#', '.', '#' },
            /*11*/ { '#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '#', '.', '#', '.', '#' },
            /*12*/ { '#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#' },
            /*13*/ { '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '#', '.', '#' },
            /*14*/ { '#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#' },
            /*15*/ { '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#' },
            /*16*/ { '#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.', '#' },
            /*17*/ { '#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.' },
            /*18*/ { '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' }
    };

    Pair src = new Pair(1, 1);
    Pair dest = new Pair(1, 15);

    @Test
    @DisplayName("isValid correct")
    void isValidCorrect() {
        Astar astar = new Astar();
        Pair point = new Pair(0, 10);
        assertTrue(astar.isValid(grid, point));
    }

    @Test
    @DisplayName("isValid negative point")
    void isValidIncorrect() {
        Astar astar = new Astar();
        Pair point = new Pair(-2, 10);
        assertFalse(astar.isValid(grid, point));
    }

    @Test
    @DisplayName("isValid point out of bounds")
    void isValidIncorrect2() {
        Astar astar = new Astar();
        Pair point = new Pair(100, 10);
        assertFalse(astar.isValid(grid, point));
    }

    @Test
    @DisplayName("isValid point at 0,0")
    void isValidPoint0() {
        Astar astar = new Astar();
        Pair point = new Pair(0, 0);
        assertTrue(astar.isValid(grid, point));
    }

    @Test
    @DisplayName("isUnblocked correct")
    void isUnblockedCorrect() {
        Astar astar = new Astar();
        Pair point = new Pair(2, 1);
        assertTrue(astar.isUnblocked(grid, point, src, dest));
    }

    @Test
    @DisplayName("isUnblocked blocked")
    void isUnblockedIncorrect() {
        Astar astar = new Astar();
        Pair point = new Pair(0, 0);
        assertFalse(astar.isUnblocked(grid, point, src, dest));
    }

    @Test
    @DisplayName("isUnblocked start point")
    void isUnblockedStart() {
        Astar astar = new Astar();
        Pair point = new Pair(1, 1);
        assertTrue(astar.isUnblocked(grid, point, src, dest));
    }

    @Test
    @DisplayName("isUnblocked end point")
    void isUnblockedEnd() {
        Astar astar = new Astar();
        Pair point = new Pair(15, 1);
        assertTrue(astar.isUnblocked(grid, point, src, dest));
    }

    @Test
    @DisplayName("isUnblocked incorrect point")
    void isUnblockedIncorrectPoint() {
        Astar astar = new Astar();
        Pair point = new Pair(100, 1);
        assertFalse(astar.isUnblocked(grid, point, src, dest));
    }

    @Test
    @DisplayName("isTowerObligatory incorrect point")
    void isTowerObligatoryIncorrectPoint() throws Exception {
        Astar astar = new Astar();
        Pair point = new Pair(2, 1);
        assertThrows(Exception.class, () -> astar.isTowerObligatory(grid, point, src, dest));
    }

    @Test
    @DisplayName("isTowerObligatory true")
    void isTowerObligatoryTrue() throws Exception {
        Astar astar = new Astar();
        Pair point = new Pair(4, 2);
        assertTrue(astar.isTowerObligatory(grid, point, src, dest));
    }

    @Test
    @DisplayName("isTowerObligatory false")
    void isTowerObligatoryFalse() throws Exception {
        Astar astar = new Astar();
        Pair point = new Pair(8, 5);
        assertFalse(astar.isTowerObligatory(grid, point, src, dest));
    }

    @Test
    @DisplayName("isPathBlocked false")
    void isPathBlockedFalse() {
        Astar astar = new Astar();
        assertFalse(astar.isPathBlocked(grid, src, dest));
    }

    @Test
    @DisplayName("isPathBlocked true")
    void isPathBlockedTrue() {
        Astar astar = new Astar();
        char[][] gridBlocked = {
                {'#', '#', '#', '#', '#', '#', '#'},
                {'#', 'S', '#', '#', '#', '#', '#'},
                {'#', '#', '#', '#', 'E', '#', '#'},
                {'#', '#', '#', '#', '#', '#', '#'}
        };
        assertTrue(astar.isPathBlocked(gridBlocked, src, dest));
    }

    @Test
    @DisplayName("gridWithTower equals")
    void gridWithTowerEquals() throws Exception {
        Astar astar = new Astar();
        char[][] gridWithTower = {
                // 0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15
                /*0*/  {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                /*1*/  {'#', 'S', '.', '.', '.', '.', '.', '#', '.', '.', '#', '.', '.', '.', '.', 'E'},
                /*2*/  {'#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '.', '#', '.', '#'},
                /*3*/  {'#', '.', '#', '.', '#', '.', '.', '.', '#', '.', '#', '#', '#', '.', '.', '#'},
                /*4*/  {'#', '.', 'T', '.', '#', '.', '#', '#', '#', '.', '#', '.', '#', '.', '.', '#'},
                /*5*/  {'#', '.', '#', '.', '.', '.', '#', '.', '#', '.', '#', '.', '#', '.', '.', '#'},
                /*6*/  {'#', '.', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#', '#', '.', '#'},
                /*7*/  {'#', '.', '.', '.', '#', '#', '#', '.', '#', '.', '.', '.', '.', '#', '.', '#'},
                /*8*/  {'#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.', '#', '.', '#'},
                /*9*/  {'#', '.', '#', '.', '#', '#', '#', '.', '.', '.', '.', '#', '.', '#', '.', '#'},
                /*10*/ {'#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#', '.', '#'},
                /*11*/ {'#', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '#', '.', '#', '.', '#'},
                /*12*/ {'#', '.', '#', '.', '#', '.', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#'},
                /*13*/ {'#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '.', '#', '.', '#'},
                /*14*/ {'#', '.', '#', '.', '#', '#', '#', '#', '#', '.', '#', '#', '.', '#', '.', '#'},
                /*15*/ {'#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '#', '.', '#'},
                /*16*/ {'#', '.', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '.', '#', '.', '#'},
                /*17*/ {'#', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
                /*18*/ {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
        };
        assertTrue(Arrays.deepEquals(gridWithTower, astar.gridWithTower(grid, grid.length, grid[0].length, src, dest)));
    }

    @Test
    @DisplayName("aStarSearch Kamikaze")
    public void aStarSearchKamikaze() {
        Astar astar = new Astar();
        ArrayList<Pair> chemin = new ArrayList<>(Arrays.asList(
                new Pair(1, 1),
                new Pair(1, 2),
                new Pair(1, 3),
                new Pair(1, 4),
                new Pair(2, 4)
        ));
        assertEquals(chemin, astar.aStarSearch(grid, grid.length, grid[0].length, src, dest, "Kamikaze"));
    }

    @Test
    @DisplayName("aStarSearch Normal")
    public void aStarSearchNormal() {
        Astar astar = new Astar();
        ArrayList<Pair> chemin = new ArrayList<>(Arrays.asList(
                new Pair(1, 1), new Pair(1, 2), new Pair(1, 3), new Pair(1,4),
                new Pair(1, 5), new Pair(1, 6), new Pair(1, 7), new Pair(1, 8),
                new Pair(1, 9), new Pair(1, 10), new Pair(1, 11), new Pair(2, 11),
                new Pair(3, 11), new Pair(3, 10), new Pair(3, 9), new Pair(4, 9),
                new Pair(5, 9), new Pair(6, 9), new Pair(7, 9), new Pair(8, 9),
                new Pair(9, 9), new Pair(10, 9), new Pair(10, 10), new Pair(10, 11),
                new Pair(9, 11), new Pair(9, 12), new Pair(9, 13), new Pair(9, 14),
                new Pair(9, 15), new Pair(10, 15), new Pair(11, 15), new Pair(12, 15),
                new Pair(12, 16), new Pair(12, 17), new Pair(13, 17), new Pair(14, 17),
                new Pair(14, 16), new Pair(14, 15), new Pair(14, 14), new Pair(14, 13),
                new Pair(14, 12), new Pair(14, 11), new Pair(14, 10), new Pair(14, 9),
                new Pair(14, 8), new Pair(14, 7), new Pair(14, 6), new Pair(14, 5),
                new Pair(14, 4), new Pair(14, 3), new Pair(14, 2), new Pair(14, 1),
                new Pair(15, 1)
        ));

        ArrayList<Pair> result = astar.aStarSearch(grid, grid.length, grid[0].length, src, dest, "Normal");

        assertEquals(chemin, result);
    }

    @Test
    @DisplayName("aStarSearch Fugitive")
    public void aStarSearchFugitive() {
        Astar astar = new Astar();
        ArrayList<Pair> chemin = new ArrayList<>(Arrays.asList(
                new Pair(1, 1), new Pair(1, 2), new Pair(1, 3), new Pair(1,4),
                new Pair(1, 5), new Pair(1, 6), new Pair(1, 7), new Pair(1, 8),
                new Pair(1, 9), new Pair(1, 10), new Pair(1, 11), new Pair(2, 11),
                new Pair(3, 11), new Pair(3, 12), new Pair(3, 13), new Pair(4, 13),
                new Pair(5, 13), new Pair(5, 12), new Pair(5, 11),  new Pair(6, 11),
                new Pair(7, 11),  new Pair(7, 10), new Pair(7, 9), new Pair(8, 9),
                new Pair(9, 9), new Pair(10, 9), new Pair(10, 10), new Pair(10, 11),
                new Pair(9, 11), new Pair(9, 12), new Pair(9, 13), new Pair(9, 14),
                new Pair(9, 15), new Pair(10, 15), new Pair(11, 15), new Pair(12, 15),
                new Pair(12, 16), new Pair(12, 17), new Pair(13, 17), new Pair(14, 17),
                new Pair(14, 16), new Pair(14, 15), new Pair(14, 14), new Pair(14, 13),
                new Pair(14, 12), new Pair(14, 11), new Pair(14, 10), new Pair(14, 9),
                new Pair(14, 8), new Pair(14, 7), new Pair(14, 6), new Pair(14, 5),
                new Pair(14, 4), new Pair(14, 3), new Pair(14, 2), new Pair(14, 1),
                new Pair(15, 1)
        ));

        ArrayList<Pair> result = astar.aStarSearch(grid, grid.length, grid[0].length, src, dest, "Fugitive");
        System.out.println(result);
        assertEquals(chemin, result);
    }

}

