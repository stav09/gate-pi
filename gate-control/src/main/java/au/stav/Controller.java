package au.stav;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import au.stav.Alias.Input;
import au.stav.Alias.Output;

public class Controller implements Runnable {

	private Logger log = Log.getLogger(Controller.class);
	
    public void run() {
        
        GPIO.initialise(new String[][] {
            {Input.GATE_FULLY_CLOSED, "PULL_DOWN"},
            {Input.GATE_FULLY_OPEN,   "PULL_DOWN"},
            {Input.RC_CHANNEL_1,      "PULL_UP"},
            {Input.RC_CHANNEL_2,      "PULL_UP"}
        });

        RelayModule.setRelay(Output.GATE_HOLD, true);
        
        GPIO.onChange(Input.RC_CHANNEL_1, (high) -> {
        	log.info("RC_CH_1:" + high);
            if (!high) {
                Events.fire(Action.GATE_OPEN, null);
            }
        });
        
        GPIO.onChange(Input.RC_CHANNEL_2, (high) -> {
        	log.info("RC_CH_2:" + high);
        	if (!high) {
        		RelayModule.toggleRelay(Output.LIGHTS);
        		Events.fire(Action.STATUS_UPDATE, null);
        	}
        });        
        
        GPIO.onChange(Input.GATE_FULLY_CLOSED, (high) -> {
            Events.fire(Action.STATUS_UPDATE, null);
            if (!high) {
                RelayModule.pulseRelay(Output.LIGHTS, true, 20000);
            }
        });
        
        GPIO.onChange(Input.GATE_FULLY_OPEN, (high) -> {
            Events.fire(Action.STATUS_UPDATE, null);
        });
        
        Events.on(Action.GATE_OPEN, (msg) -> {
            if (GPIO.isHigh(Input.GATE_FULLY_CLOSED)) {
                RelayModule.pulseRelay(Output.GATE_OPEN, true, 1500);
                RelayModule.setRelay(Output.LIGHTS, true);
            }
        });
        
        Events.on(Action.GATE_CLOSE, (msg) -> {
            if (GPIO.isHigh(Input.GATE_FULLY_OPEN)) {
                RelayModule.pulseRelay(Output.GATE_OPEN, true, 1500);
            }
        });
        
        Events.on(Action.GATE_HOLD, (msg) -> {
            RelayModule.setRelay(Output.GATE_HOLD, false);
            Events.fire(Action.STATUS_UPDATE, null);
        });

        Events.on(Action.GATE_RELEASE, (msg) -> {
            RelayModule.setRelay(Output.GATE_HOLD, true);
            Events.fire(Action.STATUS_UPDATE, null);
        });
        
        Events.on(Action.LIGHTS_ON, (msg) -> {
            RelayModule.setRelay(Output.LIGHTS, true);
            Events.fire(Action.STATUS_UPDATE, null);
        });

        Events.on(Action.LIGHTS_OFF, (msg) -> {
            RelayModule.setRelay(Output.LIGHTS, false);
            Events.fire(Action.STATUS_UPDATE, null);
        });
        
        Events.on(Action.STATUS_UPDATE, (msg) -> {
            String statusMsg = String.format(
                "{\"gate_closed\":%b,\"gate_opened\":%b,\"gate_hold\":%b,\"lights_on\":%b}",
                GPIO.isHigh(Input.GATE_FULLY_CLOSED),
                GPIO.isHigh(Input.GATE_FULLY_OPEN),
                !RelayModule.isOn(Output.GATE_HOLD),
                RelayModule.isOn(Output.LIGHTS)
            );
            Events.fire(Action.NOTIFY, statusMsg);
        });
    }
}
