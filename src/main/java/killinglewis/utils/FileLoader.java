package killinglewis.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {

    /**
     * Load file into String.
     *
     * @param filePath path of file to be loaded
     * @return file converted to string format
     */
    public static String load(String filePath) {
        String fileString = "";

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while (line != null) {
                fileString += line + "\n";
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileString;
    }
}
