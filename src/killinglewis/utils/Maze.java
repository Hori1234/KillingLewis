import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ralucaviziteu 
 */

public class Maze {
    private static int [][] theMaze; // the maze matrix filled with integers
    private static char [][] inputMaze; // the maze matrix filled with characters
    private File f; // a file f
    private BufferedReader br = null;
    //Lewis's start coordinates:
    private static int startX = 0;
    private static int startY = 0;
    //Lewis's goal coordinates:
    private int goalX = 0;
    private int goalY = 0;
    //The maze's coordinates:
    private int height = 0;
    private int width = 0;
    //The path to the file:
    private String filepath;

    /**
     * Constructor of class Maze
     * @param filepath
     */
    public Maze(String filepath){
        this.filepath = filepath;
    }

    /**
     * Main method - just for testing
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Maze ms = new Maze("/Users/ralucaviziteu/Desktop/Maze.txt");
        ms.buildMaze("/Users/ralucaviziteu/Desktop/Maze.txt");
        ms.convert(inputMaze, theMaze);
    }

    /**
     * Method to be called in order to get the integer matrix filled with the
     * maze's characteristics
     * @return
     * @throws IOException
     */
    public int[][] getMaze() throws IOException{
        buildMaze(filepath);
        convert(inputMaze, theMaze);
        return theMaze;
    }

    /**
     * Method which builds a char matrix from a file
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void buildMaze(String file) throws FileNotFoundException, IOException {
        try {
            f = new File(file);

            br = new BufferedReader(new FileReader(file));

            //First line of the file contains the width and height of the maze
            String dimensions = br.readLine();
            int width = Integer.parseInt(dimensions.substring(0, dimensions.indexOf(' ')));
            int height = Integer.parseInt((dimensions.substring(dimensions.indexOf(' ') + 1)));
            inputMaze = new char[height][width];

            //Second line of the file contains the start coordinates of Lewis in the maze
            String start = br.readLine();
            char temp;
            temp = start.charAt(0);
            startY = Character.getNumericValue(temp);
            temp = start.charAt(2);
            startX = Character.getNumericValue(temp);

            //Third line of the file contains the coordinates of Lewis's goal in the maze
            String goal = br.readLine();
            goalY = Integer.parseInt(goal.substring(0, goal.indexOf(' ')));
            goalX = Integer.parseInt((goal.substring(goal.indexOf(' ') + 1)));

            //The rest of the file contains the maze's characteristics
            int heightCounter = -1;
            String line;
            while ((line = br.readLine()) != null) {
                int column = 0;
                heightCounter++;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) != ' ') {
                        inputMaze[heightCounter][column] = line.charAt(i);
                        column++;
                    }
                }
            }
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("The file:" + f.getName() + "does not exist");
            fnfe.printStackTrace();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Method which converts a char matrix into a integer matrix
     * @param mat
     * @param intMat
     * @return
     */
    private int[][] convert(char mat [][], int intMat [][]) {
        intMat = new int [mat.length][mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++){
                intMat[i][j] = Character.getNumericValue(mat[i][j]);
            }
        }
        return intMat;
    }

}
