package com.poolingpeople.jmeter.result;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hendrik on 15.07.15.
 */
public class Diagram {

    LinkedList<Line> lines;
    Analyse analyse;
    String destinationFolder;

    // size of SVG image
    int diagramMargin = 200; // margin to the borders of the image
    int topicHeight = 1500; // height of the topic (name of diagram)
    int diagramHeight = 10000; // height of the diagram
    int numberPadding = 1500; // padding to write the numbers to the aches (left and bottom)
    int statisticHeight = 3500; // height at the bottom to write statistic data (average, min, max, ...)

    // global image size
    private int width = 25000;
    private int height;
    private SVGImage image;

    /**
     * create a new empty diagram
     */
    public Diagram() {
        height = diagramMargin * 2 + topicHeight + diagramHeight + numberPadding + statisticHeight;
    }

    /**
     * Creates a new Diagram from the passed list
     * @param lines
     *          A SORTED List of lines
     * @param destinationFolder
     *          The folder where the image should be stored
     */
    public Diagram(LinkedList<Line> lines, String destinationFolder, Analyse analyse) {
        this();

        this.lines = lines;
        this.analyse = analyse;
        this.destinationFolder = destinationFolder;

        createDiagram();
    }

    public void createDiagram() {
        int lineWidth = 80; // the width of the lines
        Color lineColor = new Color(0, 69, 134); // color of the lines

        // create image and list for points
        image = new SVGImage(width, height);

        // draw title
        drawTitle();

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
        drawCoordinate(10);
        image.addPath(points, lineWidth, lineColor);

        // draw statistic data
        drawStatistics();

        // export to file
        image.export(destinationFolder + analyse.label.name + ".svg");
    }

    /**
     * draw the title of the diagram to the image
     */
    private void drawTitle() {
        int realTopicSize = (int)(0.7 * topicHeight);
        image.addText(diagramMargin, realTopicSize, "start", realTopicSize, analyse.label.name, Color.black);
    }

    /**
     * creates a coordinate system
     * @param lines
     *          How many horizontal lines will be rendered
     */
    private void drawCoordinate(int lines) {

        int maxValue = analyse.label.elapsedMax;

        int width = 50; // the width of the lines
        Color color = Color.black; // color of the lines

        // draw horizontal lines
        String linePaddingWished = String.valueOf(maxValue / lines); // after which a new line is wished to come (here: real data values. They are != coordinate values)
        double factor = Math.pow(10, linePaddingWished.length() - 1); // How many digits the linePaddingWished has base 10
        int linePaddingReal = (int)(Math.round((maxValue / lines) / factor) * factor); // Calc real margin between two lines
        linePaddingReal = Math.max(1, linePaddingReal); // avoid division by zero error
        lines = Math.floorDiv(maxValue, linePaddingReal); // how many lines will be rendered

        double linePadding = (double)(image.height - numberPadding - topicHeight - statisticHeight) / lines;
        int fontSize = (int)(linePadding / 3);

        for(int i = 0; i < lines; i++) {
            // draw line
            int y = image.height - diagramMargin - statisticHeight - numberPadding - (int)(diagramMargin + i * linePadding);
            int[] pointA = new int[]{diagramMargin + (int)(0.8 * numberPadding), y};
            int[] pointB = new int[]{image.width - diagramMargin, y};
            image.addPath(pointA, pointB, width, color);

            // draw value at begin of line
            String number = String.valueOf(linePaddingReal * i);
            image.addText(diagramMargin + (int) (0.7 * numberPadding), y + fontSize / 4, "end", fontSize, number, color);
        }

        // draw vertical lines
        int x = diagramMargin + numberPadding;
        int y1 = diagramMargin + topicHeight;
        int y2 = image.height - diagramMargin - statisticHeight - (int)(0.8 * numberPadding);

        // first line over full height
        image.addPath(new int[]{x, y1}, new int[]{x, y2}, width, color);

        // all vertical lines after first one only on x-axis
        y1 = image.height - diagramMargin - statisticHeight - (int)(1.3 * numberPadding);

        int amountOfTimestamps = 5;
        for(int i = 0; i < amountOfTimestamps; i++) {
            // calc current time
            long timestamp = this.lines.get(analyse.label.requests / amountOfTimestamps * i).timestamp;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            String text = calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND);

            // write time on current position
            x = diagramMargin + numberPadding + i * (image.width - diagramMargin - numberPadding) / amountOfTimestamps;
            image.addText(x, y2 + (int)(0.3 * numberPadding), "middle", fontSize, text, color);

            // draw small line on current position
            image.addPath(new int[]{x, y1}, new int[]{x, y2}, width, color);
        }
    }


    /**
     * adds the analysed data to the bottom of the svg image
     */
    private void drawStatistics() {

        int fontSize = 500;

        // elapsed time statistics
        int valuePadding = 4000; // padding of the values to the left

        int row = 0;
        // title of data
        image.addText(diagramMargin, image.height - statisticHeight + row * fontSize, "start", fontSize, "Original Requests", Color.black);
        row++;
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
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.originalAmountRequests, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.requests, Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedAverage + "ms", Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMin + "ms", Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMax + "ms", Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsedMedian + "ms", Color.black);
        row++;
        image.addText(diagramMargin + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, analyse.label.elapsed90Line + "ms", Color.black);



        // timestamp statistics
        int blockPadding = valuePadding + 3500;
        valuePadding = 4000;

        // title of data
        row = 0;
        image.addText(diagramMargin + blockPadding, image.height - statisticHeight + row * fontSize, "start", fontSize, "Start timestamp", Color.black);
        row++;
        image.addText(diagramMargin + blockPadding, image.height - statisticHeight + row * fontSize, "start", fontSize, "End timestamp", Color.black);
        row++;
        image.addText(diagramMargin + blockPadding, image.height - statisticHeight + row * fontSize, "start", fontSize, "run time", Color.black);

        // value of data
        row = 0;
        image.addText(diagramMargin + blockPadding + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, (new Date(analyse.label.timestampMin)).toString(), Color.black);
        row++;
        image.addText(diagramMargin + blockPadding + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, (new Date(analyse.label.timestampMax)).toString(), Color.black);
        row++;
        image.addText(diagramMargin + blockPadding + valuePadding, image.height - statisticHeight + row * fontSize, "start", fontSize, (analyse.label.timestampMax - analyse.label.timestampMin) + "ms", Color.black);
    }
}
