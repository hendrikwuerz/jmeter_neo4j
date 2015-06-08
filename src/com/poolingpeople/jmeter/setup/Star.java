package com.poolingpeople.jmeter.setup;

import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by hendrik on 08.06.15.
 */
public class Star {

    String content, uuid;
    LinkedList<Star> peaks = new LinkedList<>();

    public Star(int amount, int deep) {

        uuid = UUID.randomUUID().toString();
        content = DataGenerator.getRandomString(10, DataGenerator.CHARACTERS);

        if(deep > 0) {
            for (int i = 0; i < amount; i++) {
                peaks.add(new Star(amount, deep - 1));
            }
        }

    }

    public String getElement() {
        String result = uuid + "," + content + System.lineSeparator();

        StringBuilder sb = new StringBuilder(result);
        peaks.forEach( star -> sb.append(star.getElement()) );

        return sb.toString();
    }

    public String getRelation() {
        StringBuilder sb = new StringBuilder();
        peaks.forEach( star -> sb.append(uuid).append(",").append(star.uuid).append(System.lineSeparator()).append(star.getRelation()) );
        return sb.toString();
    }

}
