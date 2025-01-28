package com.imd.ufrn.servers;

import com.imd.ufrn.heartbeat.HeartBeatManager;
import com.imd.ufrn.heartbeat.ServerEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerManager {

    private List<ServerEntity> servers = new CopyOnWriteArrayList<>();
    private Integer idx = 0;

    private static class ServerManagerHolder {
        private static final ServerManager INSTANCE = new ServerManager();
    }

    public static ServerManager getInstance() {
        return ServerManagerHolder.INSTANCE;
    }

    public List<ServerEntity> getServers() {
        return List.copyOf(servers);
    }

    public void addServer(ServerEntity server) {
        servers.add(server);

        HeartBeatManager.getInstance().startMonitoring(server);
    }

    public ServerEntity getAvailableServer() {
        synchronized (this) {
            if (servers.isEmpty()) {
                return null;
            }

            int startIndex = idx;

            do {
                if (idx >= servers.size()) {
                    idx = 0;
                }

                ServerEntity server = servers.get(idx);
                idx++;

                if (server.getAlive()) {
                    return server;
                }
            } while (idx != startIndex);

            return null;
        }
    }
}
