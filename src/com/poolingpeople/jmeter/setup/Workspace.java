package com.poolingpeople.jmeter.setup;

import java.util.UUID;

/**
 * Created by hendrik on 27.05.15.
 */
public class Workspace {

    String workspaceUuid, ownerUuid;

    public Workspace(String ownerUuid) {
        this.ownerUuid = ownerUuid;
        workspaceUuid = UUID.randomUUID().toString();
    }


    @Override
    public String toString() {
        return ownerUuid + "," + workspaceUuid + "," + System.lineSeparator();
    }

}
