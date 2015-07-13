package com.poolingpeople.jmeter.result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by hendrik on 13.07.15.
 */
public class Minimize {

    HashMap<String, LinkedList<Line>> pendingLines;
    int groupSizeDefault = 2; // Will be used if "groupSize" does not contain a value for a label
    HashMap<String, Integer> groupSize;
    String destinationFolder;

    HashMap<String, LinkedList<Line>> sortedElements; // the minimized results in sorted order

    /**
     * Minimizes multiple lines to one line
     * @param destinationFolder
     *      The folder to write the merged lines
     * @param analyse
     *      statistic data: How many requests are available for one label
     * @param maxSize
     *      ca amount of result data
     */
    public Minimize(String destinationFolder, Analyse analyse, int maxSize) {
        this(destinationFolder);
        // calc how many requests have to be grouped for each label so maxSize is never exceeded
        analyse.labels.forEach( (key, value) -> groupSize.put(key, Math.round((float)value.requests / maxSize)) );
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
        this.groupSizeDefault = groupSize;
    }

    private Minimize(String destinationFolder) {
        // clean destination folder
        cleanFolder(destinationFolder);

        this.destinationFolder = destinationFolder;
        this.pendingLines = new HashMap<>();
        this.groupSize = new HashMap<>();
    }


    private void cleanFolder(String destinationFolder) {
        File folder = new File(destinationFolder);
        if(!folder.exists()) folder.mkdir();
        final File[] files = folder.listFiles();
        Arrays.stream(files).forEach(file -> file.delete());
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
        // get pending line for this label
        if(pendingLines.containsKey(line.label)) { // existing pending line for this label
            LinkedList<Line> list = pendingLines.get(line.label);
            list.add(line);
            if(groupSize.getOrDefault(line.label, groupSizeDefault) <= list.size()) { // merge finished -> sortedElements
                Line mergedLine = Line.merge(list);
                copyToSortedElements(mergedLine);
                pendingLines.remove(line.label);
            }
        } else { // create a new pending line for this label with the passed line
            LinkedList<Line> list = new LinkedList<>();
            list.add(line);
            pendingLines.put(line.label, list);
        }
    }

    private void copyToSortedElements(Line line) {
        // add to final hash map
        if(sortedElements.containsKey(line.label)) {
            sortedElements.get(line.label).push(line);
        } else {
            LinkedList<Line> l = new LinkedList<>();
            l.push(line);
            sortedElements.put(line.label, l);
        }
    }

    /**
     * print all data in pending lines to the files, also if they have not reached their group size
     */
    public void finishMinimizing() {

        pendingLines.forEach( (key, list) -> copyToSortedElements(Line.merge(list)) );
        pendingLines.clear();

        sortedElements.forEach( (key, list) -> list.stream().sorted().forEach(this::export) );
    }

    /**
     * Appends the passed line to the file set in "destination"
     * @param line
     *      The line to be exported
     */
    private void export(Line line) {
        File destination = new File(destinationFolder + line.label + ".csv");
        if(!destination.exists()) try {
            destination.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(destination.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
