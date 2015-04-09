package au.stav;

/**
 * Run a test by cycling through and toggling the state of each relay  
 */
public class Test {

    public static void main(String[] args) throws Exception {
        
        boolean relayStates[] = { false, false, false, false };
        
        try (FtdiConnection device = new FtdiConnection()) {
            setStates(device, relayStates);
            for (int y = 0; y < 2; y++) {
                for (int i = 0; i < relayStates.length; i++) {
                    relayStates[i] ^= true;
                    setStates(device, relayStates);
                    Thread.sleep(400);
                }
            }
        }
    }

    private static void setStates(FtdiConnection device, boolean[] relayStates) {
        byte value = 0;
        for (int i = 0; i < relayStates.length; i++) {
            if (relayStates[i])
                value += (1 << i);
        }
        device.write(value);
    }

}
