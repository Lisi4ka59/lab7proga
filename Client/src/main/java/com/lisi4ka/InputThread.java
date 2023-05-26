package com.lisi4ka;

import com.lisi4ka.utils.PackagedCommand;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static com.lisi4ka.utils.Serializer.serialize;


public class InputThread implements Runnable{
    SocketChannel socketChannel;
    public InputThread(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }
    @Override
    public void run() {
        if (ClientApp.queue.isEmpty()) {
            ClientApp.writeFlag = false;
            for (PackagedCommand packagedCommand : ClientValidation.validation()) {
                if ("exit".equals(packagedCommand.getCommandName())) {
                    System.exit(0);
                }
                try {
                    ClientApp.queue.add(ByteBuffer.wrap(serialize(packagedCommand)));
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            }
            ClientApp.writeFlag =true;
        }
        if (!ClientApp.queue.isEmpty()) {
            try {
                socketChannel.write(ClientApp.queue.poll());
            } catch (Exception e) {
                System.out.println("Error while sending message!");
            }
        }
    }
}
