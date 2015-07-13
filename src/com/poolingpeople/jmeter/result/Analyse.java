package com.poolingpeople.jmeter.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by hendrik on 08.07.15.
 */
public class Analyse {

    static String filename = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/results_tree_success_copy_long_run.csv";

    static String minimizedFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/";

    static HashMap<String, Label> labels;

    public static void main(String[] args) {

        labels = new HashMap<>();

        // read and process each line
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(Analyse::process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Minimize the input data to the passed amount of CSV lines
        Minimize min = new Minimize(minimizedFolder, labels, 1000);
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(min::process);
        } catch (IOException e) {
            e.printStackTrace();
        }
        min.exportAll();

        // Print results
        labels.forEach( (key, value) -> System.out.println(value) );



    }

    /**
     * processes the passed line and evaluate the values for the wished results
     * @param lineString
     *      The line to be processed as a String
     */
    public static void process(String lineString) {

        Line line = new Line(lineString);
        Label label;
        if(labels.containsKey(line.label)) {
            label = labels.get(line.label);
        } else {
            label = new Label(line.label);
            labels.put(line.label, label);
        }

        label.process(line);
    }

}
