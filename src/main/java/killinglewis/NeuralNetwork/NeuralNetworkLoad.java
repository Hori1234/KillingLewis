package killinglewis.NeuralNetwork;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.impl.ActivationReLU;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NeuralNetworkLoad {
    /*Initializing the logger, for debugging purposes */
    private static Logger log = LoggerFactory.getLogger(NeuralNetworkTrainAndSave.class);

    /*Neural Network's hyperparameters*/
    /*Height of the images on which we'll train the Neural Network*/
    private int height = 28;
    /*Width of the images on which we'll train the Neural Network*/
    private int width = 28;
    /*The number of channel , in our case is 1 bc the photos are black and white*/
    private int nrchannels = 1;
    /*A random upper value for the randomness*/
    private int seed = 128;
    private Random random;
    /*batch Size of the Neural network*/
    private int batchSize = 128;
    /*Output of the Neural Network*/
    private int outputNeurons = 10;

    public void go(){
        /*Initializing our random variable*/
        random = new Random(seed);
        File testingData = new File("NeuralNetwork/mnist/testing");

        /*Define the File Spilter */
        FileSplit test = new FileSplit(testingData, NativeImageLoader.ALLOWED_FORMATS,random);

        /*Getting the labels as a parent path*/
        ParentPathLabelGenerator labelGenerator = new ParentPathLabelGenerator();

        ImageRecordReader imageRecordReader = new ImageRecordReader(height,width,nrchannels,labelGenerator);

        /*Normalizing our data*/
        DataNormalization normalizer = new ImagePreProcessingScaler(0,1);

        /*Saving the model*/
        log.info("=========> Loading the model .. <============");
        /*Where to save the model*/
        File modelPath = new File("NeuralNetwork/model/trainedModel.zip");

        log.info("=========> Model is building <============");
        MultiLayerNetwork model = null;
        try {
            model = ModelSerializer.restoreMultiLayerNetwork(modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.init();
        model.setLearningRate(0.005);
        model.setListeners(new ScoreIterationListener(10));


        log.info("=========> Evaluating <============");

        try {
            imageRecordReader.initialize(test);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataSetIterator testIterator = new RecordReaderDataSetIterator(imageRecordReader, batchSize,1,outputNeurons);
        normalizer.fit(testIterator);
        testIterator.setPreProcessor(normalizer);

        Evaluation evaluation = new Evaluation(outputNeurons);
        while (testIterator.hasNext()){
            DataSet next = testIterator.next();
            INDArray outputArray = model.output(next.getFeatures());

            evaluation.eval(next.getLabels(),outputArray);
        }
        log.info(evaluation.stats());
    }


    public static void main(String[] args) {
        new NeuralNetworkLoad().go();
    }
}
