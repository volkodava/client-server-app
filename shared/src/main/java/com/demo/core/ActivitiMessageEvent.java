package com.demo.core;

public class ActivitiMessageEvent {

    private final ActivitiMessage activitiMessage;

    public ActivitiMessageEvent(ActivitiMessage activitiMessage) {
        this.activitiMessage = activitiMessage;
    }

    public ActivitiMessage getActivitiMessage() {
        return activitiMessage;
    }

    @Override
    public String toString() {
        return "event{" + "activitiMessage=" + activitiMessage + '}';
    }
}
