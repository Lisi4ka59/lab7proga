package com.lisi4ka;

import com.lisi4ka.utils.CommandMap;
import com.lisi4ka.utils.PackagedCommand;
import com.lisi4ka.utils.PackagedResponse;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

import static com.lisi4ka.utils.Serializer.serialize;
import static java.lang.Thread.sleep;


public class ClientApp {
    static boolean serverWork = true;
    static Queue<ByteBuffer> queue = new LinkedList<>();
    public static CommandMap commandMap = null;
    static boolean connectionAccepted = true;
    static boolean writeFlag = true;

    private void run() {
        try {
            while (true) {
                boolean doneStatus = true;
                serverWork = true;
                InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("localhost"), 9857);
                Selector selector = Selector.open();
                SocketChannel sc = SocketChannel.open();
                sc.configureBlocking(false);
                sc.connect(addr);
                sc.register(selector, SelectionKey.OP_CONNECT |
                        SelectionKey.OP_READ | SelectionKey.
                        OP_WRITE);
                while (true) {
                    if (selector.select() > 0) {
                        try {
                            doneStatus = processReadySet(selector.selectedKeys());

                        } catch (ConnectException ex) {
                            System.out.println("Lost server connection. Repeat connecting in 10 seconds");
                            break;
                        }
                        if (doneStatus || !serverWork) {
                            break;
                        }
                    }
                }
                if (doneStatus) {
                    break;
                }
                sc.close();
                System.out.println("Lost server connection. Repeat connecting in 10 seconds");
                connectionAccepted = true;
                sleep(10000);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            System.out.println("Client died due to unforeseen circumstances!");
            System.exit(0);
        }
    }
        public static Boolean processReadySet (Set readySet)
            throws Exception {

            SelectionKey key = null;
            Iterator iterator = readySet.iterator();
            while (iterator.hasNext()) {
                key = (SelectionKey) iterator.next();
                iterator.remove();
            }
            writeFlag = false;
            assert key != null;
            if (key.isConnectable()) {
                boolean connected = false;
                try {
                    connected = processConnect(key);
                } catch (Exception e) {
                    System.out.println("Lost server connection");
                }
                if (!connected) {
                    return false;
                }
            }

            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer bb = ByteBuffer.allocate(8192);
                try {
                    sc.read(bb);
                } catch (Exception e) {
                    serverWork = false;
                    return false;
                }
                String result = new String(bb.array()).trim();
                byte[] data = Base64.getDecoder().decode(result);
                PackagedResponse packagedResponse = null;
                try {
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                        packagedResponse = (PackagedResponse) ois.readObject();
                        ois.close();
                } catch (EOFException ignored) {}
                if (packagedResponse != null) {
                    if (packagedResponse.getMessage() != null) {
                        System.out.print(packagedResponse.getMessage());
                        if (packagedResponse.getMessage().equals("Connection refused")){
                            System.exit(0);
                        }
                        if (packagedResponse.getPackageCount() <= packagedResponse.getPackageNumber()){
                            writeFlag = true;
                        }
                    } else if (packagedResponse.getCommandMap() != null) {
                        commandMap = packagedResponse.getCommandMap();
                    }
                }
            }
            if (key.isWritable() && writeFlag && commandMap != null) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (queue.isEmpty()) {
                    for (PackagedCommand packagedCommand : ClientValidation.validation()) {
                        if ("exit".equals(packagedCommand.getCommandName())) {
                            return true;
                        }
                        queue.add(ByteBuffer.wrap(serialize(packagedCommand)));
                    }
                }
                if (!queue.isEmpty()) {
                    try {
                        socketChannel.write(queue.poll());
                    } catch (Exception e) {
                        System.out.println("Error while sending message!");
                        connectionAccepted = false;
                    }
                }
                return false;
            }
            return false;
        }
    public static Boolean processConnect(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            while (sc.isConnectionPending()) {
                sc.finishConnect();
            }
        } catch (IOException e) {
            key.cancel();
            serverWork = false;
            return false;
        }
        return true;
    }
    public static void clientRun() {
        ClientApp clientApp = new ClientApp();
        clientApp.run();
    }
}