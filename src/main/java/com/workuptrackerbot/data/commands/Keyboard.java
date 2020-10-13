package com.workuptrackerbot.data.commands;

public enum Keyboard {

    START("keyboard.tracking.start.button","keyboard.tracking.start.message", "keyboard.tracking.start.callbackdata"),
    STOP("keyboard.tracking.stop.button","keyboard.tracking.stop.message", "keyboard.tracking.stop.callbackdata");


    private  String button;
    private  String message;
    private  String callbackdata;

    Keyboard(String button, String message, String callbackdata) {
        this.button = button;
        this.message = message;
        this.callbackdata = callbackdata;
    }

    public String getButton() {
        return button;
    }

    public String getMessage() {
        return message;
    }

    public String getCallbackdata() {
        return callbackdata;
    }
}
