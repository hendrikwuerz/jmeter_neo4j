package com.poolingpeople.jmeter.result;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by hendrik on 13.07.15.
 */
public class Control {

    public static void main(String[] args) {
        String filename = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/results_tree_success_copy_long_run.csv";
        String splitFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/split/";
        String minimizedFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/";

        Control control = new Control(filename, splitFolder, minimizedFolder);
    }

    /**
     * starts the result analyse of the generated logfile
     * @param filename
     *          The logfile with the test results
     * @param splitFolder
     *          A folder where the files separated by their label can be stored
     * @param minimizedFolder
     *          A folder where the final data will be stored. This means the minimized files and the svg diagrams
     */
    public Control(String filename, String splitFolder, String minimizedFolder) {
        this(filename, splitFolder, minimizedFolder, 5000);
    }

    /**
     * starts the result analyse of the generated logfile
     * @param filename
     *          The logfile with the test results
     * @param splitFolder
     *          A folder where the files separated by their label can be stored
     * @param minimizedFolder
     *          A folder where the final data will be stored. This means the minimized files and the svg diagrams
     * @param minimizationLevel
     *          How many results this machine can handel. This is only an average value.
     *          If no division of the input date is possible, the used number can be higher
     */
    public Control(String filename, String splitFolder, String minimizedFolder, int minimizationLevel) {

        // clean destination folder
        Util.cleanFolder(splitFolder);
        Util.cleanFolder(minimizedFolder);

        System.out.printf("Start splitting files");
        Split.split(filename, splitFolder);

        File folder = new File(splitFolder);
        Arrays.stream(folder.listFiles()).forEach(file -> { // Handle each label

            int originalAmountRequests = Analyse.countLines(file);

            LinkedList<Line> sortedLines = Minimize.minimize(file, minimizedFolder, originalAmountRequests, minimizationLevel);

            Analyse analyse = new Analyse(sortedLines, originalAmountRequests);
            System.out.println(analyse.toString() + System.lineSeparator());

            new Diagram(sortedLines, minimizedFolder, analyse);

        });

    }
}
