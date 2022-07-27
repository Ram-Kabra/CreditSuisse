package com.code.logging;

public class AsyncTimer extends Thread implements Runnable {

    private static int processTime;

    @Override
    public void run() {

        int timeCounter = 0;
        while (true) {
            try {
                Thread.sleep(1);
                this.processTime = timeCounter++;
            } catch (InterruptedException e) {
                System.out.println("Second thread has been finished! The logs " +
                        "were sent to the database in the " + processTime + " ms.");
                return;
            }
        }
    }
}
