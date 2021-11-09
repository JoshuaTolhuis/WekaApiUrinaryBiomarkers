import org.apache.commons.cli.*;

import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;


/**
 * Class with Apache CLI configured. This class will use a file given by a user and
 * classify the stage of cancer according to a machine learning algorithm "Logistic"
 *
 * @author Joshua Tolhuis
 */

public class WekaApiRunner {
    String[] arguments;
    private static final String HELP = "help";
    private static final String FILE = "file-name";

    private Options options;
    /**
     * Main function passing command line arguments to the start function
     *
     * @param args: The command line arguments
     */

    public static void main(String[] args) {
        WekaApiRunner wekaApiRunner = new WekaApiRunner();
        wekaApiRunner.start(args);
    }
    public void start(String[] args){
        this.arguments = args;
        buildOptions();
        String filename = parseArguments();

        try {
            // Modify files using an R script
            ModifyFile modifyFile = new ModifyFile();
            modifyFile.modifyData(filename);
            // Load instances
            Instances instances = loadData();
            // Load the classifier
            Logistic classifier = loadClassifier();
            // Classify the instances with the model
            Instances classification = classifyNewInstance(classifier, instances);
            // Write the classified instances to a .csv file
            writeToFile(classification);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configuring the Apache CLI options for the command line.
     */
    public void buildOptions() {
        this.options = new Options();

        // Creating the arguments possible

        Option help = new Option("h", "help", false, "Help function" );
        Option fileName = new Option("f", "file-name", true, "The file name to classify from." );

        options.addOption(help);
        options.addOption(fileName);
    }

    /**
     * Function to parse the command line arguments given by the user.
     *
     * @return filename/null: If the user gave a filename it will return the filename, if not null
     *                        will be returned and an error will be thrown.
     */
    public String parseArguments() {
        // Parsing input from the command line
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();

        try {
            CommandLine command = parser.parse(this.options, this.arguments);
            // Check if the user entered the HELP argument
            if (command.hasOption(HELP)){
                helpFormatter.printHelp("Pancreatic Cancer Predictor", this.options, true);
                return null;
            } else if (command.hasOption(FILE)){
                String fileName = command.getOptionValue(FILE);
                return fileName;
            }
        } catch (ParseException e) {
            // Printing the help when something goes wrong
            helpFormatter.printHelp("Pancreatic Cancer Predictor", this.options, true);
        }
        return null;
    }
    /**
     * Method to classify new instances using the abstract classifier.
     *
     * @param log: The loaded weka clasifier
     * @param instances: The instances from the file the user entered
     */
    private static Instances classifyNewInstance(Logistic log, Instances instances) throws Exception {
        // create copy
        Instances labeled = new Instances(instances);
        // label instances
        for (int i = 0; i < instances.numInstances(); i++) {
            double clsLabel = log.classifyInstance(instances.instance(i));
            System.out.println(labeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
        }
        System.out.println("\nNew, labeled = \n" + labeled);
        return labeled;
    }

    /**
     * Method to load the Logistics classifier .model file.
     *
     * @return Logistic object made from the .model Logistics file.
     */
    private static Logistic loadClassifier() throws Exception {
        // Load in the Logistics model
        String modelFile = "Logistics.model";
        return (Logistic) weka.core.SerializationHelper.read(modelFile);
    }

    /**
     * Method that loads the unknown data
     *
     * @return the data file
     */
    private Instances loadData() throws IOException {
        try {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource("unknowndata/data.arff");
            Instances data = source.getDataSet();
            if (data.classIndex() == -1)
                data.setClassIndex(data.numAttributes() - 1);
            return data;
        } catch (Exception e) {
            throw new IOException("Something went wrong while reading file. Please check if the" +
                    "file is the right format.");
        }
    }


    /**
     * Function to write the new classification to a file
     * @param classification: The classification
     */
    private static void writeToFile(Instances classification) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("classification.csv", "UTF-8");
        writer.println(classification);
        writer.close();
    }
}
