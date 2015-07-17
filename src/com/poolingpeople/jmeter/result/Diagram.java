package com.poolingpeople.jmeter.result;

import java.awt.*;
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
        int diagramMargin = 200;
        int numberPadding = 1500;

        int lineWidth = 80; // the width of the lines
        Color lineColor = new Color(0, 69, 134); // color of the lines

        // create image and list for points
        SVGImage image = new SVGImage(width, height);

        // factors mapping the timestamp to x-coordinates an the elapsed time to y-coordinates
        double factorWidth = (double) (width - 2 * diagramMargin - numberPadding) / label.requests;
        double factorHeight = (double) (height - 2 * diagramMargin - numberPadding) / label.elapsedMax;

        int[] counter = {0};
        List<int[]> points = lines.stream()
                .map(line -> {
                    int x = numberPadding + diagramMargin + (int) (counter[0] * factorWidth);
                    int y = height - numberPadding - diagramMargin - (int) (line.elapsed * factorHeight);
                    counter[0]++;
                    return new int[]{x, y};
                })
                .collect(Collectors.toList());

        //image.addPoint(points);
        drawCoordinate(image, label.elapsedMax, 10, numberPadding, diagramMargin);
        image.addPath(points, lineWidth, lineColor);
        image.export(destinationFolder + label.name + ".svg");
    }

    private static void drawCoordinate(SVGImage image, int maxValue, int lines, int numberPadding, int diagramMargin) {

        int width = 50; // the width of the lines
        Color color = Color.black; // color of the lines

        // draw horizontal lines
        String linePaddingWished = String.valueOf(maxValue / lines); // after which a new line is wished to come (here: real data values. They are != coordinate values)
        double factor = Math.pow(10, linePaddingWished.length() - 1); // How many digits the linePaddingWished has base 10
        int linePaddingReal = (int)(Math.round((maxValue / lines) / factor) * factor); // Calc real margin between two lines
        linePaddingReal = Math.max(1, linePaddingReal); // avoid division by zero error
        lines = Math.floorDiv(maxValue, linePaddingReal); // how many lines will be rendered

        double linePadding = (double)(image.height - numberPadding) / lines;
        int fontSize = (int)(linePadding / 3);

        for(int i = 0; i < lines; i++) {
            // draw line
            int y = image.height - numberPadding - (int)(diagramMargin + i * linePadding);
            int[] pointA = new int[]{diagramMargin + (int)(0.8 * numberPadding), y};
            int[] pointB = new int[]{image.width - diagramMargin, y};
            image.addPath(pointA, pointB, width, color);

            // draw value at begin of line
            String number = String.valueOf(linePaddingReal * i);
            image.addText(diagramMargin + (int) (0.7 * numberPadding), y + fontSize / 4, "end", fontSize, number, color);
        }

        // draw vertical lines
        int x = diagramMargin + numberPadding;
        int y1 = diagramMargin;
        int y2 = image.height - (int)(0.8 * numberPadding) - diagramMargin;
        image.addPath(new int[]{x, y1}, new int[]{x, y2}, width, color);
        image.addText(x, y2 + (int)(0.3 * numberPadding), "end", fontSize, "0", color);
    }
}
