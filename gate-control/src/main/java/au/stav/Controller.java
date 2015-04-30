package au.stav;

import au.stav.Alias.Input;
import au.stav.Alias.Output;

public class Controller implements Runnable {

    public void run() {
        
        GPIO.initialise(new String[][] {
            {Input.GATE_FULLY_CLOSED, "PULL_DOWN"},
            {Input.GATE_OPENED,       "PULL_DOWN"},
            {Input.RC_CHANNEL_1,      "PULL_UP"},
            {Input.RC_CHANNEL_2,      "PULL_UP"}
        });

        GPIO.onChange(Input.RC_CHANNEL_1, (isHigh) -> {
            if (!isHigh) {
                Events.fire(Action.GATE_OPEN, null);
            }
        });
        
        Events.on(Action.GATE_OPEN, (msg) -> {
            RelayModule.pulseRelay(Output.GATE_OPEN, true, 1500);
            Events.fire(Action.NOTIFY, "GATE_OPENED"); //FIXME: Should notify from GATE_FULLY_CLOSED change
        });
        
        Events.on(Action.GET_STATUS, (msg) -> {
            String statusMsg = String.format(
                "STATUS:{\"gate_closed\":%b,\"gate_hold\":%b,\"lights_on\":%b}",
                GPIO.isHigh(Input.GATE_FULLY_CLOSED),
                RelayModule.isOn(Output.LIGHTS),
                RelayModule.isOn(Output.GATE_HOLD)
            );
            Events.fire(Action.NOTIFY, statusMsg);
        });
    }
}
