package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 03.06.15.
 */
public class LinkedListElement {

    String uuid, uuidBefore, text;

    public LinkedListElement(String uuidBefore, String text) {
        this.uuid = UUID.randomUUID().toString();
        this.uuidBefore = uuidBefore;
        this.text = text;
    }

    @Override
    public String toString() {
        return uuidBefore + "," + uuid + "," + text + System.lineSeparator();
    }
}
