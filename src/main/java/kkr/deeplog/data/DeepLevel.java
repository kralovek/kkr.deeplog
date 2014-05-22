package kkr.deeplog.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeepLevel {

	public static Map<String, List<DeepLevel>> levelsLayout = Collections
			.synchronizedMap(new HashMap<String, List<DeepLevel>>());

	private long time;
	private long memory;
	private boolean ok = false;
	private int level = 0;

	public DeepLevel(int level, long time, long memory) {
		this.level = level;
		this.time = time;
		this.memory = memory;
	}

	private static synchronized List<DeepLevel> getLevels(DeepId deepId) {
		List<DeepLevel> deepLevels = levelsLayout.get(deepId.getId());
		if (deepLevels == null) {
			deepLevels = new ArrayList<DeepLevel>();
			levelsLayout.put(deepId.getId(), deepLevels);
		}
		return deepLevels;
	}

	public static synchronized void initAll() {
		levelsLayout.clear();
	}

	public static synchronized void init(DeepId deepId) {
		levelsLayout.remove(deepId.getId());
	}

	public static synchronized DeepLevel getLastLevel(DeepId deepId) {
		List<DeepLevel> deepLevels = getLevels(deepId);
		if (deepLevels.size() == 0) {
			return DeepLevel.addNewLevel(deepId, new Date().getTime(), memory());
		} else {
			return deepLevels.get(deepLevels.size() - 1);
		}
	}

	public static synchronized DeepLevel removeLastLevel(DeepId deepId) {
		List<DeepLevel> deepLevels = getLevels(deepId);
		if (deepLevels.size() == 0) {
			DeepLevel.addNewLevel(deepId, new Date().getTime(), memory());
			return removeLastLevel(deepId);
		} else {
			return deepLevels.remove(deepLevels.size() - 1);
		}
	}

	public static synchronized DeepLevel addNewLevel(DeepId deepId, long time, long memory) {
		List<DeepLevel> deepLevels = getLevels(deepId);
		DeepLevel deepLevel = new DeepLevel(deepLevels.size(), time, memory);
		deepLevels.add(deepLevel);
		return deepLevel;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public int getLevel() {
		return level;
	}

	public long getTime() {
		return time;
	}

	public long getMemory() {
		return memory;
	}

	private static long memory() {
		return 0;
	}
	
	public String toString() {
		return "[" + level + "] " + (ok ? "OK" : "KO");
	}
}
