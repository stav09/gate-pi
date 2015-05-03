package au.stav;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class Events {

    private Map<Action, List<Consumer<String>>> handlers = new HashMap<>();

    private ExecutorService callbackPool = Executors.newSingleThreadExecutor();
    
    private static Logger log = Log.getLogger(Events.class);
    
    private static Events instance = new Events();
    
    private Events() {}
    
    public static void on(Action a, Consumer<String> eventHandler) {
        synchronized (instance) {
            if (instance.handlers.containsKey(a)) {
                instance.handlers.get(a).add(eventHandler);
            } else {
                List<Consumer<String>> actionList = new ArrayList<>();
                actionList.add(eventHandler);
                instance.handlers.put(a, actionList);
            }
        }
    }

    public static void fire(Action a, String message) {
        synchronized (instance) {
            log.info(String.format("Firing event %s with message '%s'", a, message));
            
            if (instance.handlers.containsKey(a)) {
                instance.handlers.get(a).forEach(c -> {
                    instance.callbackPool.execute(() -> {
                    	try {
							c.accept(message);
						} catch (Exception e) {
							evict(c);
						}
                    });
                });
            }
        }
    }
    
    public static void evict(Consumer<String> handler) {
    	synchronized (instance) {
    		instance.handlers.entrySet().forEach(e -> {
    			if (e.getValue().contains(handler)) {
    				e.getValue().remove(handler);
    			}
    		});
		}
    }
}
