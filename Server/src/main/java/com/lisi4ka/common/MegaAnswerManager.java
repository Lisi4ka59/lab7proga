package com.lisi4ka.common;

import com.lisi4ka.utils.PackagedResponse;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.RecursiveAction;

public class MegaAnswerManager extends RecursiveAction {
    public static volatile Queue<PackagedResponse> answerQueue = new LinkedList<>();
    public static volatile Queue<SelectionKey> keyQueue = new LinkedList<>();
    static volatile int threads = 0;
    @Override
    protected void compute() {
        int i = 0;
        threads++;
        if (threads < 1) {
            MegaAnswerManager megaAnswerManager1 = new MegaAnswerManager();
            MegaAnswerManager megaAnswerManager2 = new MegaAnswerManager();
            megaAnswerManager1.compute();
            megaAnswerManager2.compute();
        } else {
            try {
                while (true) {
                    if(i<keyQueue.size()){
                        i=keyQueue.size();
                    }
                    if (!keyQueue.isEmpty()) {
                        SelectionKey key = keyQueue.poll();
                        if (key != null){
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            if (!answerQueue.isEmpty()) {
                                PackagedResponse answer = answerQueue.poll();
                                AnswerManager answerManager = new AnswerManager(socketChannel, answer);
                                answerManager.run();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
