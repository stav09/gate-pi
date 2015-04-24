package au.stav;

import au.stav.Alias.Input;
import au.stav.Alias.Output;

public class Main {

    public static void main(String ... args) throws InterruptedException {
        
        GPIO.initialise(new String[][] {
            {Input.GATE_FULLY_CLOSED, "PULL_DOWN"},
            {Input.GATE_OPENED, "PULL_DOWN"},
            {Input.RC_CHANNEL_1, "PULL_UP"},
            {Input.RC_CHANNEL_2, "PULL_UP"}
        });
        
        GPIO.onChange(Input.RC_CHANNEL_1, (isHigh) -> {
            if (!isHigh) {
                RelayModule.pulseRelay(Output.GATE_OPEN, true, 1500);
            }
        });
        
        while(true) Thread.sleep(1000);
    }

}
