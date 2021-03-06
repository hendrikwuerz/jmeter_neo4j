package com.poolingpeople.jmeter.analyse;

/**
 * Created by hendrik on 13.07.15.
 *
 * A label is one type of request. Example "create stuff"
 */
public class Label {

    String name;
    int requests;

    long timestampMin;
    long timestampMax;

    int elapsedSum;
    int elapsedAverage;
    int elapsedMin;
    int elapsedMax;
    int elapsedMedian;
    int elapsed90Line;

    public Label(String name) {
        this.name = name;
        requests = 0;

        timestampMin = -1;
        timestampMax = -1;

        elapsedSum = 0;
        elapsedMin = -1;
        elapsedMax = -1;
        elapsedMedian = -1;
    }

    public void process(Line line) {
        requests++;

        if(line.timestamp < timestampMin || timestampMin == -1) {
            timestampMin = line.timestamp;
        }
        if(line.timestamp > timestampMax || timestampMax == -1) {
            timestampMax = line.timestamp;
        }

        elapsedSum += line.elapsed;
        if(line.elapsed < elapsedMin || elapsedMin == -1) {
            elapsedMin = line.elapsed;
        }
        if(line.elapsed > elapsedMax || elapsedMax == -1) {
            elapsedMax = line.elapsed;
        }

        elapsedAverage = (elapsedSum / requests);
    }

    public void setElapsedMedian(int elapsedMedian) {
        this.elapsedMedian = elapsedMedian;
    }

    public void setElapsed90Line(int elapsed90Line) {
        this.elapsed90Line = elapsed90Line;
    }

    @Override
    public String toString() {
        return name + ": " + requests + " Requests" + System.lineSeparator() +
                "Durchschnittlich " + elapsedAverage + " Millisekunden" + System.lineSeparator() +
                "Minimal  " + elapsedMin + " Millisekunden" + System.lineSeparator() +
                "Maximal  " + elapsedMax + " Millisekunden" + System.lineSeparator() +
                "Median   " + elapsedMedian + " Millisekunden" + System.lineSeparator() +
                "90% Line " + elapsed90Line + " Millisekunden" + System.lineSeparator() +
                "Minimal  " + timestampMin + " Timestamp" + System.lineSeparator() +
                "Maximal  " + timestampMax + " Timestamp" + System.lineSeparator();
    }
}
