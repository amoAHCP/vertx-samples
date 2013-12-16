package org.jacpfx.dto;

import java.io.Serializable;

/**
 * Created by Andy Moncsek on 13.12.13.
 */
public class PayloadContainer implements Serializable{

    private final String id;
    private final byte[] payload;

    public PayloadContainer(final String id, byte[] payload) {
        this.id = id;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public byte[] getPayload() {
        return payload;
    }
}
