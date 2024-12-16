import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    @DisplayName("validateGrid avec un départ ('S') et une arrivée ('E')")
    void testValidateGridWithStartAndEnd() {
        char[][] grid = {
                {'S', '.', '.'},
                {'.', '.', 'E'},
                {'.', '.', '.'}
        };
        assertTrue(Pair.validateGrid(grid), "La grille devrait contenir 'S' et 'E'");
    }

    @Test
    @DisplayName("validateGrid sans départ ('S') et arrivée ('E')")
    void testValidateGridWithoutStartOrEnd() {
        char[][] grid = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        assertFalse(Pair.validateGrid(grid), "La grille ne devrait pas contenir 'S' et 'E'");
    }

    @Test
    @DisplayName("validateGrid avec un départ ('S')  mais sans arrivée ('E')")
    void testValidateGridWithStartNoEnd() {
        char[][] grid = {
                {'S', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        assertFalse(Pair.validateGrid(grid), "La grille devrait contenir 'S' mais pas 'E'");
    }

    @Test
    @DisplayName("validateGrid avec un départ ('S') et une arrivée ('E')")
    void testValidateGridWithNoStartAndEnd() {
        char[][] grid = {
                {'.', '.', '.'},
                {'.', '.', 'E'},
                {'.', '.', '.'}
        };
        assertFalse(Pair.validateGrid(grid), "La grille devrait contenir 'E' mais pas 'S'");
    }


    @Test
    @DisplayName("Trouve la première occurence")
    void testGetPairIndex() throws Exception {
        char[][] grid = {
                {'.', '.', '.'},
                {'.', 'A', '.'},
                {'.', '.', 'B'}
        };
        int[] result = Pair.getPairIndex(grid, 'A');
        assertArrayEquals(new int[]{1, 1}, result, "La position de 'A' devrait être (1,1)");
    }

    @Test
    @DisplayName("Lance une exception si le caractère voulu n'est pas dans la grille")
    void testGetPairIndexThrowsException() {
        char[][] grid = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        Exception exception = assertThrows(Exception.class, () -> Pair.getPairIndex(grid, 'A'));
        assertEquals("Character 'A' not found in the grid", exception.getMessage());
    }

    @Test
    @DisplayName("Trouve l'occurrence la plus proche en colonne")
    void testGetCloserPairIndex() throws Exception {
        char[][] grid = {
                {'.', '.', '.'},
                {'A', '.', '.'},
                {'.', '.', 'A'}
        };
        int[] result = Pair.getCloserPairIndex(grid, 'A');
        assertArrayEquals(new int[]{1, 0}, result, "Le 'A' le plus proche devrait être en (1,0)");
    }

    @Test
    @DisplayName("Lance une exception si le caractère voulu n'est pas dans la grille")
    void testGetCloserPairIndexThrowsException() {
        char[][] grid = {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        Exception exception = assertThrows(Exception.class, () -> Pair.getCloserPairIndex(grid, 'A'));
        assertEquals("Character 'A' not found in the grid", exception.getMessage());
    }

    @Test
    @DisplayName("equals true")
    void testPairEquality() {
        Pair p1 = new Pair(1, 2);
        Pair p2 = new Pair(1, 2);
        assertEquals(p1, p2, "(1,2) et (1,2) devrait être égaux");
    }

    @Test
    @DisplayName("equals false")
    void testPairInequality() {
        Pair p1 = new Pair(1, 2);
        Pair p2 = new Pair(2, 3);
        assertNotEquals(p1, p2, "(1,2) et (2,3) ne devrait pas être égaux");
    }

}
