package com.poolingpeople.jmeter.result;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hendrik on 15.07.15.
 */
public class Util {


    /**
     * removes all elements from the passed folder.
     * If the folder does not exists, it will be created
     * @param destinationFolder
     *          The folder
     */
    public static void cleanFolder(String destinationFolder) {
        File folder = new File(destinationFolder);
        if(!folder.exists()) folder.mkdir();
        final File[] files = folder.listFiles();
        Arrays.stream(files).forEach(file -> file.delete());
    }

    /**
     * Appends the passed lines to the file set with "destinationFolder" and label
     * @param line
     *      The lines to be exported
     * @param label
     *      The label of the passed data
     * @param destinationFolder
     *      The destination folder
     */
    public static void exportLines(List<String> line, String label, String destinationFolder) {
        File destination = prepareFileForExport(label, destinationFolder);
        try (BufferedWriter writer = Files.newBufferedWriter(destination.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            line.forEach(current -> {
                try {
                    writer.write(current);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the needed file if not existing and returns the file object
     * @param label
     *          The label for which the file should be created
     * @param destinationFolder
     *          The destination folder of the file
     * @return
     *          The created file object
     */
    private static File prepareFileForExport(String label, String destinationFolder) {
        File destination = new File(destinationFolder + label + ".csv");
        if(!destination.exists()) try {
            destination.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destination;
    }

    public static String getHexCode(Color color) {
        String c = Integer.toHexString(color.getRGB());
        return "#" + c.substring(2, c.length());
    }

}
