package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {
    public ObjectOutputStream out;
    public ObjectInputStream in;
    public Socket socket;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Message readMessage() throws IOException, ClassNotFoundException {
        Message input;
        while(true) {
            if((input = (Message) in.readObject()) != null) {
                return input;
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
