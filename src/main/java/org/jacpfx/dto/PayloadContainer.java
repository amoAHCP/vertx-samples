package org.jacpfx.dto;

/**
 * Created by Andy Moncsek on 13.12.13.
 */
public class PayloadContainer {

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
