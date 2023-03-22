package mukan.minesweeperfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class StopWatch {
    private int seconds;
    private int minutes;
    private Timeline timeline;
    private Text currTime;

    public StopWatch () {
        currTime = new Text("00:00");
        this.seconds = 0;
        this.minutes = 0;
    }

    public void start() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            seconds++;
            setTime();
        });

        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void restart () {
        seconds = 0;
        minutes = 0;
        currTime.setText("00:00");
    }

    public Text getCurrTime () {
        return currTime;
    }

    private void setTime() {
        if (seconds % 60 == 0) {
            minutes++;
            seconds = 0;
        }

        String sec = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);
        String min = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
        currTime.setText(min + ":" + sec);
    }

    public void stop() {
        timeline.stop();
    }
}

