package com.poolingpeople.jmeter.result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by hendrik on 08.07.15.
 */
public class Analyse {

    LinkedList<Line> list;
    Label label;


    // start analyse from lines
    public Analyse(LinkedList<Line> list) {
        this.list = list;
        this.label = new Label(list.getFirst().label);
        int size = list.size();

        // evaluate all data in label
        list.stream().forEach(label::process);

        // get global statistic data
        // calc median and 90% Line
        LinkedList<Line> listElapsedSorted = (LinkedList<Line>)list.clone();
        // sort list after elapsed time to get the median
        listElapsedSorted.sort( (l1, l2) -> Integer.compare(l1.elapsed, l2.elapsed));

        // median
        if(size % 2 == 0) {
            label.setElapsedMedian((listElapsedSorted.get((size+1) / 2 - 1).elapsed + listElapsedSorted.get((size+1) / 2 - 1).elapsed) / 2);
        } else {
            label.setElapsedMedian(listElapsedSorted.get( (size+1) / 2 - 1).elapsed);
        }

        // 90% Line
        int idx = (int)(0.9 * size);
        label.setElapsed90Line(listElapsedSorted.get(idx).elapsed);
    }

    public static int countLines(File file) {
        int lines = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public String toString() {
        return "Analyse for " + label.name + System.lineSeparator() +
                "Based on " + list.size() + " Elements" + System.lineSeparator() +
                label.toString();
    }

}
