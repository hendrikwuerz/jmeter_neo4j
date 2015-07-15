package com.poolingpeople.jmeter.result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hendrik on 13.07.15.
 */
public class SVGImage {

    int width, height;
    StringBuilder elements;

    public SVGImage() {
        this(16000, 9000);
    }

    public SVGImage(int width, int height) {
        this.width = width;
        this.height = height;
        elements = new StringBuilder();
    }

    public void addPath(LinkedList<int[]> points) {
        StringBuilder sb = new StringBuilder();
        int[] firstPoint = points.pop();
        sb.append("M " + firstPoint[0] + "," + firstPoint[1] + " L");
        points.forEach( p -> sb.append(" " + p[0] + "," + p[1]));

        elements.append("<path fill=\"none\" stroke=\"rgb(0,69,134)\" stroke-width=\"80\" stroke-linejoin=\"round\" d=\"")
                .append(sb)
                .append("\"/>")
                .append(System.lineSeparator());

    }

    public void addPoint(List<int[]> points) {
        StringBuilder sb = new StringBuilder();

        points.forEach( p -> elements.append("<circle cx=\"" + p[0] + "\" cy=\"" + p[1] + "\" r=\"10\" stroke=\"rgb(0,69,134)\" stroke-width=\"80\" fill=\"black\" />").append(System.lineSeparator()) );

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
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                "<svg width=\"160mm\" height=\"90mm\" viewBox=\"0 0 " + width + " " + height + "\" version=\"1.1\" baseProfile=\"tiny\" xmlns=\"http://www.w3.org/2000/svg\" stroke-width=\"28.222\" stroke-linejoin=\"round\" xml:space=\"preserve\"> \n" +
                elements.toString() +
                "</svg>";
    }

}
