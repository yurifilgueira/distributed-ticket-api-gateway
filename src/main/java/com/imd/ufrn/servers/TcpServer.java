package com.imd.ufrn.servers;

import com.imd.ufrn.handlers.TcpHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TcpServer extends Server {

    private Logger logger = Logger.getLogger(TcpServer.class.getName());

    public TcpServer() {
    }

    public TcpServer(Integer port) {
        super(port);
    }

    @Override
    public void start() {
        logger.info("\u001B[34mStarting TCP Server\u001B[0m");

        ServerSocket server = null;

        try {

            server = new ServerSocket(port);

            logger.info("\u001B[34mTCP Server started\u001B[0m");

            while (true) {
                Socket socket = server.accept();

                String clientAddress = socket.getInetAddress().getHostAddress();
                int clientPort = socket.getPort();

                logger.info("\u001B[32mConnection established with client: " + clientAddress + ":" + clientPort + "\u001B[0m");

                executor.execute(new TcpHandler(socket));

            }

        }catch (IOException ioe) {
            logger.warning(ioe.toString());
        }finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
