package com.poolingpeople.jmeter.result;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Created by hendrik on 15.07.15.
 */
public class Split {

    public static void split(String filename, String destinationFolder) {

        Util.cleanFolder(destinationFolder);

        // read and process each line
        try (Stream<String> lines = Files.lines(new File(filename).toPath(), Charset.defaultCharset())) {
            lines.forEachOrdered( line -> Util.exportLine(new Line(line), destinationFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
