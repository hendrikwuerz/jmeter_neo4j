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
    public Diagram(LinkedList<Line> lines, String destinationFolder, Analyse analyse) {
        this.lines = lines;
        this.destinationFolder = destinationFolder;

        // size of SVG image
        int diagramMargin = 200; // margin to the borders of the image
        int topicHeight = 1500; // height of the topic (name of diagram)
        int diagramHeight = 10000; // height of the diagram
        int numberPadding = 1500; // padding to write the numbers to the aches (left and bottom)
        int statisticHeight = 3500; // height at the bottom to write statistic data (average, min, max, ...)

        int width = 25000;
        int height = diagramMargin * 2 + topicHeight + diagramHeight + numberPadding + statisticHeight;

        int lineWidth = 80; // the width of the lines
        Color lineColor = new Color(0, 69, 134); // color of the lines

        // create image and list for points
        SVGImage image = new SVGImage(width, height);

        // draw title
        int realTopicSize = (int)(0.7 * topicHeight);
        image.addText(diagramMargin, realTopicSize, "start", realTopicSize, analyse.label.name, Color.black);

        // factors mapping the timestamp to x-coordinates an the elapsed time to y-coordinates
        double factorWidth = (double) (width - 2 * diagramMargin - numberPadding) / analyse.label.requests;
        double factorHeight = (double) (diagramHeight) / analyse.label.elapsedMax;

        // calc position of the diagram points for the svg
        int[] counter = {0};
        List<int[]> points = lines.stream()
                .map(line -> {
                    int x = diagramMargin + numberPadding + (int) (counter[0] * factorWidth);
                    int y = height - diagramMargin - statisticHeight - numberPadding - (int) (line.elapsed * factorHeight);
                    counter[0]++;
                    return new int[]{x, y};
                })
                .collect(Collectors.toList());

        // draw diagram
        drawCoordinate(image, analyse.label.elapsedMax, 10, numberPadding, diagramMargin, topicHeight, statisticHeight);
        image.addPath(points, lineWidth, lineColor);

        // draw statistic data
        drawStatistics(image, analyse, diagramMargin, statisticHeight);

        // export to file
        image.export(destinationFolder + analyse.label.name + ".svg");
    }

    /**
     * creates a coordinate system
     * @param image
     *          The image where the coordinate system will be rendered
     * @param maxValue
     *          The max value of the y-axis
     * @param lines
     *          How many horizontal lines will be rendered
     * @param numberPadding
     *          how many padding is available to write numbers to the axis
     * @param diagramMargin
     *          margin to the borders of the image
     * @param paddingTop
     *          padding to the top of the image for other stuff
     * @param paddingBottom
     *          padding to the bottom of the image for other stuff
     */
    private static void drawCoordinate(SVGImage image, int maxValue, int lines, int numberPadding, int diagramMargin, int paddingTop, int paddingBottom) {

        int width = 50; // the width of the lines
        Color color = Color.black; // color of the lines

        // draw horizontal lines
        String linePaddingWished = String.valueOf(maxValue / lines); // after which a new line is wished to come (here: real data values. They are != coordinate values)
        double factor = Math.pow(10, linePaddingWished.length() - 1); // How many digits the linePaddingWished has base 10
        int linePaddingReal = (int)(Math.round((maxValue / lines) / factor) * factor); // Calc real margin between two lines
        linePaddingReal = Math.max(1, linePaddingReal); // avoid division by zero error
        lines = Math.floorDiv(maxValue, linePaddingReal); // how many lines will be rendered

        double linePadding = (double)(image.height - numberPadding - paddingTop - paddingBottom) / lines;
        int fontSize = (int)(linePadding / 3);

        for(int i = 0; i < lines; i++) {
            // draw line
            int y = image.height - diagramMargin - paddingBottom - numberPadding - (int)(diagramMargin + i * linePadding);
            int[] pointA = new int[]{diagramMargin + (int)(0.8 * numberPadding), y};
            int[] pointB = new int[]{image.width - diagramMargin, y};
            image.addPath(pointA, pointB, width, color);

            // draw value at begin of line
            String number = String.valueOf(linePaddingReal * i);
            image.addText(diagramMargin + (int) (0.7 * numberPadding), y + fontSize / 4, "end", fontSize, number, color);
        }

        // draw vertical lines
        int x = diagramMargin + numberPadding;
        int y1 = diagramMargin + paddingTop;
        int y2 = image.height - diagramMargin - paddingBottom - (int)(0.8 * numberPadding);
        image.addPath(new int[]{x, y1}, new int[]{x, y2}, width, color);
        image.addText(x, y2 + (int)(0.3 * numberPadding), "end", fontSize, "0", color);
    }

    private static void drawStatistics(SVGImage image, Analyse analyse, int diagramMargin, int statisticHeight) {

        int fontSize = 500;
        int valuePadding = 2500; // padding of the values to the left

        int row = 0;
        // title of data
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Requests", Color.black);
        row++;
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Average", Color.black);
        row++;
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Minimal", Color.black);
        row++;
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Maximal", Color.black);
        row++;
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Median", Color.black);
        row++;
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "90% line", Color.black);


        // values of data
        row = 0;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.requests, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedAverage, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMin, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMax, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMedian, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsed90Line, Color.black);
    }
}
