package org.example;
/*****************************************************************************************
 * DL4J Example: version 230219
 *****************************************************************************************/
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;

import static java.lang.System.out;
public class LearningDL4J extends JComponent
{
    private static int FEATURES_COUNT = 4;
    private static int CLASSES_COUNT = 3;
    private static String version = "230130";
    private static Object eval;
    private static int i;
    private static double accuracy;
    private ArrayList<Dimension> graphPointsList = new ArrayList<>();
    private static Dimension2D oldPoint = new Dimension(0, 0);
    private static Dimension newPoint = new Dimension(0, 0);
    private DataSet trainingData;
    private DataSet testingData;
    void loadData()
    {
        try (RecordReader recordReader = new CSVRecordReader(0, ','))
        {
            recordReader.initialize(new FileSplit(new ClassPathResource("iris.csv").getFile()));
            DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 150, 4, 3);
            DataSet allData = iterator.next();
            allData.shuffle(123);
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(allData);
            normalizer.transform(allData);
            SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.65);
            trainingData = testAndTrain.getTrain();
            testingData = testAndTrain.getTest();
            irisNNetwork(trainingData, testingData);
        } catch (Exception e)
        {
            out.println("Error: " + e.getLocalizedMessage());
        }
    }
    private void irisNNetwork(DataSet trainingData, DataSet testData)//Using Point(width, height) for Point(x, y) plotting point
    {
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(0.1, 0.9))
                .l2(0.0001)
                .list()
                .layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(3).build())
                .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
                .layer(2, new OutputLayer.Builder(
                        LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX)
                        .nIn(3).nOut(CLASSES_COUNT).build())
                .backprop(true).pretrain(false)
                .build();
        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();
        Evaluation eval = null;
        for (i = 0; i < 1000; i++) {
            INDArray output = model.output(testData.getFeatureMatrix());
            eval = new Evaluation(3);
            model.fit(trainingData);
            eval.eval(testData.getLabels(), output);
            accuracy = eval.accuracy();
            accuracy = 1 - accuracy;
            newPoint = new Dimension(i, (int) (accuracy * 1000));
            NFL.setEpoch((int) newPoint.getWidth());
            NFL.setAccuracy(newPoint.getHeight());
            out.print("\nAccuracy " + accuracy + " " + newPoint.getWidth() + " " + newPoint.getHeight());
        }
        out.printf(eval.stats());
    }
    public static double getEpoch()
    {
        return i;
    }
    public static double getAccuracy()
    {
        return accuracy;
    }
}

