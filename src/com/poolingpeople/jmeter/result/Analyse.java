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

    String filename;
    boolean loadFullList;
    HashMap<String, Label> labels;
    LinkedList<Line> list;

    public Analyse(String filename, boolean loadFullList) {
        this.filename = filename;
        this.loadFullList = loadFullList;
        analyse();
    }

    public HashMap<String, Label> analyse() {
        labels = new HashMap<>();

        // read and process each line
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered(this::process);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return labels;
    }

    /*
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
        min.finishMinimizing();

        // Print results
        labels.forEach( (key, value) -> System.out.println(value) );

        int width = 20000;
        int height = 10000;
        labels.forEach((key, value) -> { // loop different labels

            SVGImage image = new SVGImage(width, height);
            LinkedList<int[]> points = new LinkedList<>();

            double factorWidth = (double) width / (value.timestampMax - value.timestampMin);
            double factorHeight = (double) height / (value.elapsedMax - value.elapsedMin);

            //try (Stream<String> lines = Files.lines(new File(minimizedFolder + key + ".csv").toPath(), Charset.defaultCharset())) {
            try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> {
                    Line l = new Line(line);
                    int x = (int) ((l.timestamp - value.timestampMin) * factorWidth);
                    //int y = (int)(((l.elapsed - value.elapsedMin) * factorHeight - height / 2) * -1 + height / 2);
                    int y = (int) ((l.elapsed - value.elapsedMin) * factorHeight);
                    if (l.label.equals(key)) points.add(new int[]{x, y});
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.addPoint(points);
            image.export("/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/" + key + ".svg");
        });

    }
    */

    /**
     * processes the passed line and evaluate the values for the wished results
     * @param lineString
     *      The line to be processed as a String
     */
    public void process(String lineString) {

        Line line = new Line(lineString);

        // remember this line only if flag set
        if(loadFullList) {
            list.add(line);
        }

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
