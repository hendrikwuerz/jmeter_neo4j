package com.poolingpeople.jmeter.analyse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Created by hendrik on 15.07.15.
 */
public class Split {

    public static void split(String filename, String destinationFolder) {

        HashMap<String, LinkedList<String>> pending = new HashMap<>();
        int buffer = 5000; // how many lines are buffered before save

        // read and process each line
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered( line -> {
                String label = Line.getLabelFor(line);
                // create new List for this type if not already known
                if(!pending.containsKey(label)) {
                    pending.put(label, new LinkedList<>());
                }
                // add the new line to the list
                LinkedList<String> list = pending.get(label);
                list.add(line);
                // check for export
                if(list.size() >= buffer) {
                    Util.exportLines(list, label, destinationFolder);
                    list.clear();
                }
            });

            // export last elements
            pending.forEach( (name, list) -> Util.exportLines(list, name, destinationFolder) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
