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
                return null; // Nenhum servidor disponível
            }

            int startIndex = idx; // Armazena o índice inicial para evitar loops infinitos

            do {
                // Garantir que o índice seja cíclico
                if (idx >= servers.size()) {
                    idx = 0;
                }

                // Escolher o servidor no índice atual
                ServerEntity server = servers.get(idx);
                idx++;

                // Verificar se o servidor está ativo
                if (server.getAlive()) {
                    return server; // Retorna o servidor se estiver ativo
                }
            } while (idx != startIndex); // Continua até que todos os servidores tenham sido verificados

            // Se nenhum servidor ativo for encontrado, retorna null
            return null;
        }
    }
}
