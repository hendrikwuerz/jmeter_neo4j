package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 27.05.15.
 */
public class Task {

    long now1, now2;
    String title, type, uuid;

    public Task(int i) {
        now1 = System.currentTimeMillis() - (long)(Math.random() * 1000000000);
        now2 = now1 + (long)(Math.random() * 1000);
        title = "My task title " + i;
        type = "task";
        uuid = UUID.randomUUID().toString();
    }

    public Task(String s) {
        String[] arr = s.split(",");
        now1 = Long.parseLong(arr[0]);
        now2 = Long.parseLong(arr[1]);
        title = arr[2];
        type = arr[3];
        uuid = arr[4];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(now1).append(",");
        sb.append(now2).append(",");
        sb.append(title).append(",");
        sb.append(type).append(",");
        sb.append(uuid).append(System.lineSeparator());

        return sb.toString();
    }

}
