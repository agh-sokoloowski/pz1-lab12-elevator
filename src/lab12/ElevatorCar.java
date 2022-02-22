package lab12;

public class ElevatorCar extends Thread {
    int floor = 0;
    Tour tour = Tour.UP;
    Movement movementState = Movement.STOP;

    public int getFloor() {
        return floor;
    }

    public void run() {
        System.out.print("Result:   ");
        System.out.print(this.getFloor());

        for (; ; ) {
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (movementState == Movement.STOP) {
                if (ElevatorStops.get().hasStopAbove(this.getFloor())) {
                    this.movementState = Movement.MOVING;
                    this.tour = Tour.UP;
                }

                if (ElevatorStops.get().hasStopBelow(this.getFloor())) {
                    this.movementState = Movement.MOVING;
                    this.tour = Tour.DOWN;
                }
            }

            if (ElevatorStops.get().hasStopAbove(this.getFloor())) {
                if (this.tour == Tour.UP) {
                    this.floor++;

                    if (this.getFloor() != ElevatorStops.get().getMaxSetFloor()) {
                        if (ElevatorStops.get().whileMovingUpShouldStopAt(this.getFloor())) {
                            this.movementState = Movement.STOP;
                            ElevatorStops.get().clearStopUp(this.getFloor());

                            System.out.print(" -> ");
                            System.out.print(this.getFloor());
                        }
                    } else {
                        ElevatorStops.get().clearStopUp(this.getFloor());
                        ElevatorStops.get().clearStopDown(this.getFloor());
                        this.movementState = Movement.STOP;
                        this.tour = Tour.DOWN;

                        System.out.print(" -> ");
                        System.out.print(this.getFloor());
                    }

                    continue;
                }
            }

            if (ElevatorStops.get().hasStopBelow(this.getFloor())) {
                if (this.tour == Tour.DOWN) {
                    this.floor--;

                    if (this.getFloor() != ElevatorStops.get().getMinSetFloor()) {
                        if (ElevatorStops.get().whileMovingDownShouldStopAt(this.getFloor())) {
                            this.movementState = Movement.STOP;
                            ElevatorStops.get().clearStopDown(this.getFloor());

                            System.out.print(" -> ");
                            System.out.print(this.getFloor());
                        }
                    } else {
                        ElevatorStops.get().clearStopUp(this.getFloor());
                        ElevatorStops.get().clearStopDown(this.getFloor());
                        this.movementState = Movement.STOP;
                        this.tour = Tour.UP;

                        System.out.print(" -> ");
                        System.out.print(this.getFloor());
                    }
                }
            }
        }
    }

    enum Tour {UP, DOWN}

    enum Movement {STOP, MOVING}
}
