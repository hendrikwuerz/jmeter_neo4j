package com.poolingpeople.jmeter.result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * Created by hendrik on 15.07.15.
 */
public class Util {


    public static void cleanFolder(String destinationFolder) {
        File folder = new File(destinationFolder);
        if(!folder.exists()) folder.mkdir();
        final File[] files = folder.listFiles();
        Arrays.stream(files).forEach(file -> file.delete());
    }


    /**
     * Appends the passed line to the file set with "destinationFolder"
     * @param line
     *      The line to be exported
     */
    public static void exportLine(Line line, String destinationFolder) {
        File destination = new File(destinationFolder + line.label + ".csv");
        if(!destination.exists()) try {
            destination.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(destination.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(line.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
