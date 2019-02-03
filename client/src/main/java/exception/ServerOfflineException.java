package exception;

public class ServerOfflineException extends Exception {
    public ServerOfflineException(String message) {
        super(message);
    }

    public ServerOfflineException() {
        super();
    }
}
