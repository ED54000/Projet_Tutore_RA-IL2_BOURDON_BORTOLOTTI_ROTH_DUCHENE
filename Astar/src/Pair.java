import java.util.ArrayList;

public class Pair {
    int first;
    int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public static int[] getCloserPairIndex(char[][] grid, char target) throws Exception {
        ArrayList<Pair> PairIndex = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            // Iterate through each column in the current row
            for (int col = 0; col < grid[row].length; col++) {
                // Check if the current cell matches the target character
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

    public static int[] getPairIndex(char[][] grid, char target) throws Exception {
        // Iterate through each row
        for (int row = 0; row < grid.length; row++) {
            // Iterate through each column in the current row
            for (int col = 0; col < grid[row].length; col++) {
                // Check if the current cell matches the target character
                if (grid[row][col] == target) {
                    // Return the indices as an array
                    return new int[]{row, col};
                }
            }
        }

        // Throw an exception if the character is not found
        throw new Exception("Character '" + target + "' not found in the grid");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair && this.first == ((Pair) obj).first && this.second == ((Pair) obj).second;
    }

    private static Pair getCloser(Pair p1, Pair p2) {
        if (p1.second < p2.second) {
            return p1;
        } else
            return p2;
    }


}