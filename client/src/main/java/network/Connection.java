package network;

import app.Client;
import exception.ServerOfflineException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection {
    public ObjectOutputStream out;
    public ObjectInputStream in;
    Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message readMessage() throws ServerOfflineException {
        Message input;
        try {
            while (true)
                if ((input = (Message) in.readObject()) != null) {
                    return input;
                }
        } catch (SocketException e) {
            Client.disconnected();
            throw new ServerOfflineException("Server went offline");
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
