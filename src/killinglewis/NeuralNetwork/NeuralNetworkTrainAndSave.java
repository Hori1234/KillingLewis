package killinglewis.NeuralNetwork;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.listener.impl.LogRecordListener;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.datasets.iterator.ReconstructionDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.api.Updater;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.jfree.data.general.Dataset;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.activations.impl.ActivationReLU;
import org.nd4j.linalg.activations.impl.ActivationSoftmax;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.custom.Flatten;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.learning.regularization.Regularization;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.*;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import javax.print.attribute.standard.RequestingUserName;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NeuralNetworkTrainAndSave {

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
    /*Number of passes through the dataset*/
    private int epochs = 15;

    public void go(){
        /*Initializing our random variable*/
        random = new Random(seed);

        /*Getting the images paths for training and testing*/
        File trainingData = new File("NeuralNetwork/mnist/training");
        File testingData = new File("NeuralNetwork/mnist/testing");

        /*Define the File Spilter */
        FileSplit train = new FileSplit(trainingData, NativeImageLoader.ALLOWED_FORMATS,random);
        FileSplit test = new FileSplit(testingData, NativeImageLoader.ALLOWED_FORMATS,random);

        /*Getting the labels as a parent path*/
        ParentPathLabelGenerator labelGenerator = new ParentPathLabelGenerator();

        ImageRecordReader imageRecordReader = new ImageRecordReader(height,width,nrchannels,labelGenerator);
        try {
            imageRecordReader.initialize(train);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //imageRecordReader.setListeners(new LogRecordListener());

        DataSetIterator dataIterator = new RecordReaderDataSetIterator(imageRecordReader,batchSize,1,outputNeurons);

        /*Normalizing our data*/
        DataNormalization normalizer = new ImagePreProcessingScaler(0,1);
        normalizer.fit(dataIterator);
        dataIterator.setPreProcessor(normalizer);

        log.info("=========> Model is building <============");
        MultiLayerNetwork model = new MultiLayerNetwork(getModelConv());
        model.init();
        model.setLearningRate(0.005);
        model.setListeners(new ScoreIterationListener(10));


        log.info("=========> Model is training <============");
        for (int i = 0; i<epochs; i++){
            model.fit(dataIterator);
        }

        log.info("=========> Evaluating <============");
        imageRecordReader.reset();
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

        /*Saving the model*/
        log.info("=========> Saving the model .. <============");
        /*Where to save the model*/
        File location = new File("NeuralNetwork/model/trainedModel.zip");
        /*If we want or not to save the updater.*/
        boolean wantUpdater = false;
        try {
            ModelSerializer.writeModel(model,location,wantUpdater);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private MultiLayerConfiguration modelLayering (){
        MultiLayerConfiguration model = initModel();

        return model;
    }
    private MultiLayerConfiguration initModel(){
        MultiLayerConfiguration model = new NeuralNetConfiguration.Builder()
                .seed(seed).maxNumLineSearchIterations(1)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Nesterovs(0.9))
                .l2(1e-4)
                .list() //For configuring MultiLayerNetwork we call the list method
                .layer(0,new DenseLayer.Builder()
                    .nIn(height*width)
                    .nOut(100)
                    .activation(new ActivationReLU())
                    .weightInit(WeightInit.XAVIER)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .nIn(100)
                    .nOut(outputNeurons)
                    .activation(new ActivationSoftmax())
                    .weightInit(WeightInit.XAVIER)
                    .build())
                .backpropType(BackpropType.Standard)
                .setInputType(InputType.convolutional(height,width,nrchannels))
                .build();

        return model;
    }

    public MultiLayerConfiguration getModelConv() {

        log.info("Building simple convolutional network...");

        Map<Integer, Double> learningRateSchedule = new HashMap<>();
        learningRateSchedule.put(0, 0.06);
        learningRateSchedule.put(200, 0.05);
        learningRateSchedule.put(600, 0.028);
        learningRateSchedule.put(800, 0.0060);
        learningRateSchedule.put(1000, 0.001);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()

                .seed(seed)

                .l2(0.0005) // ridge regression value

                .updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))

                .weightInit(WeightInit.XAVIER)

                .list()

                .layer(new ConvolutionLayer.Builder(5, 5)

                        .nIn(nrchannels)

                        .stride(1, 1)

                        .nOut(20)

                        .activation(Activation.IDENTITY)

                        .build())

                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)

                        .kernelSize(2, 2)

                        .stride(2, 2)

                        .build())

                .layer(new ConvolutionLayer.Builder(5, 5)

                        .stride(1, 1) // nIn need not specified in later layers

                        .nOut(50)

                        .activation(Activation.IDENTITY)

                        .build())

                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)

                        .kernelSize(2, 2)

                        .stride(2, 2)

                        .build())

                .layer(new DenseLayer.Builder().activation(Activation.RELU)

                        .nOut(500)

                        .build())

                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)

                        .nOut(outputNeurons)

                        .activation(Activation.SOFTMAX)

                        .build())

                .setInputType(InputType.convolutionalFlat(height, width, nrchannels)) // InputType.convolutional for normal image

                .build();

        return conf;

    }

    public static void main(String[] args) {
        new NeuralNetworkTrainAndSave().go();
    }

}
