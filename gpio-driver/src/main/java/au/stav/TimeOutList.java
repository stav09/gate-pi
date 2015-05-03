package au.stav;

import java.util.HashMap;

public class TimeOutList<T> {

	private volatile HashMap<T, Long> list = new HashMap<>();

	private long interval;

	public TimeOutList(long interval) {
		this.interval = interval;
	}

	public void put(T entry) {
		long timeout = System.currentTimeMillis() + interval;
		list.put(entry, timeout);
	}
	
	public boolean isTimedOut(T entry) {
		if (!list.containsKey(entry)) {
			return false;
		} else {
			long timeout = list.get(entry);
			if (timeout < System.currentTimeMillis()) {
				list.remove(entry);
				return false;
			} else {
				return true;
			}
		}
		
	}
}
