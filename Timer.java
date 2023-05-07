import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class Timer extends AnimationTimer {
    private long startTime;
    private long elapsedNanos;
    private long elapsedSeconds;
    private long elapsedMilliseconds;
    private Label secondsLabel;

    public Timer(Label secondsLabel) {
        this.secondsLabel = secondsLabel;
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
        secondsLabel.setText(String.format("%d s", elapsedSeconds));
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