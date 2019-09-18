package network;

import app.Server;
import exception.ClientOfflineException;
import utils.ServerMessageHandler;

public class ClientConnection implements Runnable {

    private Connection connection;

    public ClientConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ServerMessageHandler.handleIncomingMessage(connection.readMessage(), this);
            }
        } catch (Exception e) {
            System.out.println("Caiu aqui nego, ip=" + connection.socket.getInetAddress());
            System.out.println("Exception: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            Server.removeFromTargets(this);
            Server.clientConnection();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

