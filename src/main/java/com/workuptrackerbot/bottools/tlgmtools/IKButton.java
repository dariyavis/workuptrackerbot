package com.workuptrackerbot.bottools.tlgmtools;

import org.json.simple.JSONObject;

public class IKButton {
    private String text;
    private String path;
    private String data;

    public IKButton(String text, String path, String data) {
        this.text = text;
        this.path = path;
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
