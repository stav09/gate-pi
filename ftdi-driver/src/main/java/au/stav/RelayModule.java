package au.stav;


public class RelayModule {

    private boolean relayStates[] = { false, false, false, false };

    private FtdiConnection device;
    
    private static RelayModule instance = new RelayModule();
    
    private RelayModule() {
        device = new FtdiConnection();
        setStates();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            instance.device.close();
        }));
    }

    private void setStates() {
        byte value = 0;
        for (int i = 0; i < relayStates.length; i++) {
            if (relayStates[i])
                value += (1 << i);
        }
        device.write(value);
    }
    
    public static void setRelay(int id, boolean on) {
        if (isOn(id) != on) {
            instance.relayStates[id] = on;
            instance.setStates();
        }
    }

    public static boolean isOn(int id) {
        return instance.relayStates[id];
    }
    
    public static boolean toggleRelay(int id) {
        setRelay(id, !isOn(id));
        return isOn(id);
    }
    
    public static void pulseRelay(int id, boolean on, int durationMs) {        
        new Thread(() -> {
            try {
                setRelay(id, on);
                Thread.sleep(durationMs);
                setRelay(id, !on);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
