package killinglewis.ModelLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class NNLoader extends Thread {

    public volatile static boolean processing;
    private static String modelPath;
    public volatile static String[] result;
    private String[] lineArray;
    public NNLoader(String path, boolean bool){
        this.modelPath = path;
        this.processing = bool;
    }

    private void loadModelPython() throws InterruptedException, IOException {
        String command = "cmd /c python " + modelPath;
        //String command = "NeuralNetwork/NeuralNetTest/NeuralNetTest.exe";
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        while ((line = bri.readLine()) != null) {
            System.out.println(line);
            if (line.contains("[[")){
                String linecpy = line;
                lineArray = linecpy.split("");

            }
        }
        bri.close();
        result = new String[lineArray.length/3 - 1];
        int j = 0;
        for (int i = 0; i<lineArray.length; i++){
            if (lineArray[i].equals("0") || lineArray[i].equals("1")){
                result[j] = lineArray[i];
                j++;
            }
        }

        bre.close();
        p.waitFor();
        System.out.println("Done.");

        p.destroy();
        NNLoader.processing = false;
    }

    public static void loadModelJava(){

    }

    @Override
    public void run() {

        while (true) {
            if (NNLoader.processing) {
                System.out.println("lalala");
                try {
                    loadModelPython();

                } catch (InterruptedException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
