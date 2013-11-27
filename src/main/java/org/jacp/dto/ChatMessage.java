package org.jacp.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Andy Moncsek
 * Date: 27.11.13
 * Time: 09:54
 * This DTO represents a chat message
 */
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = -6569287031035361315L;
    private String from;
    private String to;
    private String message;

    public ChatMessage() {

    }

    public ChatMessage(final String from, final String to, final String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
