package lab12;

public class Elevator {
    // tworzymy 3 wątki
    static ElevatorCar car = new ElevatorCar();
    static ExternalPanelsAgent externalPanelAgent = new ExternalPanelsAgent(car);
    static InternalPanelAgent internalPanelAgent = new InternalPanelAgent(car);

    // symulacja przywołania windy z panelu zewnętrznego
    static void makeExternalCall(int atFloor, boolean directionUp) {
        try {
            externalPanelAgent.input.put(new ExternalPanelsAgent.ExternalCall(atFloor, directionUp));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // symulacja wyboru pietra w panelu wewnętrznym
    static void makeInternalCall(int toFloor) {
        try {
            internalPanelAgent.input.put(new InternalPanelAgent.InternalCall(toFloor));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // uruchomienie wątków
    static void init() {
        car.start();
        externalPanelAgent.start();
        internalPanelAgent.start();
    }

    static void wait(int floors) {
        try {
            Thread.sleep((floors * 500L) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    static void scenario1() {
        System.err.println("Expected: 0 -> 4 -> 2");
        makeExternalCall(4, false);
        wait(4);
        makeInternalCall(2);
    }

    @SuppressWarnings("unused")
    static void scenario2() {
        System.err.println("Expected: 0 -> 8 -> 3 -> 4 -> 6 -> 8");
        makeExternalCall(8, false);
        wait(8);
        makeInternalCall(3);
        wait(5);
        makeExternalCall(4, true);
        wait(1);
        makeInternalCall(6);
        wait(1);
        makeInternalCall(8);
    }

    @SuppressWarnings("unused")
    static void scenario3() {
        System.err.println("Expected: 0 -> 3 -> 8");
        makeExternalCall(3, true);
        wait(3);
        makeInternalCall(8);
    }

    @SuppressWarnings("unused")
    static void scenario4() {
        System.err.println("Expected: 0 -> 2 -> 3 -> 6 -> 7");
        makeExternalCall(2, true);
        wait(2);
        makeInternalCall(3);
        wait(6);
        makeExternalCall(6, true);
        wait(6);
        makeInternalCall(7);
    }

    @SuppressWarnings("unused")
    static void scenario5() {
        System.err.println("Expected: 0 -> 4 -> 6");
        // Chcemy jechać w dół, ale w międzyczasie zmieniamy zdanie.
        // Pojedziemy w górę, bo nie ma żadnych przystanków na dole
        makeExternalCall(4, false);
        wait(4);
        makeInternalCall(6);
    }

    @SuppressWarnings("unused")
    static void scenario6() {
        System.err.println("Expected: 0 -> 4 -> 2 -> 1 -> 6");
        // Chcemy jechać w dół, ale w międzyczasie zmieniamy zdanie.
        // Tym razem pozwiedzamy piętra, bo winda miała jechać w dół
        makeExternalCall(4, false);
        wait(3);
        makeExternalCall(2, false);
        wait(1);
        makeInternalCall(6);
        wait(1);
        makeInternalCall(1);
    }


    @SuppressWarnings("CommentedOutCode")
    public static void main(String[] args) {
        init();
        scenario1();
//        scenario2();
//        scenario3();
//        scenario4();
//        scenario5();
//        scenario6();
    }
}
