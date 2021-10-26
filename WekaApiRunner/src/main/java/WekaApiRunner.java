import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.IOException;

public class WekaApiRunner {
    private final String modelFile = "No_Stage.model";

    public static void main(String[] args){
       WekaApiRunner runner = new WekaApiRunner();
       runner.start();
    }

    private void start() {
        String data = "weka_data.arff";
        String unknownFile = "unknown_weka_data.arff";

        try {
            Instances instances = loadArff(data);
            Logistic log = buildClassifier(instances);
            saveClassifier(log);
            Logistic fromFile = loadClassifier();
            Instances unknownInstances = loadArff(unknownFile);
            System.out.println("\nunclassified unknownInstances = \n" + unknownInstances);
            classifyNewInstance(fromFile, unknownInstances);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void classifyNewInstance(Logistic log, Instances unknownInstances) throws Exception {
        // create copy
        Instances labeled = new Instances(unknownInstances);
        // label instances
        for (int i = 0; i < unknownInstances.numInstances(); i++) {
            double clsLabel = log.classifyInstance(unknownInstances.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
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

    private void saveClassifier(Logistic log) throws Exception {
        //post 3.5.5
        // serialize model
        weka.core.SerializationHelper.write(modelFile, log);

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
}
