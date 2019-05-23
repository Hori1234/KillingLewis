package killinglewis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelLoader {

    // Data to extract from model file
    private Vector<Float> vertices = new Vector<>();
    private Vector<Float> normals = new Vector<>();
    private Vector<Float> tCoords = new Vector<>();
    private Vector<Integer> faces = new Vector<>();

    private int[] indices;
    private float[] vertex;
    private float[] texCoords;
    private float[] normalCoords;

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
                    if (line.charAt(0) == 'v' && line.charAt(1) != 't' && line.charAt(1) != 'n') {
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

            convert();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float[] getVertices () {
        return vertex;
    }

    public float[] getTCoords () {
        return texCoords;
    }

    public float[] getNormals() {
        return normalCoords;
    }

    public int[] getFaces () {
        return indices;
    }

    private void convert() {
        HashMap<List<Integer>, Integer> indexMap = new HashMap<>();

        Iterator<Integer> it = faces.iterator();
        int k = 0;

        ArrayList<Integer> ind = new ArrayList<>();
        ArrayList<Float> tex = new ArrayList<>();
        ArrayList<Float> norm = new ArrayList<>();
        ArrayList<Float> vert = new ArrayList<>();

        while (it.hasNext()) {
            ArrayList<Integer> temp = new ArrayList<>();

            temp.add(it.next());
            temp.add(it.next());
            temp.add(it.next());

            //System.out.println("(" + temp.get(0) + " " + temp.get(1) + " " + temp.get(2) + ")");

            if (indexMap.containsKey(temp)) {
                ind.add(indexMap.get(temp));
            } else {
                indexMap.put(temp, k);
                ind.add(k);
                vert.add(vertices.get((temp.get(0) - 1) * 3));
                vert.add(vertices.get((temp.get(0) - 1) * 3 + 1));
                vert.add(vertices.get((temp.get(0) - 1) * 3 + 2));
                tex.add(tCoords.get((temp.get(1) - 1) * 2));
                tex.add(tCoords.get((temp.get(1) - 1) * 2 + 1));
                norm.add(normals.get((temp.get(2) - 1) * 3));
                norm.add(normals.get((temp.get(2) - 1) * 3 + 1));
                norm.add(normals.get((temp.get(2) - 1) * 3 + 2));

                k++;
            }

            //System.out.println("k: " + ind.get(ind.size() - 1) + " (" + vertices.get((temp.get(0) - 1) * 3) + ", " + vertices.get((temp.get(0) - 1) * 3 + 1) + ", " + tCoords.get((temp.get(1) - 1) * 2) + ", " + tCoords.get((temp.get(1) - 1) * 2 + 1) + ", " + normals.get((temp.get(2) - 1) * 3) + ")");
        }

        indices = new int[ind.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = ind.get(i);
        }

        vertex = new float[vert.size()];
        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = vert.get(i);
        }

        texCoords = new float[tex.size()];
        for (int i = 0; i < texCoords.length; i++) {
            texCoords[i] = tex.get(i);
        }

        normalCoords = new float[norm.size()];
        for (int i = 0; i < normalCoords.length; i++) {
            normalCoords[i] = norm.get(i);
        }

    }
}
