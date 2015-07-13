package com.poolingpeople.jmeter.result;

import java.util.LinkedList;

/**
 * Created by hendrik on 13.07.15.
 */
public class Line {

    long timestamp = -1;
    int elapsed = -1;
    String label;
    int responseCode = -1;
    String responseMessage;
    String threadName;
    String dataType;
    String success;
    String failureMessage;
    String bytes;
    String grpThreads;
    String allThreads;
    String url;
    String filename;
    String latency;
    String encoding;
    String sampleCount;
    String errorCount;
    String hostname;
    String idleTime;

    public Line() {
        // default constructor to get an empty line
    }

    public Line(String line) {
        String[] values = line.split(",", -1);

        try {
            timestamp = Long.parseLong(values[0]);
        } catch (NumberFormatException e) {
            // Ignore everything that is not a number
        }
        try {
            elapsed = Integer.parseInt(values[1]);
        } catch (NumberFormatException e) {
            // Ignore everything that is not a number
        }
        label = values[2];
        try {
            responseCode = Integer.parseInt(values[3]);
        } catch (NumberFormatException e) {
            // Ignore everything that is not a number
        }
        responseMessage = values[4];
        threadName = values[5];
        dataType = values[6];
        success = values[7];
        failureMessage = values[8];
        bytes = values[9];
        grpThreads = values[10];
        allThreads = values[11];
        url = values[12];
        filename = values[13];
        latency = values[14];
        encoding = values[15];
        sampleCount = values[16];
        errorCount = values[17];
        hostname = values[18];
        idleTime = values[19];
    }

    /**
     * merges the current line with the passed ones. Number values will be averaged
     * @param lines
     *      The list of lines to be added
     */
    public void mergeWithCurrent(LinkedList<Line> lines) {
        lines.forEach( line -> {
            timestamp += line.timestamp;
            elapsed += line.elapsed;
        } );
        int elements = lines.size() + 1; // +1 because of current line has to be counted too
        timestamp = timestamp / elements;
        elapsed = elapsed / elements;
    }

    public static Line merge(LinkedList<Line> lines) {
        Line first = lines.pop();
        first.mergeWithCurrent(lines);
        return first;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp).append(",")
                .append(elapsed).append(",")
                .append(label).append(",")
                .append(responseCode).append(",")
                .append(responseMessage).append(",")
                .append(threadName).append(",")
                .append(dataType).append(",")
                .append(success).append(",")
                .append(failureMessage).append(",")
                .append(bytes).append(",")
                .append(grpThreads).append(",")
                .append(allThreads).append(",")
                .append(url).append(",")
                .append(filename).append(",")
                .append(latency).append(",")
                .append(encoding).append(",")
                .append(sampleCount).append(",")
                .append(errorCount).append(",")
                .append(hostname).append(",")
                .append(idleTime)
                .append(System.lineSeparator());
        return sb.toString();
    }

}
