package src.main.java.minesweeper.ui;
import javafx.animation.AnimationTimer;

public class Timer extends AnimationTimer {
    private long startTime;
    private long elapsedNanos;
    private long elapsedSeconds;
    private long elapsedMilliseconds;
    private NumberDisplay numberDisplay;

    public Timer(NumberDisplay numberDisplay) {
        this.numberDisplay = numberDisplay;
    }

    @Override
    public void start() {
        startTime = System.nanoTime();
        super.start();
    }

    @Override
    public void handle(long now) {
        elapsedNanos = now - startTime;
        elapsedSeconds = elapsedNanos / 1000000000;
        elapsedMilliseconds = (elapsedNanos / 1000000) % 1000;
        updateDisplay();
    }

    private void updateDisplay() {
        numberDisplay.update((int) elapsedSeconds);
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public long getElapsedMilliseconds() {
        return elapsedMilliseconds;
    }
};