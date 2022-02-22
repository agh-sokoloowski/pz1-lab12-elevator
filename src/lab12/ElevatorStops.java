package lab12;

public class ElevatorStops {
    static final int FLOORS = 10;
    static final int MIN_FLOOR = 0;
    static final int MAX_FLOOR = FLOORS - 1;
    private static ElevatorStops INSTANCE;
    boolean[] stopsDown = new boolean[FLOORS];
    boolean[] stopsUp = new boolean[FLOORS];

    private ElevatorStops () {}

    static ElevatorStops get() {
        if (INSTANCE == null) {
            INSTANCE = new ElevatorStops();
        }

        return INSTANCE;
    }

    void setLiftStopUp(int floor) {
        stopsUp[floor] = true;
    }

    void setLiftStopDown(int floor) {
        stopsDown[floor] = true;
    }

    void clearStopUp(int floor) {
        stopsUp[floor] = false;
    }

    void clearStopDown(int floor) {
        stopsDown[floor] = false;
    }

    boolean hasStopAbove(int floor) {
        for (int i = floor + 1; i < MAX_FLOOR; i++) {
            if (stopsUp[i] || stopsDown[i]) return true;
        }
        return false;
    }

    boolean hasStopBelow(int floor) {
        for (int i = floor - 1; i >= MIN_FLOOR; i--) {
            if (stopsUp[i] || stopsDown[i]) return true;
        }
        return false;

    }

    int getMaxSetFloor() {
        for (int i = MAX_FLOOR; i >= MIN_FLOOR; i--) {
            if (stopsUp[i] || stopsDown[i]) return i;
        }
        return 0;
    }

    int getMinSetFloor() {
        for (int i = MIN_FLOOR; i <= MAX_FLOOR; i++) {
            if (stopsUp[i] || stopsDown[i]) return i;
        }
        return 0;
    }

    boolean whileMovingDownShouldStopAt(int floor) {
        return stopsDown[floor];
    }

    boolean whileMovingUpShouldStopAt(int floor) {
        return stopsUp[floor];
    }
}
