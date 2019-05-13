package killinglewis.utils;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Maze {
    private int [][] theMaze;
    private char [][] inputMaze;
    private File f;
    private BufferedReader br = null;
    private static int startX = 0;
    private static int startY = 0;
    private int goalX = 0;
    private int goalY = 0;
    private int height = 0;
    private int width = 0;

    private void buildMaze(String file) throws FileNotFoundException, IOException {
        try {
            f = new File(file);

            br = new BufferedReader(new FileReader(file));

            String dimensions = br.readLine();
            int width = Integer.parseInt(dimensions.substring(0, dimensions.indexOf(' ')));
            int height = Integer.parseInt((dimensions.substring(dimensions.indexOf(' ') + 1)));
            inputMaze = new char[height][width];

            String start = br.readLine();
            char temp;
            temp = start.charAt(0);
            startY = Character.getNumericValue(temp);
            temp = start.charAt(2);
            startX = Character.getNumericValue(temp);

            String goal = br.readLine();
            goalY = Integer.parseInt(goal.substring(0, goal.indexOf(' ')));
            goalX = Integer.parseInt((goal.substring(goal.indexOf(' ') + 1)));

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

    public static void print(char mat[][]) throws IOException {
        Maze m = new Maze();
        m.buildMaze("");
        for (int i = 0; i < mat.length; i++)
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
    }


}
