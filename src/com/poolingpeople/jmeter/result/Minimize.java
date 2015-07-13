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
    String destinationFolder;
    int groupSize;

    /**
     * Minimizes multiple lines to one line
     * @param destinationFolder
     *      The folder to write the merged lines
     * @param groupSize
     *      How many lines will be merged to one line
     */
    public Minimize(String destinationFolder, int groupSize) {
        // clean destination folder
        File folder = new File(destinationFolder);
        if(!folder.exists()) folder.mkdir();
        final File[] files = folder.listFiles();
        Arrays.stream(files).forEach(file -> file.delete());

        pendingLines = new HashMap<>();
        this.destinationFolder = destinationFolder;
        this.groupSize = groupSize;
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
            if(list.size() == groupSize) { // merge finished -> export
                export(Line.merge(list));
                pendingLines.remove(line.label);
            }
        } else { // create a new pending line for this label with the passed line
            LinkedList<Line> list = new LinkedList<>();
            list.add(line);
            pendingLines.put(line.label, list);
        }
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
