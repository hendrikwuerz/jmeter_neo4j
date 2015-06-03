package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 27.05.15.
 */
public class Task {

    long now1, now2;
    String uuidOfOwner, title, type, uuid, status, privacy;

    public Task(String owner) {
        now1 = System.currentTimeMillis() - (long)(Math.random() * 1000000000);
        now2 = now1 + (long)(Math.random() * 1000);
        uuidOfOwner = owner;
        title = "My task title " + DataGenerator.getRandomString(5, DataGenerator.CHARACTERS);
        type = "task";
        uuid = UUID.randomUUID().toString();
        status = "OPEN";
        privacy = "PRIVATE";
    }

    public Task(String s, boolean csv) {
        String[] arr = s.split(",");
        now1 = Long.parseLong(arr[0]);
        now2 = Long.parseLong(arr[1]);
        uuidOfOwner = arr[2];
        title = arr[3];
        type = arr[4];
        uuid = arr[5];
        status = arr[6];
        privacy = arr[7];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(now1).append(",");
        sb.append(now2).append(",");
        sb.append(uuidOfOwner).append(",");
        sb.append(title).append(",");
        sb.append(type).append(",");
        sb.append(uuid).append(",");
        sb.append(status).append(",");
        sb.append(privacy).append(System.lineSeparator());

        return sb.toString();
    }

}
