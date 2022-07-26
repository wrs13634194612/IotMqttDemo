package com.example.demoanalytic;


        import java.io.Serializable;

public class StopService implements Serializable {
    public boolean isStop;

    public StopService(boolean isStop) {
        this.isStop = isStop;
    }
}
