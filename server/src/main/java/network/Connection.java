package network;

import exception.ClientOfflineException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

    public Message readMessage() throws ClientOfflineException {
        Message input;
        try {
            while (true)
                if ((input = (Message) in.readObject()) != null) {
                    return input;
                }
        } catch (Exception e) {
            throw new ClientOfflineException();
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

    public Socket getSocket() {
        return socket;
    }
}
