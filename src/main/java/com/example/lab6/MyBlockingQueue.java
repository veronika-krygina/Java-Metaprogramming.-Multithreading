package com.example.lab6;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyBlockingQueue {
    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

    private static class Adder implements Runnable {

        @Override
        public void run() {
            System.out.println("Adder thread started");
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(1);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println("Adder thread exited");
        }
    }

    private static class Averager implements Runnable {
        private int numbersTaken = 0;

        public synchronized double getAvg() {
            return avg;
        }

        public synchronized void setAvg(double avg) {
            this.avg = avg;
        }

        private double avg = 0;
        @Override
        public void run() {
            System.out.println("Averager thread started");
            while(true) {
                try {
                    int next = queue.take();
                    double tempavg = getAvg();
                    tempavg = numbersTaken * tempavg;
                    numbersTaken++;
                    tempavg += next;
                    tempavg = tempavg / numbersTaken;
                    setAvg(tempavg);
                    System.out.println("New average: " + getAvg());
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("Averager thread exited");

        }
    }
    public static void main(String[] args) {
        Thread adderThread = new Thread(new Adder());
        Thread averagerThread = new Thread(new Averager());
        adderThread.start();
        averagerThread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        averagerThread.interrupt();

    }
}
