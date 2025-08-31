import java.util.*;

public class SofaMover {

    static class SofaState {
        int row;
        int col;
        int orientation; // 0 for horizontal, 1 for vertical
        int steps;

        SofaState(int row, int col, int orientation, int steps) {
            this.row = row;
            this.col = col;
            this.orientation = orientation;
            this.steps = steps;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int R = sc.nextInt();
        int C = sc.nextInt();
        sc.nextLine();

        char[][] grid = new char[R][C];
        SofaState startState = null;
        int sRow = -1, sCol = -1;
        int SRow1 = -1, SCol1 = -1, SRow2 = -1, SCol2 = -1;

        for (int i = 0; i < R; i++) {
            String[] row = sc.nextLine().split(" ");
            for (int j = 0; j < C; j++) {
                grid[i][j] = row[j].charAt(0);
                if (grid[i][j] == 's') {
                    if (sRow == -1) {
                        sRow = i;
                        sCol = j;
                    }
                } else if (grid[i][j] == 'S') {
                    if (SRow1 == -1) {
                        SRow1 = i;
                        SCol1 = j;
                    } else {
                        SRow2 = i;
                        SCol2 = j;
                    }
                }
            }
        }
        sc.close();

        // Determine initial sofa orientation and create the start state
        if (sRow != -1 && sCol != -1) {
            if (sCol + 1 < C && grid[sRow][sCol + 1] == 's') { // Horizontal
                startState = new SofaState(sRow, sCol, 0, 0);
            } else if (sRow + 1 < R && grid[sRow + 1][sCol] == 's') { // Vertical
                startState = new SofaState(sRow, sCol, 1, 0);
            }
        }

        // Determine the orientation of the destination
        int destinationOrientation = (SCol1 + 1 == SCol2) ? 0 : 1;
        int destinationRow = Math.min(SRow1, SRow2);
        int destinationCol = Math.min(SCol1, SCol2);

        int result = findMinSteps(R, C, grid, startState, destinationRow, destinationCol, destinationOrientation);

        if (result == -1) {
            System.out.println("Impossible");
        } else {
            System.out.println(result);
        }
    }

    private static int findMinSteps(int M, int N, char[][] grid, SofaState startState,
                                    int destRow, int destCol, int destOrientation) {

        // If the start and destination are the same
        if (startState.row == destRow && startState.col == destCol && startState.orientation == destOrientation) {
            return 0;
        }

        Queue<SofaState> queue = new LinkedList<>();
        boolean[][][] visited = new boolean[M][N][2];

        queue.add(startState);
        visited[startState.row][startState.col][startState.orientation] = true;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            SofaState current = queue.poll();

            // Check if we have reached the destination
            if (current.row == destRow && current.col == destCol && current.orientation == destOrientation) {
                return current.steps;
            }

            // Move the sofa
            for (int i = 0; i < 4; i++) {
                int newR = current.row + dr[i];
                int newC = current.col + dc[i];
                int newOrientation = current.orientation;

                // Check if the move is valid
                boolean isValid = false;
                if (newOrientation == 0) { // Horizontal

                    if (newR >= 0 && newR < M && newC >= 0 && newC + 1 < N &&
                            grid[newR][newC] != 'H' && grid[newR][newC + 1] != 'H') {
                        isValid = true;
                    }
                } else { // Vertical

                    if (newR >= 0 && newR + 1 < M && newC >= 0 && newC < N &&
                            grid[newR][newC] != 'H' && grid[newR + 1][newC] != 'H') {
                        isValid = true;
                    }
                }

                // If valid and not visited, add to queue
                if (isValid && !visited[newR][newC][newOrientation]) {
                    visited[newR][newC][newOrientation] = true;
                    queue.add(new SofaState(newR, newC, newOrientation, current.steps + 1));
                }
            }

            // Rotate the sofa ---
            int newOrientation = (current.orientation == 0) ? 1 : 0;

            boolean canRotate = false;

            if (current.orientation == 0) { // Horizontal to Vertical
                // Check if the 2x2 area is clear
                if (current.row + 1 < M && current.col + 1 < N &&
                        grid[current.row][current.col] != 'H' && grid[current.row][current.col + 1] != 'H' &&
                        grid[current.row + 1][current.col] != 'H' && grid[current.row + 1][current.col + 1] != 'H') {

                    canRotate = true;
                }
            } else { // Vertical to Horizontal
                // Check if the 2x2 area is clear
                if (current.row + 1 < M && current.col + 1 < N &&
                        grid[current.row][current.col] != 'H' && grid[current.row][current.col + 1] != 'H' &&
                        grid[current.row + 1][current.col] != 'H' && grid[current.row + 1][current.col + 1] != 'H') {

                    canRotate = true;
                }
            }

            // If rotation is possible and the new state hasn't been visited
            if (canRotate && !visited[current.row][current.col][newOrientation]) {
                visited[current.row][current.col][newOrientation] = true;
                queue.add(new SofaState(current.row, current.col, newOrientation, current.steps + 1));
            }
        }

        return -1; // Impossible to reach
    }
}