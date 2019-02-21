/**
 * Description the game timer.
 *
 * @author Statkevich Nataliya.
 */

class TimerClock {

    private float milliSecPerCycle;
    private long lastUpdate;
    private boolean isPaused;

    //The number of cycles that have elapsed and have not yet been polled.
    private int elapsedCycles;
    //The amount of excess time towards the next elapsed cycle.
    private float excessCycles;

    TimerClock(float cyclesPerSecond) {
        setCyclesPerSecond(cyclesPerSecond);
        reset();
    }

    private static long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }

    void setCyclesPerSecond(float cyclesPerSecond) {
        this.milliSecPerCycle = (1.0f / cyclesPerSecond) * 1000;
    }

    //Resets the timer statistics.
    void reset() {
        this.elapsedCycles = 0;
        this.excessCycles = 0.0f;
        this.lastUpdate = getCurrentTime();
        this.isPaused = false;
    }

    void update() {
        long currUpdate = getCurrentTime();
        float delta = (float) (currUpdate - lastUpdate) + excessCycles;

        if (!isPaused) {
            this.elapsedCycles += (int) Math.floor(delta / milliSecPerCycle);
            this.excessCycles = delta % milliSecPerCycle;
        }
        this.lastUpdate = currUpdate;
    }

    void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    boolean hasElapsedCycle() {
        if (elapsedCycles > 0) {
            this.elapsedCycles--;
            return true;
        }
        return false;
    }
}
