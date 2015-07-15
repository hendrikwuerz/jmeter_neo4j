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

        Split.split(filename, splitFolder);

        File folder = new File(splitFolder);
        Arrays.stream(folder.listFiles()).forEach(file -> { // Handle each label

            Analyse fullAnalyse = new Analyse(file, false); // do not remember all data, only get statistics

            LinkedList<Line> sortedLines = Minimize.minimize(file, minimizedFolder, fullAnalyse, 1000);

            new Diagram(sortedLines, minimizedFolder);

        });



    }

}
