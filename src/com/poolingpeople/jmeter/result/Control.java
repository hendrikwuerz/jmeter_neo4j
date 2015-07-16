package com.poolingpeople.jmeter.result;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by hendrik on 13.07.15.
 */
public class Control {

    static String filename = "D:\\_Homepage\\arbeit\\jmeter_neo4j\\jmeter\\logs\\logfile.csv";

    static String splitFolder = "D:\\_Homepage\\arbeit\\jmeter_neo4j\\jmeter\\logs\\split\\";

    static String minimizedFolder = "D:\\_Homepage\\arbeit\\jmeter_neo4j\\jmeter\\logs\\min\\";

    public static void main(String[] args) {

        // clean destination folder
        Util.cleanFolder(splitFolder);
        Util.cleanFolder(minimizedFolder);

        System.out.printf("Start splitting files");
        long start = System.currentTimeMillis();
        Split.split(filename, splitFolder);
        System.out.println("Needed " + (System.currentTimeMillis() - start) + "ms for splitting");

        File folder = new File(splitFolder);
        Arrays.stream(folder.listFiles()).forEach(file -> { // Handle each label

            LinkedList<Line> sortedLines = Minimize.minimize(file, minimizedFolder, Analyse.countLines(file), 5000);

            Analyse analyse = new Analyse(sortedLines);
            System.out.println(analyse.toString() + System.lineSeparator());

            new Diagram(sortedLines, minimizedFolder);

        });



    }

}
