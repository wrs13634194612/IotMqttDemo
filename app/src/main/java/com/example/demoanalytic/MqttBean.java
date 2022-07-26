package com.example.demoanalytic;


        import java.io.Serializable;

public class MqttBean implements Serializable {
    public String topic;
    public String message;
    public boolean register;
    public boolean isUnregister;

    public MqttBean(String topic, String message, boolean isUnregister) {
        this.topic = topic;
        this.message = message;
        this.isUnregister = isUnregister;
    }

    public MqttBean(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public MqttBean(String topic, boolean register) {
        this.topic = topic;
        this.register = register;
    }


}
