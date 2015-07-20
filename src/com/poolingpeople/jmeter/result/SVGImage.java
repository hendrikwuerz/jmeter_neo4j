package com.poolingpeople.jmeter.result;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hendrik on 13.07.15.
 */
public class SVGImage {

    int width, height;
    Color background;
    StringBuilder elements;

    public SVGImage() {
        this(16000, 9000);
    }

    public SVGImage(int width, int height) {
        this(width, height, new Color(222, 222, 222));
    }

    public SVGImage(int width, int height, Color background) {
        this.width = width;
        this.height = height;
        this.background = background;
        elements = new StringBuilder();
    }

    /**
     * Draw a line between the passed points
     * @param pointA
     *          Start point of line
     * @param pointB
     *          End point of line
     * @param width
     *          The width of the rendered lines
     * @param color
     *          The color of the path
     */
    public void addPath(int[] pointA, int[] pointB, int width, Color color) {
        LinkedList<int[]> tmp = new LinkedList<>();
        tmp.add(pointA);
        tmp.add(pointB);
        addPath(tmp, width, color);
    }

    /**
     * Draw a path based on a list of points
     * @param points
     *          An ordered list of points which will be connected by a line.
     *          The list must contain minimum two elements
     * @param width
     *          The width of the rendered lines
     * @param color
     *          The color of the path
     */
    public void addPath(List<int[]> points, int width, Color color) {
        StringBuilder sb = new StringBuilder();
        int[] firstPoint = points.get(0);
        points.remove(0);
        sb.append("M " + firstPoint[0] + "," + firstPoint[1] + " L");
        points.forEach(p -> sb.append(" " + p[0] + "," + p[1]));

        elements.append("<path fill=\"none\" stroke=\"" + Util.getHexCode(color) + "\" stroke-width=\"" + width + "\" stroke-linejoin=\"round\" d=\"")
                .append(sb)
                .append("\"/>")
                .append(System.lineSeparator());

    }

    public void addPoint(List<int[]> points) {
        StringBuilder sb = new StringBuilder();

        points.forEach(p -> elements.append("<circle cx=\"" + p[0] + "\" cy=\"" + p[1] + "\" r=\"10\" stroke=\"rgb(0,69,134)\" stroke-width=\"80\" fill=\"black\" />").append(System.lineSeparator()));

    }


    public void addText(int x, int y, String textAnchor, int fontSize, int text, Color color) {
        addText(x, y, textAnchor, fontSize, String.valueOf(text), color);
    }

    /**
     *
     * @param x
     *          The x-coordinate of the text
     * @param y
     *          The y-coordinate of the text
     * @param textAnchor
     *          Alignment: start | middle | end
     * @param fontSize
     *          The size of the written text
     * @param text
     *          The text to be added
     * @param color
     *          The color of the text
     */
    public void addText(int x, int y, String textAnchor, int fontSize, String text, Color color) {
        elements.append("<text text-anchor=\"" + textAnchor + "\" x=\"" + x + "\" y=\"" + y + "\" font-family=\"Verdana\" font-size=\"" + fontSize + "\" fill=\"" + Util.getHexCode(color) + "\" >" + text + "</text>").append(System.lineSeparator());
    }

    public void export(String file) {
        try {
            Files.write( new File(file).toPath(), toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<svg width=\"160mm\" height=\"90mm\" viewBox=\"0 0 " + width + " " + height + "\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.2\" baseProfile=\"tiny\" viewport-fill=\"red\"> \n" +
                "<rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\"" + Util.getHexCode(background) + "\" stroke-width=\"0\"/>" +
                elements.toString() +
                "</svg>";
    }

}
