package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 01.06.15.
 */
public class Bug {

    String actionStatus, uuidOfOwner, privacy, type, title, priority, uuid;

    public Bug(String owner) {
        actionStatus = "OPEN";
        uuidOfOwner = owner;
        privacy = "PRIVATE";
        type = "note";
        title = "My note title " + DataGenerator.getRandomString(7, DataGenerator.CHARACTERS);
        priority = "P5";
        uuid = UUID.randomUUID().toString();
    }
/*
    public Bug(String s) {
        String[] arr = s.split(",");
        actionStatus = arr[0];
        uuidOfOwner = arr[1];
        privacy = arr[2];
        type = arr[3];
        title = arr[4];
        priority = arr[5];
        uuid = arr[6];
    }
*/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(actionStatus).append(",");
        sb.append(uuidOfOwner).append(",");
        sb.append(privacy).append(",");
        sb.append(type).append(",");
        sb.append(title).append(",");
        sb.append(priority).append(",");
        sb.append(uuid).append(System.lineSeparator());

        return sb.toString();
    }
}
