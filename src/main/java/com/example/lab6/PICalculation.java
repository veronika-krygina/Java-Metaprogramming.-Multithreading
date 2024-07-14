package com.example.lab6;


import java.util.Scanner;

public class PICalculation {

    private static volatile boolean toStop = false;
    private static class PIComputer implements Runnable {
        private volatile long sumNumber = 0;
        private volatile double pi = 0;
        private double epsilon = 0.00001;

        public synchronized long getSumNumber() {
            return sumNumber;
        }

        public synchronized void setSumNumber(long sumNumber) {
            this.sumNumber = sumNumber;
        }

        public synchronized double getPi() {
            return pi;
        }

        public synchronized void setPi(double pi) {
            this.pi = pi;
        }

        @Override
        public void run() {
            System.out.println("Started PI Computer");
            long denominator = 1;
            while (!toStop) {
                if (sumNumber % 2 == 0)
                    setPi(getPi() + 4 / (double) denominator);
                else setPi(getPi() - 4 / (double) denominator);
                setSumNumber(getSumNumber() + 1);
                denominator += 2;
                if (Long.MAX_VALUE - 2 < denominator || 4 / (double) denominator < epsilon) break;
            }
            System.out.println("PI Computer finished");
        }

        public double getEpsilon() {
            return epsilon;
        }

        public void setEpsilon(double epsilon) {
            this.epsilon = epsilon;
        }
    }
    public static void main(String[] args) {
        PIComputer comp = new PIComputer();
        comp.setEpsilon(0.0000000001);
        Thread th = new Thread(comp);
        th.start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to get current info on PI computation progress, type in exit to quit");
        while (true){
            String input = scanner.nextLine();
            if (input.trim().equalsIgnoreCase("exit")) break;
            System.out.println("PI: " + comp.getPi() + " Steps: " + comp.getSumNumber());
        }
        toStop = true;

    }
}
