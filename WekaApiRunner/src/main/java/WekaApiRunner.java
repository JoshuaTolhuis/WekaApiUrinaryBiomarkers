import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class WekaApiRunner {
    private final String modelFile = "src/main/resources/Logistics.model";

    public static void main(String[] args) {
        WekaApiRunner runner = new WekaApiRunner();
        runner.start();
    }

    private void start() {
        String data = "weka_data.arff";
        String unknownFile = "weka_data.arff";

        try {
            Instances instances = loadArff(data);
            printInstances(instances);
            Logistic log = buildClassifier(instances);
            Logistic classifier = loadClassifier();
            Instances classification = classifyNewInstance(classifier, instances);
            writeToFile(classification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Instances classifyNewInstance(Logistic log, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = log.classifyInstance(unknownInstances.instance(i));
            System.out.println(labeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
        return labeled;
    }


    private void printInstances(Instances instances) {
        int numAttributes = instances.numAttributes();

        for (int i = 0; i < numAttributes; i++) {
            System.out.println("attribute " + i + " = " + instances.attribute(i));
        }

        System.out.println("class index = " + instances.classIndex());

        int numInstances = instances.numInstances();
        for (int i = 0; i < numInstances; i++) {
            if (i == 5) break;
            Instance instance = instances.instance(i);
            System.out.println("instance = " + instance);
        }
    }

    private Logistic loadClassifier() throws Exception {
        // deserialize model
        return (Logistic) weka.core.SerializationHelper.read(modelFile);
    }

    private Logistic buildClassifier(Instances instances) throws Exception {
        String[] options = new String[1];
        options[0] = "-R 1.0E-8";
        Logistic log = new Logistic();
        printInstances(instances);
        return log;
    }


    private Instances loadArff(String dataset) throws IOException {
        try {
            DataSource source = new DataSource(dataset);
            Instances data = source.getDataSet();
            // setting class attribute if the data format does not provide this information
            // For example, the XRFF format saves the class attribute information as well
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            throw new IOException("could not read from file");
        }
    }


    private void writeToFile(Instances classification) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("classification.csv", "UTF-8");
        writer.println(classification);
        writer.close();
    }
}
