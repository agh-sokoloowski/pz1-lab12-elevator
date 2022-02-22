package lab12;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class InternalPanelAgent extends Thread {
    BlockingQueue<InternalCall> input = new ArrayBlockingQueue<>(100);
    ElevatorCar elevatorCar;

    InternalPanelAgent(ElevatorCar elevatorCar) {
        this.elevatorCar = elevatorCar;
    }

    public void run() {
        for (; ; ) {
            // odczytaj wezwanie z kolejki
            InternalPanelAgent.InternalCall ic = null;
            try {
                ic = input.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // w zależności od aktualnego piętra, na którym jest winda,
            // umieść przystanek w odpowiedniej tablicy "EleveatorStops"
            assert ic != null;
            if (ic.toFloor > elevatorCar.getFloor()) {
                ElevatorStops.get().setLiftStopUp(ic.toFloor);
            } else {
                ElevatorStops.get().setLiftStopDown(ic.toFloor);
            }
        }
    }

    static class InternalCall {
        private final int toFloor;

        InternalCall(int toFloor) {
            this.toFloor = toFloor;
        }
    }

}
