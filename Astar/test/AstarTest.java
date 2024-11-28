import org.junit.jupiter.api.*;

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

    Pair src = new Pair(1,1);
    Pair dest = new Pair(15,1);

    @Test
    @DisplayName("isValid correct")
    void isValidCorrect(){
        Astar astar = new Astar();
        Pair point = new Pair(0,10);
        assertTrue(astar.isValid(grid,point));
    }

    @Test
    @DisplayName("isValid negative point")
    void isValidIncorrect(){
        Astar astar = new Astar();
        Pair point = new Pair(-2,10);
        assertFalse(astar.isValid(grid,point));
    }

    @Test
    @DisplayName("isValid point out of bounds")
    void isValidIncorrect2(){
        Astar astar = new Astar();
        Pair point = new Pair(100,10);
        assertFalse(astar.isValid(grid,point));
    }

    @Test
    @DisplayName("isValid point at 0,0")
    void isValidPoint0(){
        Astar astar = new Astar();
        Pair point = new Pair(0,0);
        assertTrue(astar.isValid(grid,point));
    }

    @Test
    @DisplayName("isUnblocked correct")
    void isUnblockedCorrect(){
        Astar astar = new Astar();
        Pair point = new Pair(2,1);
        assertTrue(astar.isUnblocked(grid,point,src,dest));
    }

    @Test
    @DisplayName("isUnblocked blocked")
    void isUnblockedIncorrect(){
        Astar astar = new Astar();
        Pair point = new Pair(0,0);
        assertFalse(astar.isUnblocked(grid,point,src,dest));
    }

    @Test
    @DisplayName("isUnblocked start point")
    void isUnblockedStart(){
        Astar astar = new Astar();
        Pair point = new Pair(1,1);
        assertTrue(astar.isUnblocked(grid,point,src,dest));
    }

    @Test
    @DisplayName("isUnblocked end point")
    void isUnblockedEnd(){
        Astar astar = new Astar();
        Pair point = new Pair(15,1);
        assertTrue(astar.isUnblocked(grid,point,src,dest));
    }

    @Test
    @DisplayName("isUnblocked incorrect point")
    void isUnblockedIncorrectPoint(){
        Astar astar = new Astar();
        Pair point = new Pair(100,1);
        assertFalse(astar.isUnblocked(grid,point,src,dest));
    }



}
