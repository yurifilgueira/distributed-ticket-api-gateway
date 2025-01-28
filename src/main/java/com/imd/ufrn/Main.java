package com.imd.ufrn;

import com.imd.ufrn.heartbeat.HeartBeatManager;
import com.imd.ufrn.servers.Server;
import com.imd.ufrn.servers.TcpServer;
import com.imd.ufrn.servers.UdpServer;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);

        Server server = new TcpServer(port);

        HeartBeatManager.getInstance().configureServer("tcp");

        server.start();
    }
}