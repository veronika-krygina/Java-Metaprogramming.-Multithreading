package com.example.lab6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimeMultiplesController {

    @FXML
    private TextField fromInput;
    @FXML
    private TextField toInput;
    @FXML
    private TextArea output;

    @FXML
    private Button playPauseBtn;
    private boolean isPaused = false;
    @FXML private Button restartBtn;
    @FXML private Button stopBtn;

    private volatile List<String> queue = Collections.synchronizedList(new ArrayList<String>());
    private PrimeMultiples primeMultiples = new PrimeMultiples(this::addToTextArea, this::finish);

    private class PrimeMultiples implements Runnable {
        private Thread thisThread;
        private int from = 0;
        private int to = 0;
        private Runnable appendRes;
        private volatile String lastFoundStr;
        private Runnable finish;
        private boolean paused;
        private boolean stopped;

        public synchronized int getFrom() {
            return from;
        }

        public synchronized void setFrom(int from) {
            this.from = from;
        }

        public synchronized int getTo() {
            return to;
        }

        public synchronized void setTo(int to) {
            this.to = to;
        }

        public synchronized String getLastFoundStr() {
            return lastFoundStr;
        }

        public synchronized void setLastFoundStr(String lastFoundStr) {
            this.lastFoundStr = lastFoundStr;
            queue.add(lastFoundStr);
        }

        public synchronized boolean isPaused() {
            return paused;
        }

        public synchronized void setPaused(boolean paused) {
            this.paused = paused;
        }

        public synchronized boolean isStopped() {
            return stopped;
        }

        public synchronized void setStopped(boolean stopped) {
            this.stopped = stopped;
        }

        public PrimeMultiples( Runnable appendResultFunc, Runnable finishFunc) {
            this.appendRes = appendResultFunc;
            this.finish = finishFunc;

        }

        @Override
        public void run() {
            for (int i = from; i <= to; i++) {
                try {
                    setLastFoundStr(i + ": ");
                    if (appendRes != null)
                        Platform.runLater(appendRes);
                    for (int j = 1; j <= i; j++) {

                        if (i % j == 0 & isPrime(j)) {
                            setLastFoundStr(j + " ");
                            if (appendRes != null)
                                Platform.runLater(appendRes);
                        }
                    }
                    setLastFoundStr("\n");
                    if (appendRes != null)
                        Platform.runLater(appendRes);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    while (isPaused()) {
                        try {
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e1) {
                            if (isStopped()) {
                                break;
                            }
                        }
                    }
                    if (isStopped()) {
                        break;
                    }
                }


            }
            if (finish != null)
                Platform.runLater(finish);
        }
        private boolean isPrime(int i) {
            if (i == 1) return false;
            if (i < 0) return false;
            if (i == 2) return true;
            for (int j = 2; j <= i / 2; j++) {
                if (i % j == 0) return false;
            }
            return true;
        }


        public void start() {
            thisThread = new Thread(this);
            setPaused(false);
            setStopped(false);
            thisThread.start();
        }

        public void  pause() {
            setPaused(true);
            thisThread.interrupt();
        }

        public void resume() {
            setPaused(false);
            thisThread.interrupt();
        }

        public void stop() {
            setStopped(true);
            thisThread.interrupt();
        }

    }

    @FXML private void playPause(ActionEvent actionEvent) {
        if (isPaused) {
            primeMultiples.resume();
            restartBtn.setDisable(true);
            isPaused = false;
        }
        else {
            primeMultiples.pause();
            isPaused = true;
            restartBtn.setDisable(false);

        }
    }
    @FXML private void stopClick(ActionEvent actionEvent) {
        playPauseBtn.setDisable(true);
        primeMultiples.stop();
    }


    @FXML private void startClick(ActionEvent actionEvent) {
        try {
            primeMultiples.setTo(Integer.parseInt(toInput.getText()));
            primeMultiples.setFrom(Integer.parseInt(fromInput.getText()));
            output.setText("");
            playPauseBtn.setDisable(false);
            restartBtn.setDisable(true);
            stopBtn.setDisable(false);
            primeMultiples.start();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect range");
            alert.showAndWait();

        }
    }

    private volatile int outputCount = 0;
    private synchronized void addToTextArea() {
        try {
            String str = queue.get(outputCount++);
            output.setText(output.getText() + str);
        } catch (Exception ignored) {
        }
    }
    private void finish() {
        restartBtn.setDisable(false);
        stopBtn.setDisable(true);
        playPauseBtn.setDisable(true);
    }
}