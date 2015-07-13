package com.poolingpeople.jmeter.result;

/**
 * Created by hendrik on 13.07.15.
 *
 * A label is one type of request. Example "create stuff"
 */
public class Label {

    String name;
    int requests;

    int elapsedSum;
    int elapsedMin;
    int elapsedMax;

    public Label(String name) {
        this.name = name;
        requests = 0;
        elapsedSum = 0;
        elapsedMin = -1;
        elapsedMax = -1;
    }

    public void process(Line line) {
        requests++;
        elapsedSum += line.elapsed;
        if(line.elapsed < elapsedMin || elapsedMin == -1) {
            elapsedMin = line.elapsed;
        }
        if(line.elapsed > elapsedMax || elapsedMax == -1) {
            elapsedMax = line.elapsed;
        }
    }

    @Override
    public String toString() {
        return name + ": " + requests + " Requests" + System.lineSeparator() +
                "Durchschnittlich " + (elapsedSum / requests) + " Millisekunden" + System.lineSeparator() +
                "Minimal " + elapsedMin + " Millisekunden" + System.lineSeparator();
    }
}
