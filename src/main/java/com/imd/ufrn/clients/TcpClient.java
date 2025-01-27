package com.imd.ufrn.clients;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {

    public String sendRequest(String request, int port) throws IOException {
        Socket socket = null;

        InetAddress address = InetAddress.getByName("localhost");

        socket = new Socket(address, port);

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
            throw new IOException(e.getMessage());
        }
        finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

}
