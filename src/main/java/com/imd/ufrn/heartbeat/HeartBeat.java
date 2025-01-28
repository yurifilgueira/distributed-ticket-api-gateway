package com.imd.ufrn.heartbeat;

import com.imd.ufrn.clients.Client;
import com.imd.ufrn.clients.TcpClient;
import com.imd.ufrn.clients.UdpClient;
import com.imd.ufrn.servers.Server;

import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class HeartBeat implements Runnable {

    private String serverType;
    private ServerEntity entity;
    private Logger logger = Logger.getLogger(HeartBeat.class.getName());

    public HeartBeat() {
    }

    public HeartBeat(String serverType, ServerEntity entity) {
        this.serverType = serverType;
        this.entity = entity;
    }

    @Override
    public void run() {
        Client client = getClient(serverType);


        boolean lastState = true;

        while (true) {

            String response = null;

            String request = "get;" + entity.getAssociatedRoute() + "/health";
            response = client.sendRequest(request, entity.getAddress(), entity.getPort());

            if (response.equals("500 - ERROR")) {
                if (lastState) {
                    String serverIsDown = "\u001B[34mServer " + entity.getPort() + " is down.\u001B[0m";
                    logger.info(serverIsDown);
                    entity.setAlive(false);
                    lastState = !lastState;
                }
            }else {
                if (!lastState) {
                    String serverIsUp = "\u001B[34mServer " + entity.getPort() + " is up.\u001B[0m";
                    logger.info(serverIsUp);
                    entity.setAlive(true);
                    lastState = !lastState;
                }
            }

            try {
                sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Client getClient(String serverType) {

        if (serverType.toUpperCase().equals("UDP")) {
            return new UdpClient();
        }
        else if (serverType.toUpperCase().equals("TCP")) {
            return new TcpClient();
        }
        else {
            return null;
        }

    }
}
