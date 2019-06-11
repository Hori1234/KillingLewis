package killinglewis.ModelLoader;

import killinglewis.NeuralNetwork.WindImagePipelineNeuralNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NNLoader extends Thread {

    /*For the Python Implementation of the NN*/
    public volatile static boolean processing;
    private static String modelPath;
    public volatile static String[] result;
    private String[] lineArray;

    /*For the java implementation of the NN*/
    public volatile static int resultedLabel;

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

    private void loadModelJava(){
        WindImagePipelineNeuralNetwork NNTestImage = new WindImagePipelineNeuralNetwork("output.jpg");
        NNTestImage.go();

        Float[] evaluations = NNTestImage.getValuesOfOutput();
        List<Integer> labels = NNTestImage.getLabels();
        Float maxEval = 0.0f;
        int maxEvalIndex = 0;
        for (int i=0; i < evaluations.length; i++){
            if (evaluations[i] > maxEval){
                maxEval = evaluations[i];
                maxEvalIndex = i;
            }
        }
        resultedLabel = labels.get(maxEvalIndex);
        NNLoader.processing = false;
    }

    @Override
    public void run() {

        while (true) {
            if (NNLoader.processing) {
                System.out.println("lalala");
                //loadModelPython();
                loadModelJava();
            }
        }
    }


}
