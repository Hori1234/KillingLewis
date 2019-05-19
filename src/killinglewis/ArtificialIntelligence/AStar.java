package killinglewis.ArtificialIntelligence;
import killinglewis.utils.Maze;

import java.io.IOException;
import java.util.*;

/**
 * @author ralucaviziteu
 */

public class AStar {

    //Priority queue with the set of cells to be evaluated
    PriorityQueue<Cell> openList;
    //List of cells which have already been evaluated
    private boolean[][] closeList;
    //Cells of our maze
    private Cell[][] maze;
    //Start cell
    private int startX, startY;
    //End cell
    private int endX, endY;
    //Blocked cells array
    private static int[][] blocked;

    public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
        maze = new Cell[width][height];
        closeList = new boolean[width][height];
        openList = new PriorityQueue<Cell>((Cell c1, Cell c2) -> {
            return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
        });

        startCell(si, sj);
        endCell(ei, ej);

        //the heuristic function
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                maze[i][j] = new Cell(i, j);
                maze[i][j].heuristicCost = Math.abs(i - endX) + Math.abs(j - endY);
                maze[i][j].solution = false;
            }
        }

        maze[startX][startY].finalCost = 0;


        //we put the blocks in the maze
        for (int i = 0; i < blocks.length; i++) {
            addBlockOnCell(blocks[i][0], blocks[i][1]);
        }
    }

    public void addBlockOnCell(int i, int j) {
        maze[i][j] = null;
    }

    public void startCell(int i, int j) {
        startX = i;
        startY = j;
    }

    public void endCell(int i, int j) {
        endX = i;
        endY = j;
    }

    public void updateCost(Cell current, Cell t, int cost) {
        if (t == null || closeList[t.i][t.j])
            return;

        int tFinalCost = t.heuristicCost + cost;
        boolean isOpen = openList.contains(t);

        if (!isOpen || tFinalCost < t.finalCost) {
            t.finalCost = tFinalCost;
            t.parent = current;

            if (!isOpen)
                openList.add(t);
        }
    }

    public void process() {

        //we add the start location to the open list
        openList.add(maze[startX][startY]);
        Cell current;

        while (true) {
            current = openList.poll();

            if (current == null)
                break;

            closeList[current.i][current.j] = true;

            if (current.equals(maze[endX][endY]))
                return;

            Cell t;

            if (current.i - 1 >= 0) {
                t = maze[current.i - 1][current.j];
                updateCost(current, t, current.finalCost + 1 );

                /*if (current.j - 1 >= 0) {
                    t = maze[current.i - 1][current.j - 1];
                    updateCost(current, t, current.finalCost + 1);
                }*/

                /*if (current.j + 1 < maze[0].length) {
                    t = maze[current.i - 1][current.j + 1];
                    updateCost(current, t, current.finalCost + 1);
                }*/
            }

            if (current.j - 1 >= 0) {
                t = maze[current.i ][current.j - 1];
                updateCost(current, t, current.finalCost + 1);
            }

            if (current.j + 1 < maze[0].length) {
                t = maze[current.i][current.j + 1];
                updateCost(current, t, current.finalCost + 1);
            }

            if (current.i + 1 < maze.length) {
                t = maze[current.i + 1][current.j];
                updateCost(current, t, current.finalCost + 1);

                /*if (current.j - 1 >= 0) {
                    t = maze[current.i + 1][current.j - 1];
                    updateCost(current, t, current.finalCost + 1);
                }*/

                /*if (current.j + 1 < grid[0].length) {
                    t = maze[current.i + 1][current.j + 1];
                    updateCost(current, t, current.finalCost + 1);
                }*/
            }
        }
    }

    public void display() {
        System.out.println("Maze:");

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if ( i == startX && j == startY)
                    System.out.print("S  "); // Start point
                else if (i == endX && j == endY)
                    System.out.print("G  "); // Goal point
                else if (maze[i][j] != null)
                    System.out.printf("%-3d", 0);
                else
                    System.out.print("X  "); //Blocked cell
            }

            System.out.println();
        }

        System.out.println();
    }

    public void displayScores() {
        System.out.println("\nScores for cells: ");
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] != null)
                    System.out.printf("%-3d", maze[i][j].finalCost);
                else
                    System.out.print("X  ");
            }

            System.out.println();
        }

        System.out.println();
    }

    public void displaySolution() {
        if (closeList[endX][endY]) {
            //We track back the path
            System.out.print("Path:");
            Cell current = maze[endX][endY];
            System.out.print("[" + current.i + " " + current.j + "]");
            maze[current.i][current.j].solution = true;

            while (current.parent != null) {
                System.out.print("->" + "[" + current.parent.i + " " + current.parent.j + "]");
                maze[current.parent.i][current.parent.j].solution = true;
                current = current.parent;
            }

            System.out.println("\n");

            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if ( i == startX && j == startY)
                        System.out.print("S  "); // Start point
                    else if (i == endX && j == endY)
                        System.out.print("G  "); // Goal point
                    else if (maze[i][j] != null)
                        System.out.printf("%-3s", maze[i][j].solution ? "*" : "0");
                    else
                        System.out.print("X  "); //Blocked cell
                }

                System.out.println();
            }

            System.out.println();
        } else
            System.out.println("No possible path");
    }


    public static void main(String[] args) throws IOException {
        int[][] theMaze;
        int goalX, goalY, startX, startY, height, width;

        String filepath = "mazes/maze1.txt";

        //The maze of our game
        Maze maze = new Maze(filepath);
        theMaze = maze.getMaze();

        //The start cell's coordinates
        startX = maze.getStartX();
        startY = maze.getStartY();

        //The goal cell's coordinates
        goalX = maze.getGoalX();
        goalY = maze.getGoalY();

        //The coordinates of the maze
        height = maze.getHeight();
        width = maze.getWidth();

        blocked = new int[width*height][width*height];
        blocked[0][0] = 1;

        int p = 0;
        //Get the blocked cells in the form of an array
        for (int i = 0; i < theMaze.length; i++) {
            for (int j = 0; j < theMaze[i].length; j++)
            {
                if (theMaze[i][j] == 1) {
                    blocked[p][0] = i;
                    blocked[p][1] = j;
                    p++;
                }
            }
        }

        AStar astar = new AStar(width,height,startX,startY,goalX,goalY, blocked);
        astar.display();
        astar.process(); //Apply A* Algorithm
        astar.displayScores(); //Display Scores on grid
        astar.displaySolution(); //Display Solution  Path

    }
}

