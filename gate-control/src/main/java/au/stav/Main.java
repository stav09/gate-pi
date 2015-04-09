package au.stav;

public class Main {

    public static void main(String ... args) throws InterruptedException {
        
        GPIO.initialise(new String[][] {
            {"GPIO_07", "PULL_DOWN"},
            {"GPIO_14", "PULL_DOWN"},
            {"GPIO_05", "PULL_UP"},
            {"GPIO_06", "PULL_UP"}
        });
        
        GPIO.addInputListener("GPIO_05", (isHigh) -> {
            if (!isHigh) {
                RelayModule.pulseRelay(0, true, 1500);
            }
        });
        
        while(true) Thread.sleep(1000);
    }

}
