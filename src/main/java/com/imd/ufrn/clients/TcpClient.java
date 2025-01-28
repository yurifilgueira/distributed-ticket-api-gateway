package com.imd.ufrn.clients;

import com.imd.ufrn.heartbeat.ServerEntity;
import com.imd.ufrn.servers.ServerManager;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient implements Client {

    public String sendRequest(String request, InetAddress address, int port) {
        Socket socket = null;

        try {
            socket = new Socket(address, port);

            socket.setSoTimeout(3000);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ){

            bufferedWriter.write(request);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String response = bufferedReader.readLine();

            return response;
        }catch (Exception e) {
            return null;
        }
        finally {

            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
