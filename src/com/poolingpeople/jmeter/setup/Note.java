package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 01.06.15.
 */
public class Note {

    long date;
    String uuidOfOwner, description, privacy, title, type, uuid;

    public Note(String uuidOfOwner) {
        this.uuidOfOwner = uuidOfOwner;
        date = System.currentTimeMillis() - (long)(Math.random() * 1000000000);
        description = "This is my great note description: " + DataGenerator.getRandomString(30, DataGenerator.CHARACTERS);
        privacy = "PRIVATE";
        title = "My note title ";
        type = "note";
        uuid = UUID.randomUUID().toString();
    }

    /*
    public Note(String s) {
        String[] arr = s.split(",");
        date = Long.parseLong(arr[0]);
        description = arr[1];
        privacy = arr[2];
        title = arr[3];
        type = arr[4];
        uuid = arr[5];
    }
    */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(uuidOfOwner).append(",");
        sb.append(date).append(",");
        sb.append(description).append(",");
        sb.append(privacy).append(",");
        sb.append(title).append(",");
        sb.append(type).append(",");
        sb.append(uuid).append(System.lineSeparator());

        return sb.toString();
    }
}
