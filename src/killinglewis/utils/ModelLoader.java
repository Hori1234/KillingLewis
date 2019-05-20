package killinglewis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelLoader {

    // Data to extract from model file
    private Vector<Float> vertices = new Vector<>();
    private Vector<Float> normals = new Vector<>();
    private Vector<Float> tCoords = new Vector<>();
    private Vector<Integer> faces = new Vector<>();

    public void loadModel (String filePath) {
        // Clear containers before proceeding with the load of data
        vertices.clear();
        normals.clear();
        tCoords.clear();
        faces.clear();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            Pattern p = Pattern.compile("[-+]?\\d*\\.\\d+|\\d+"); // regex for float in text
            Pattern i = Pattern.compile("([\\+-]?\\d+)([eE][\\+-]?\\d+)?"); // regex for int in text

            while (line != null) {
                if (line.length() >= 1) {
                    // Line is containing information about vertex
                    if (line.charAt(0) == 'v' && line.charAt(1) != 't') {
                        Matcher m = p.matcher(line);
                        while (m.find()) {
                            vertices.add(Float.parseFloat(m.group()));
                        }
                    }
                    // normals
                    else if (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
                        Matcher m = p.matcher(line);
                        while (m.find()) {
                            normals.add(Float.parseFloat(m.group()));
                        }
                    }
                    // Line is containing information texture coordinate
                    else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
                        Matcher m = p.matcher(line);
                        while (m.find()) {
                            tCoords.add(Float.parseFloat(m.group()));
                        }
                    }
                    // Line is containing information about faces
                    else if (line.charAt(0) == 'f') {
                        Matcher m = i.matcher(line);
                        while (m.find()) {
                            faces.add(Integer.parseInt(m.group()));
                        }
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] getVertices () {
        float[] result = null;
        try {
            if (vertices.size() == 0) {
                throw new Exception("No model loaded.");
            } else {
                result = new float[vertices.size()];
                int k = 0;
                Iterator<Float> it = vertices.iterator();

                while (it.hasNext()) {
                    result[k++] = it.next() / 100.0f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public float[] getTCoords () {
        float[] result = null;
        try {
            if (tCoords.size() == 0) {
                throw new Exception("No model loaded.");
            } else {
                result = new float[tCoords.size()];
                int k = 0;
                Iterator<Float> it = tCoords.iterator();

                while (it.hasNext()) {
                    result[k++] = it.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public float[] getNormals() {
        float[] result = null;
        try {
            if (normals.size() == 0) {
                throw new Exception("No model loaded.");
            } else {
                result = new float[normals.size()];
                int k = 0;
                Iterator<Float> it = normals.iterator();

                while (it.hasNext()) {
                    result[k++] = it.next() / 100.0f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int[] getFaces () {
        int[] result = null;
        try {
            if (faces.size() == 0) {
                throw new Exception("No model loaded.");
            } else {
                result = new int[faces.size()];
                int k = 0;
                Iterator<Integer> it = faces.iterator();

                while (it.hasNext()) {
                    result[k++] = it.next() - 1;
                    it.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
