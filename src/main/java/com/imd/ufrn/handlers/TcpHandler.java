package com.imd.ufrn.handlers;

import com.imd.ufrn.clients.TcpClient;
import com.imd.ufrn.heartbeat.ServerEntity;
import com.imd.ufrn.servers.ServerManager;

import java.io.*;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TcpHandler implements Runnable {

    private Logger logger = Logger.getLogger(TcpHandler.class.getName());
    private Socket socket;

    public TcpHandler() {
    }

    public TcpHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            logger.info("\u001B[34mDispatching request\u001B[0m");

            String request = bufferedReader.readLine();
            String[] tokens = request.split(";");

            String response = null;

            if (tokens[0].equals("/register")) {

                ServerEntity entity = new ServerEntity("/tickets", socket.getInetAddress(), Integer.parseInt(tokens[1]), true);

                if (ServerManager.getInstance().getServers().contains(entity)) {
                    System.out.println("Server already exists");
                }

                ServerManager.getInstance().addServer(entity);

                response = "REGISTERED";
            }
            else{
                TcpClient client = new TcpClient();
                ServerEntity serverEntity = ServerManager.getInstance().getAvailableServer();
                if (serverEntity != null) {
                    response = client.sendRequest(request, serverEntity.getAddress(), serverEntity.getPort());
                } else {
                    response = "500 - NO SERVERS AVAILABLE";
                }
            }
            bufferedWriter.write(response);
            bufferedWriter.flush();
            logger.info("\u001B[32mResponse sent to the client\u001B[0m");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    logger.info("\u001B[32mConnection successfully closed\u001B[0m");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warning("\u001B[31mFailed to close the connection\u001B[0m");
            }
        }
    }


    private String[] tokenize(String line) {
        String[] tokens = line.split(";");

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