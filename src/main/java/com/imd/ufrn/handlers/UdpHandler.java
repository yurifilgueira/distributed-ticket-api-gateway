package com.imd.ufrn.handlers;

import com.imd.ufrn.clients.UdpClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class UdpHandler implements Runnable {

    private Logger logger = Logger.getLogger(UdpHandler.class.getName());
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UdpHandler() {
    }

    public UdpHandler(DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {

        logger.info("\u001B[34mDispatching request\u001B[0m");
        try {

            UdpClient udpClient = new UdpClient();
            String request = new String(packet.getData(), 0, packet.getLength());
            String response = udpClient.sendRequest(request, 8081);

            packet = new DatagramPacket(response.getBytes(), response.getBytes().length, packet.getAddress(), packet.getPort());

            socket.send(packet);
            logger.info("\u001B[32mResponse sent to the client\u001B[0m");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] tokenize(DatagramPacket packet) {
        String[] tokens = new String(packet.getData(), 0, packet.getLength()).split(";");

        String operation = tokens[0];
        String path = tokens[1];

        StringBuilder body = new StringBuilder();

        for (int i = 2; i < tokens.length; i++) {
            body.append(tokens[i]);
            if (i < tokens.length - 1) {
                body.append(";");
            }
        }

        return new String[]{operation, path, body.toString()};

    }

}