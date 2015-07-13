package com.poolingpeople.jmeter.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Created by hendrik on 13.07.15.
 */
public class Control {

    static String filename = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/results_tree_success_copy_long_run.csv";

    static String minimizedFolder = "/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/";

    public static void main(String[] args) {

        Analyse fullAnalyse = new Analyse(filename, false); // do not remember all data, only get statistics

        Minimize min = new Minimize(minimizedFolder, fullAnalyse, 1000);

        fullAnalyse.labels.forEach((key, label) -> {

            Analyse smallAnalyse = new Analyse(minimizedFolder + key + ".csv", true); // remember all data

            int width = 20000;
            int height = 10000;

            SVGImage image = new SVGImage(width, height);
            LinkedList<int[]> points = new LinkedList<>();

            double factorWidth = (double) width / (label.timestampMax - label.timestampMin);
            double factorHeight = (double) height / (label.elapsedMax - label.elapsedMin);

            smallAnalyse.list.stream().sorted().forEach(line -> {

                //try (Stream<String> lines = Files.lines(new File(minimizedFolder + key + ".csv").toPath(), Charset.defaultCharset())) {
                try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
                    int x = (int) ((line.timestamp - label.timestampMin) * factorWidth);
                    //int y = (int)(((l.elapsed - value.elapsedMin) * factorHeight - height / 2) * -1 + height / 2);
                    int y = (int) ((line.elapsed - label.elapsedMin) * factorHeight);
                    if (line.label.equals(key)) points.add(new int[]{x, y});
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            image.addPoint(points);
            image.export("/home/hendrik/dev/pooling-people/JMeter/Neo4j/JMeterNeo4jData/jmeter/logs/min/" + key + ".svg");

        });

    }

}
