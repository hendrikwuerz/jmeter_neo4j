package com.poolingpeople.jmeter.result;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hendrik on 15.07.15.
 */
public class Diagram {

    LinkedList<Line> lines;
    String destinationFolder;

    /**
     * Creates a new Diagram from the passed list
     * @param lines
     *          A SORTED List of lines
     * @param destinationFolder
     *          The folder where the image should be stored
     */
    public Diagram(LinkedList<Line> lines, String destinationFolder) {
        this.lines = lines;
        this.destinationFolder = destinationFolder;

        // get data for min max of passed lines
        Label label = new Analyse(lines).label;

        // size of SVG image
        int width = 20000;
        int height = 10000;

        // create image and list for points
        SVGImage image = new SVGImage(width, height);

        // factors mapping the timestamp to x-coordinates an the elapsed time to y-coordinates
        double factorWidth = (double) width / label.requests;
        double factorHeight = (double) height / label.elapsedMax;

        int[] counter = {0};
        List<int[]> points = lines.stream()
                .map(line -> {
                    int x = (int) (counter[0] * factorWidth);
                    int y = (int) (line.elapsed * factorHeight);
                    // mirror y
                    y = (y - height / 2) * -1 + height / 2;
                    counter[0]++;
                    return new int[]{x, y};
                })
                .collect(Collectors.toList());

        //image.addPoint(points);
        image.addPath(points);
        image.export(destinationFolder + label.name + ".svg");
    }
}
