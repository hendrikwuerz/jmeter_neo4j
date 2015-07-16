package com.poolingpeople.jmeter.result;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

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
        // calc median
        LinkedList<Line> listElapsedSorted = (LinkedList<Line>)list.clone();
        // sort list after elapsed time to get the median
        listElapsedSorted.sort( (l1, l2) -> Integer.compare(l1.elapsed, l2.elapsed));
        if(size % 2 == 0) {
            label.setElapsedMedian((listElapsedSorted.get((size+1) / 2 - 1).elapsed + listElapsedSorted.get((size+1) / 2 - 1).elapsed) / 2);
        } else {
            label.setElapsedMedian(listElapsedSorted.get( (size+1) / 2 - 1).elapsed);
        }
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
