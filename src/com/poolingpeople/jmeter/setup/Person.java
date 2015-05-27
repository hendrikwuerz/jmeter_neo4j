package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 27.05.15.
 */
public class Person {

    long now1, now2;
    String email, firstName, lastName, password, privacy, registrationCode, status, type, uuid;

    public Person(int i) {
        now1 = System.currentTimeMillis() - (long)(Math.random() * 1000000000);
        now2 = now1 + (long)(Math.random() * 1000);
        email = "user" + i + "@example.com";
        firstName = "user " + i;
        lastName = "lastname " + i;
        password = DataGenerator.getRandomString(10, DataGenerator.CHARACTERS);
        privacy = "PRIVATE";
        registrationCode = DataGenerator.getRandomString(10, DataGenerator.NUMBERS);
        status = "COMPLETED";
        type = "person";
        uuid = UUID.randomUUID().toString();
    }

    public Person(String s) {
        String[] arr = s.split(",");
        now1 = Long.parseLong(arr[0]);
        now2 = Long.parseLong(arr[1]);
        email = arr[2];
        firstName = arr[3];
        lastName = arr[4];
        password = arr[5];
        privacy = arr[6];
        registrationCode = arr[7];
        status = arr[8];
        type = arr[9];
        uuid = arr[10];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(now1).append(",");
        sb.append(now2).append(",");
        sb.append(email).append(",");
        sb.append(firstName).append(",");
        sb.append(lastName).append(",");
        sb.append(password).append(",");
        sb.append(privacy).append(",");
        sb.append(registrationCode).append(",");
        sb.append(status).append(",");
        sb.append(type).append(",");
        sb.append(uuid).append(System.lineSeparator());
        return sb.toString();
    }
}
