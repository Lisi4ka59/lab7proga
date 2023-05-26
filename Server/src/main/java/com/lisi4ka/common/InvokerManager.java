package com.lisi4ka.common;

import static com.lisi4ka.common.ServerApp.cities;
import static com.lisi4ka.common.ServerApp.queueMap;

public class InvokerManager implements Runnable {
    String socketChannel;
    String commandText;
    String user;

    public InvokerManager(String socketChannel, String commandText, String user) {
        this.commandText = commandText;
        this.socketChannel = socketChannel;
        this.user = user;
    }

    @Override
    public void run() {
        Invoker invoker = new Invoker(cities);
        queueMap.get(socketChannel).add(invoker.run(commandText, user));
    }
}