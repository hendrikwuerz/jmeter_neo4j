package com.poolingpeople.jmeter.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Created by hendrik on 08.07.15.
 */
public class Analyse {

    static String filename = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/results_tree_success_copy_long_run.csv";

    static String minimizedFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/";

    static String[] labels; // all labels in the csv file

    static HashMap<String, Integer> requests; // hash map to get index in other arrays for a label
    static int[] amountRequests; // amount of request send with a label
    static int[] sumResponseTime; // the sum of all response times of the requests with a label

    public static void main(String[] args) {

        requests = getLabels();
        amountRequests = new int[requests.size()];
        sumResponseTime = new int[requests.size()];

        // read and process each line
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(Analyse::process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Minimize the input data to the passed amount of CSV lines
        Minimize min = new Minimize(minimizedFolder, 2);
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(min::process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print results
        for(int i = 0; i < labels.length; i++) {
            System.out.println(labels[i] + ": " + amountRequests[i] + " Requests");
            System.out.println("Durchschnittlich: " + (sumResponseTime[i] / amountRequests[i]) + " Millisekunden");
            System.out.println();
        }

    }

    /**
     * Creates a HashMap with all all labels and their index in the other arrays
     * Fills the "labels" instance variable
     * @return
     *      The HashMap with the labels and their index
     */
    public static HashMap<String, Integer> getLabels() {
        // read each line
        LinkedList<String> labels = new LinkedList<>();
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(line -> checkAndInsertNewLabel(line, labels));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create HashMap and Array
        Analyse.labels = new String[labels.size()];
        HashMap<String, Integer> requests = new HashMap<>();
        for(int i = 0; i < labels.size(); i++) {
            requests.put(labels.get(i), i);
            Analyse.labels[i] = labels.get(i);
        }
        return requests;
    }

    /**
     * checks the passed line and add the label if it is not already known
     * @param line
     *      The line to be checked
     * @param labels
     *      All already knows labels
     * @return
     *      The new list with labels. Maybe a new lables was added
     */
    private static LinkedList<String> checkAndInsertNewLabel(String line, LinkedList<String> labels) {
        String[] values = line.split(",", -1);
        if(!labels.contains(values[2])) { // new label
            labels.add(values[2]);
        }
        return labels;
    }

    /**
     * processes the passed line and evaluate the values for the wished results
     * @param line
     *      The line to be processed
     */
    public static void process(String line) {
        String[] values = line.split(",", -1);

        int type = requests.getOrDefault(values[2], -1);
        if(type < 0) {
            System.out.println("MIST " + values[2] + " not found");
            return;
        }

        try {
            amountRequests[type]++;
            sumResponseTime[type] += Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            // Ignore everything that is not a number
        }
    }

}
