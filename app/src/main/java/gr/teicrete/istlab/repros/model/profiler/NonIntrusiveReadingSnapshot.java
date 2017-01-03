package gr.teicrete.istlab.repros.model.profiler;

/**
 * Created by Antonis on 30-Dec-16.
 */

public class NonIntrusiveReadingSnapshot {

    private long timestamp;
    private boolean motionDetected;
    private double lightLevel;
    private double audioLevel;

    public NonIntrusiveReadingSnapshot() {
    }

    public NonIntrusiveReadingSnapshot(long timestamp, boolean motionDetected, double lightLevel, double audioLevel) {
        this.timestamp = timestamp;
        this.motionDetected = motionDetected;
        this.lightLevel = lightLevel;
        this.audioLevel = audioLevel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    public double getLightLevel() {
        return lightLevel;
    }

    public double getAudioLevel() {
        return audioLevel;
    }

    public static double booleanToDouble(boolean value) {
        if (value) {
            return 1.0;
        } else {
            return 0;
        }
    }
}
