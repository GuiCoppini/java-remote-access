package main;

public class ScreenSharer implements Runnable {

    private Connection connection;

    public ScreenSharer(Connection c) {
        this.connection = c;
    }

    @Override
    public void run() {
        while (ScreenUtils.isSharing) {
            try {
                ScreenUtils.sendScreenshot(connection, true);
                Thread.sleep(250);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        connection.sendMessage(new Message("stop-screenshare"));
    }
}
