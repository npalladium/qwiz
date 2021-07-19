package com.quantumdisruption.qwiz.QWiz;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.JSONObject;

import java.util.Date;

@Getter
@EqualsAndHashCode
public class EventMessage {
    public String type;
    public String message;
    public String channel;
    public String user;

    public Date date;

    public EventMessage(String type, String message, String channel, String user) {
        this.type = type;
        this.message = message;
        this.channel = channel;
        this.user = user;
        this.date = new Date();
    }

    public String toString() {
        JSONObject j = new JSONObject().put("type", this.getType()).put("message", this.getMessage()).put("channel", this.getChannel()).put("user", this.getUser());
        return j.toString();
    }

}
