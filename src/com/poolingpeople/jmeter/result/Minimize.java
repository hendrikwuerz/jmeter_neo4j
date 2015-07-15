package com.poolingpeople.jmeter.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Created by hendrik on 13.07.15.
 */
public class Minimize {

    int groupSize;
    String destinationFolder;

    LinkedList<Line> pendingLines;
    LinkedList<Line> minimizedLines; // the minimized results in sorted order

    public static LinkedList<Line> minimize(File sourceFile, String destinationFolder, int requests, int maxSize) {
        Minimize m = new Minimize(destinationFolder, requests, maxSize);

        // read and process each line
        try (Stream<String> lines = Files.lines(sourceFile.toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered( m::process );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // sort list and export
        m.finishMinimizing();

        return m.minimizedLines;
    }

    /**
     * Minimizes multiple lines to one line
     * @param destinationFolder
     *      The folder to write the merged lines
     * @param requests
     *      statistic data: How many requests are available for one label
     * @param maxSize
     *      ca amount of result data
     */
    public Minimize(String destinationFolder, int requests, int maxSize) {
        // calc how many requests have to be grouped for each label so maxSize is never exceeded
        this(destinationFolder, Math.round((float) requests / maxSize));
    }

    /**
     * Minimizes multiple lines to one line
     * @param destinationFolder
     *      The folder to write the merged lines
     * @param groupSize
     *      How many lines will be merged to one line
     */
    public Minimize(String destinationFolder, int groupSize) {
        this(destinationFolder);
        this.groupSize = groupSize;
    }

    private Minimize(String destinationFolder) {
        this.destinationFolder = destinationFolder;
        this.pendingLines = new LinkedList<>();
        this.minimizedLines = new LinkedList<>();
    }

    /**
     * get a new line to be merged with others with the same label
     * @param line
     *      The line to be merged
     */
    public void process(String line) {
        process(new Line(line));
    }


    /**
     * get a new line to be merged with others with the same label
     * @param line
     *      The line to be merged
     */
    public void process(Line line) {

        pendingLines.add(line);

        // collected all data for grouping
        if(pendingLines.size() >= groupSize) {
            Line mergedLine = Line.merge(pendingLines);
            pendingLines.clear();
            minimizedLines.add(mergedLine);
        }

    }

    /**
     * print all data in pending lines to the files, also if they have not reached their group size
     */
    public void finishMinimizing() {

        if(pendingLines.size() > 0) {
            minimizedLines.add(Line.merge(pendingLines));
            pendingLines.clear();
        }

        minimizedLines.sort( (l1, l2) -> l1.compareTo(l2));

        minimizedLines.stream().forEach( line -> Util.exportLine(line, destinationFolder) );
    }
}
