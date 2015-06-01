package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 01.06.15.
 */
public class Pool {

    String uuidOfOwner, description, privacy, type, title, uuid;

    public Pool(String owner) {
        uuidOfOwner = owner;
        description= "My description for a pool " + DataGenerator.getRandomString(10, DataGenerator.CHARACTERS);
        privacy = "PRIVATE";
        type = "pool";
        title = "My pool title " + DataGenerator.getRandomString(7, DataGenerator.CHARACTERS);
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(uuidOfOwner).append(",");
        sb.append(privacy).append(",");
        sb.append(description).append(",");
        sb.append(type).append(",");
        sb.append(title).append(",");
        sb.append(uuid).append(System.lineSeparator());

        return sb.toString();
    }
}
