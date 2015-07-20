package com.poolingpeople.jmeter.result;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by hendrik on 13.07.15.
 */
public class Control {

    static String filename = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/results_tree_success_copy_long_run.csv";

    static String splitFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/split/";

    static String minimizedFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/";

    public static void main(String[] args) {

        // clean destination folder
        Util.cleanFolder(splitFolder);
        Util.cleanFolder(minimizedFolder);

        System.out.printf("Start splitting files");
        Split.split(filename, splitFolder);

        File folder = new File(splitFolder);
        Arrays.stream(folder.listFiles()).forEach(file -> { // Handle each label

            int originalAmountRequests = Analyse.countLines(file);

            LinkedList<Line> sortedLines = Minimize.minimize(file, minimizedFolder, originalAmountRequests, 5000);

            Analyse analyse = new Analyse(sortedLines, originalAmountRequests);
            System.out.println(analyse.toString() + System.lineSeparator());

            new Diagram(sortedLines, minimizedFolder, analyse);

        });



    }

}
