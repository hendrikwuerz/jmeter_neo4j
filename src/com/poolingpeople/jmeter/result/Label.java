package com.poolingpeople.jmeter.result;

/**
 * Created by hendrik on 13.07.15.
 *
 * A label is one type of request. Example "create stuff"
 */
public class Label {

    String name;
    int requests;
    int elapsed;

    public Label(String name) {
        this.name = name;
        requests = 0;
        elapsed = 0;
    }

    public void process(Line line) {
        requests++;
        elapsed += line.elapsed;
    }

    @Override
    public String toString() {
        return name + ": " + requests + " Requests" + System.lineSeparator() + "Durchschnittlich " + (elapsed / requests) + " Millisekunden" + System.lineSeparator();
    }
}
