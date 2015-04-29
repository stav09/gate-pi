package au.stav;

import au.stav.Alias.Input;
import au.stav.Alias.Output;

public class Startup implements Runnable {

    public void run() {
        
        GPIO.initialise(new String[][] {
            {Input.GATE_FULLY_CLOSED, "PULL_DOWN"},
            {Input.GATE_OPENED,       "PULL_DOWN"},
            {Input.RC_CHANNEL_1,      "PULL_UP"},
            {Input.RC_CHANNEL_2,      "PULL_UP"}
        });

        GPIO.onChange(Input.RC_CHANNEL_1, (isHigh) -> {
            if (!isHigh) {
                Events.fire(Action.OPEN_GATE, null);
            }
        });
        
        Events.on(Action.OPEN_GATE, (msg) -> {
            RelayModule.pulseRelay(Output.GATE_OPEN, true, 1500);
            Events.fire(Action.NOTICE, "Gate Opened");
        });
    }
}
