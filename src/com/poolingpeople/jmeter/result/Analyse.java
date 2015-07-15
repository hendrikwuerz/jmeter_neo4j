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

    File data;
    boolean loadFullList;
    LinkedList<Line> list;
    Label label;

    // start analyse from lines
    public Analyse(LinkedList<Line> list) {
        this.loadFullList = false;
        this.list = list;
        this.label = new Label(list.getFirst().label);

        list.stream().forEach(this::process);
    }

    // start analyse from file
    public Analyse(File data, boolean loadFullList) {
        this.data = data;
        this.loadFullList = loadFullList;
        analyse();
    }

    public void analyse() {
        // read and process each line
        try (Stream<String> lines = Files.lines(data.toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(this::process);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * processes the passed line and evaluate the values for the wished results
     * @param lineString
     *      The line to be processed as a String
     */
    public void process(String lineString) {
       process(new Line(lineString));
    }

    /**
     * processes the passed line and evaluate the values for the wished results
     * @param line
     *      The line to be processed
     */
    public void process(Line line) {

        // remember this line only if flag set
        if(loadFullList) {
            list.add(line);
        }

        if(label == null) { // first line -> now label is known
            label = new Label(line.label);
        }

        label.process(line);
    }

}
