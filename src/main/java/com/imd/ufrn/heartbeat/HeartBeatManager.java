package com.imd.ufrn.heartbeat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HeartBeatManager {

    private ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private String serverType;

    private static class HeartBeatHolder {
        private static final HeartBeatManager INSTANCE = new HeartBeatManager();
    }

    public static HeartBeatManager getInstance() {
        return HeartBeatHolder.INSTANCE;
    }

    public void configureServer(String serverType) {
        if (this.serverType == null) {
            this.serverType = serverType;
        }
    }

    public void startMonitoring(ServerEntity serverEntity) {
        HeartBeat heartBeat = new HeartBeat(serverType, serverEntity);

        startHeartBeat(heartBeat);
    }

    private void startHeartBeat(HeartBeat heartBeat) {
        executor.execute(heartBeat);
    }

}
