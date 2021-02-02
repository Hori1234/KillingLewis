package killinglewis.NeuralNetwork;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WindImagePipelineNeuralNetwork {
    /*Initializing the logger, for debugging purposes */
    private static Logger log = LoggerFactory.getLogger(WindImagePipelineNeuralNetwork.class);

    /*Height of the images on which we'll train the Neural Network*/
    private int height = 28;
    /*Width of the images on which we'll train the Neural Network*/
    private int width = 28;
    /*The number of channel , in our case is 1 bc the photos are black and white*/
    private int nrchannels = 1;
    /*A random upper value for the randomness*/
    private int seed = 128;
    private Random random;
    /*Labels fo the digits as a list*/
    private List<Integer> labels;
    /*Image as a IND array*/
    private INDArray image;
    private volatile INDArray output;
    private String imagePath;
    public WindImagePipelineNeuralNetwork(String path){
        this.imagePath = path;
    }
    public void go(){
        /*List of labels*/
        //List<Integer> labels= Arrays.asList(2,3,7,1,6,4,0,5,8,9);
        labels = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        /*File location*/
        File imageLocation = new File(imagePath);/*Where to save the model*/
        File modelPath = new File("NeuralNetwork/model/trainedModel.zip");

        MultiLayerNetwork model = null;
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("===========================> Test Image <==================================");
        NativeImageLoader nativeImageLoader = new NativeImageLoader(height,width,nrchannels);

        /*Get the image*/
        try {
            image = nativeImageLoader.asMatrix(imageLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Normalize the input*/
        DataNormalization normalization = new ImagePreProcessingScaler(0,1);
        normalization.transform(image);

        /*Forward the image to the Neural Network*/
        output = model.output(image);
        log.info("File chosen: " + imageLocation);
        log.info("Neural network Evaluation");
        log.info("List of labels in order");
        log.info(output.toString());
        log.info(labels.toString());
        log.info(output.shapeInfoToString());
        Float[] tmp = getValuesOfOutput();
    }

    public Float[] getValuesOfOutput(){
        Float[] values = new Float[10];
        INDArray tmp;
        tmp= output.get(NDArrayIndex.point(0), NDArrayIndex.all());

        for (int i=0; i < tmp.columns(); i++){
            values[i] = tmp.getFloat(i);
        }

        return values;
    }

    public List<Integer> getLabels(){
        return labels;
    }

    public static void main(String[] args) {
        new WindImagePipelineNeuralNetwork("NeuralNetwork/mnist/testing/7/0.png").go();
    }
}
